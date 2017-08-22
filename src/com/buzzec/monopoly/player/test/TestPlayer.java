package com.buzzec.monopoly.player.test;

import com.buzzec.monopoly.player.Player;
import com.buzzec.monopoly.space.Space;
import com.buzzec.monopoly.space.property.Property;
import com.buzzec.monopoly.util.logging.Log;

import java.util.ArrayList;

/**
 * Created by bbett on 7/25/2017.
 */
public class TestPlayer extends Player {
    public TestPlayer(int playerNumber){
        setPlayerNumber(playerNumber);
        setMoney(0);
        setJailTimeLeft(0);
        setLocation(0);
        setOut(false);
        setProperties(new ArrayList<>());
    }
    @Override
    public void beforeTurn(Log log) {
        log.log("Player " + getPlayerNumber() + " beforeTurn()");
    }

    @Override
    public void passGo(Log log) {
        log.log("Player " + getPlayerNumber() + " passGo()");
    }

    @Override
    public void landOn(Space space, int rollOne, int rollTwo, Log log) {
        log.log("Player " + getPlayerNumber() + " landOn()");
    }

    @Override
    public boolean leaveJail(Log log) {
        log.log("Player " + getPlayerNumber() + " leaveJail()");
        return false;
    }

    @Override
    public void betweenTurn(Log log) {
        log.log("Player " + getPlayerNumber() + " betweenTurn()");
        Property temp = findLowestMortgaged();
        if(temp != null && temp.isMortgaged()){
            if (getMoney() * 2 > temp.getValue()) {
                temp.unmortgage(log);
            }
        }
    }

    @Override
    public boolean buyOpenProperty(Property prop, Log log) {
        log.log("Player " + getPlayerNumber() + " buyOpenProperty()");
        return prop.getValue() <= getMoney();
    }

    @Override
    public int auction(Property prop, int highBid, Log log) {
        log.log("Player " + getPlayerNumber() + " auction()");
        int bid = highBid + (int)(Math.random() * 10) + 1;
        if(highBid <= prop.getValue() && bid < getMoney()){
            return bid;
        }
        return -1;
    }

    @Override
    public void outWarning(Log log) {
        log.log("Player " + getPlayerNumber() + " outWarning()");
        while(getMoney() < 0){
            Property temp = findLowestUnmortgaged();
            if(temp != null) {
                if(!temp.mortgage(log)){
                    break;
                }
            }
        }
    }

    private Property findLowestUnmortgaged(){
        if(getProperties().size() > 0) {
            Property output = getProperties().get(0);
            for(Property x : getProperties()){
                if((output.isMortgaged() || x.getValue() < output.getValue()) && !x.isMortgaged()){
                    output = x;
                }
            }
            return output;
        }
        else {
            return null;
        }
    }

    private Property findLowestMortgaged(){
        if(getProperties().size() > 0) {
            Property output = getProperties().get(0);
            for(Property x : getProperties()){
                if((!output.isMortgaged() || x.getValue() < output.getValue()) && x.isMortgaged()){
                    output = x;
                }
            }
            return output;
        }
        else {
            return null;
        }
    }
}
