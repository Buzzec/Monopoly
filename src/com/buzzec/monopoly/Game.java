package com.buzzec.monopoly;

import com.buzzec.monopoly.util.Helper;
import com.buzzec.monopoly.util.logging.Log;
import com.buzzec.monopoly.player.Player;
import com.buzzec.monopoly.space.Card;
import com.buzzec.monopoly.space.Cost;
import com.buzzec.monopoly.space.Space;
import com.buzzec.monopoly.space.property.Property;
import com.buzzec.monopoly.space.property.Railroad;
import com.buzzec.monopoly.space.property.Utility;
import com.buzzec.monopoly.util.Reference;
import exceptions.UnknownProperty;

import java.util.ArrayList;

/**
 * Created by bbett on 7/1/2017.
 * @author Brett Etter
 */
public class Game {
    /**
     * currentTurn: used to track the current player to have a turn
     * streak: used to track a streak of doubles
     */
    private int
            currentTurn,
            streak;
    /**
     * board: ArrayList containing all the board information
     */
    private final ArrayList<Space>
            board;
    /**
     * players: ArrayList containing all players still in the game
     * playersOut: ArrayList containing all players who are out
     */
    private final ArrayList<Player>
            players,
            playersOut;
    /**
     * toggle: true if doubles let you take another turn
     */
    private boolean
            toggle;
    /**
     * log: Log object used to log things
     */
    private Log
            log;

    public Game(ArrayList<Space> board, ArrayList<Player> players, int startingMoney, Log  log){
        this.board = board;
        for(Player x : players){
            x.setMoney(startingMoney);
        }
        this.players = players;
        currentTurn = 0;
        streak = 0;
        toggle = true;
        this.log = log;
        playersOut = new ArrayList<>();
    }
    /**
     * Constructor used to analyse a file and generate a game board
     * @param fileName Name of file to be analyzed
     * @param players ArrayList of players to play the game, must be greater than one in size
     * @param startingMoney How much money each player starts with
     * @param log Log object used for logging this game
     */
    public Game(String fileName, ArrayList<Player> players, int startingMoney, Log log){
        this(Helper.generateBoard(fileName, log), players, startingMoney, log);
    }

    //TODO add logging to doTurn()
    /**
     * Used to run a turn of the game
     * @return true if game continues, false if game is over
     */
    public boolean doTurn(){
        //Set currentPlayer
        Player currentPlayer = players.get(currentTurn);

        log.log("---------------");
        log.log("PLAYER " + currentPlayer.getPlayerNumber() + "'S TURN");
        log.log("---------------");
        log.log("Properties:");
        StringBuilder output = new StringBuilder();
        for(int x = 0; x < currentPlayer.getProperties().size(); x++){
            if(x != 0){
                output.append(", ");
            }
            output.append(currentPlayer.getProperties().get(x).getName());
        }
        log.log(output);
        log.log("Money: " + currentPlayer.getMoney());
        log.log("Jail Time Left: " + currentPlayer.getJailTimeLeft());
        log.log("Location: " + currentPlayer.getLocation());
        //Roll Dice
        int rollOne = Helper.rollDie(log);
        int rollTwo = Helper.rollDie(log);

        //Give currentPlayer before turn actions
        currentPlayer.beforeTurn(log);

        //3 doubles in a row and jail
        if(rollOne == rollTwo){
            streak++;
            if(streak == Reference.DOUBLES_TO_JAIL){
                sendToJail(currentPlayer);
            }
        }

        //If player is in jail give action (to determine if paying or not)
        if(currentPlayer.getJailTimeLeft() != 0 && currentPlayer.leaveJail(log)){
            currentPlayer.loseMoney(Reference.JAIL_COST, log);
            currentPlayer.setJailTimeLeft(0);
        }

        //If still in jail
        if(currentPlayer.getJailTimeLeft() != 0){
            //Doubles!
            if(rollOne == rollTwo){
                currentPlayer.setJailTimeLeft(0);
                //Doubles don't repeat turn
                toggle = false;
            }
            //Reduce time left
            else {
                currentPlayer.setJailTimeLeft(currentPlayer.getJailTimeLeft() - 1);
                //If out of time must pay
                if(currentPlayer.getJailTimeLeft() == 0){
                    currentPlayer.loseMoney(Reference.JAIL_COST, log);
                    if(checkOut()){
                        return players.size() > 1;
                    }
                }
            }
        }

        //Only continue if out of jail
        if(currentPlayer.getJailTimeLeft() == 0) {
            //Find new spot
            int newLocation = currentPlayer.getLocation() + rollOne + rollTwo;
            while (newLocation >= board.size()) {
                currentPlayer.passGo(log);
                newLocation -= board.size();
                currentPlayer.gainMoney(Reference.GO_MONEY, log);
            }

            //Store new location
            Space location = board.get(newLocation);

            //Move currentPlayer
            currentPlayer.setLocation(newLocation);

            //Give event
            currentPlayer.landOn(location, rollOne, rollTwo, log);

            //Run landOn
            landOn(currentPlayer, location, rollOne, rollTwo);
            if(checkOut()){
                if(location.getOwner() != null && !location.getOwner().equals(currentPlayer)){
                    while(currentPlayer.getProperties().size() > 0){
                        location.getOwner().gainProperty(currentPlayer.getProperties().get(0), log);
                    }
                }
                return players.size() > 1;
            }
        }

        //If end of turn give all between turn actions
        if (rollOne != rollTwo || !toggle) {
            for(int x = currentTurn; x < players.size(); x++) {
                players.get(x).betweenTurn(log);
            }
            for(int x = 0; x < currentTurn; x++){
                players.get(x).betweenTurn(log);
            }
            if(!checkOut()){
                currentTurn++;
            }
            if (currentTurn >= players.size()) {
                currentTurn = 0;
                toggle = true;
            }
        }
        checkOut();
        return players.size() > 1;
    }
    public boolean doTurn(int moneyLimit){
        for(Player x : players){
            if(x.getMoney() >= moneyLimit){
                return false;
            }
        }
        return doTurn();
    }

    private void auction(Property prop){
        log.log("Auction for " + prop.getName());
        ArrayList<Player> remaining = new ArrayList<>();
        int highBid = 0;
        int highPlayer = -1;
        remaining.addAll(players);
        for(int x = 0; x < remaining.size(); x++){
            int bid = remaining.get(x).auction(prop, highBid, log);
            log.log("Player " + remaining.get(x).getPlayerNumber() + " bids " + bid + " on " + prop.getName());
            if(bid > highBid){
                highBid = bid;
                highPlayer = remaining.get(x).getPlayerNumber();
            }
            else{
                remaining.remove(x);
                x--;
            }
        }
        while(remaining.size() > 1){
            for(int x = 0; x < remaining.size(); x++){
                int bid = remaining.get(x).auction(prop, highBid, log);
                log.log("Player " + remaining.get(x).getPlayerNumber() + " bids " + bid + " on " + prop.getName());
                if(bid > highBid){
                    highBid = bid;
                    highPlayer = remaining.get(x).getPlayerNumber();
                }
                else{
                    remaining.remove(x);
                    x--;
                }
            }
        }
        Player winner = null;
        if(highPlayer != -1){
            for(Player x : players){
                if(x.getPlayerNumber() == highPlayer){
                    winner = x;
                    log.log("Winner is Player " + highPlayer + " at the cost of " + highBid);
                    break;
                }
            }
            if(winner != null){
                winner.loseMoney(highBid, log);
                winner.gainProperty(prop, log);
            }
        }
        else {
            log.log("No winner for " + prop.getName());
        }
    }
    public int find(String name){
        log.log("Finding " + name);
        for(int x = 0; x < board.size(); x++){
            if(board.get(x).getName().equals(name)){
                log.log("Found it at " + x);
                return x;
            }
        }
        log.log("Unsuccessful");
        return -1;
    }
    //TODO implement cards
    private void drawCard(Card card, Player player){

    }
    private void sendToJail(Player player){
        log.log("Player " + player.getPlayerNumber() + " is sent to jail");
        player.setJailTimeLeft(Reference.TURNS_IN_JAIL);
        player.setLocation(find("Jail"));
        toggle = false;
    }
    private void landOn(Player player, Space location, int rollOne, int rollTwo){
        //Determine type landed on
        Class type = location.getClass();
        log.log("Player " + player.getPlayerNumber() + " landed on " + location.getName());

        //Property, Railroad, or Helper
        if(type == Property.class || type == Railroad.class || type == Utility.class) {
            Property prop = (Property) location;
            //If no owner
            if (prop.getOwner() == null) {
                log.log("No owner");
                //Give option to buy
                if (player.buyOpenProperty(prop, log)) {
                    log.log("Chooses to buy");
                    Helper.buyProperty(prop, player, log);
                }
                //No buy then auction
                else {
                    log.log("Chooses not to buy");
                    auction(prop);
                }
            }
            //If owned
            else {
                log.log("Property is owned by Player " + prop.getOwner().getPlayerNumber());
                if (!prop.getOwner().isEqual(player)){
                    if(!prop.isMortgaged()){
                        player.giveMoney(prop.getOwner(), prop.getRent(rollOne, rollTwo), log);
                    }
                }
            }
        }

        //Card
        else if(type == Card.class) {
            //Draw a card
            drawCard((Card) location, player);
        }

        //Cost
        else if(type == Cost.class) {
            //Pay the money
            player.loseMoney(((Cost) location).getCost(), log);
        }

        //Space
        else if(type == Space.class) {
            //Send to jail
            if (location.getName().equals("Go To Jail")) {
                sendToJail(player);
            }
        }
        else {
             throw new UnknownProperty();
        }
    }
    /**
     * checks through all players to see if they are out
     * @return true if the current player is out
     */
    private boolean checkOut(){
        boolean output = false;
        for(int x = 0; x < players.size(); x++){
            if(players.get(x).checkOut()){
                log.log("Player " + players.get(x).getPlayerNumber() + " is in danger of being out!");
                players.get(x).outWarning(log);
                if(players.get(x).checkOut() && players.get(x).isOut()){
                    log.log("Player " + players.get(x).getPlayerNumber() + " is OUT!");
                    if(x == currentTurn){
                        output = true;
                    }
                    if(x <= currentTurn && x != 0){
                        currentTurn--;
                    }

                    playersOut.add(players.get(x));
                    players.remove(x);
                    x--;
                }
            }
        }
        if(output){
            log.log("Current player is out!");
        }
        return output;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Space x:board){
            sb.append(x.getId());
            sb.append(" ");
            sb.append(x.toString());
            if(x.isProperty()){
                sb.append(" ");
                sb.append(((Property)x).getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}