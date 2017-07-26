package com.buzzec.monopoly.runnables;

import com.buzzec.monopoly.Game;
import com.buzzec.monopoly.player.test.TestPlayer;
import com.buzzec.monopoly.util.Reference;
import com.buzzec.monopoly.util.logging.Log;
import com.buzzec.monopoly.player.Player;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Log log = new Log("logs\\test", null, true);
        ArrayList<Player> players = new ArrayList<>();
        for(int x = 0; x < 6; x++){
            players.add(new TestPlayer(x));
        }
        Game game = new Game("boards\\AmericanBoard.txt", players, Reference.STARTING_MONEY, log);
        log.log(game);
        while(game.doTurn());
    }
}