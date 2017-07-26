package com.buzzec.monopoly.player;

import com.buzzec.monopoly.util.logging.Log;
import com.buzzec.monopoly.space.Space;
import com.buzzec.monopoly.space.property.Property;

import java.util.ArrayList;

/**
 * Created by bbett on 7/1/2017.
 */
public abstract class Player {
    private int
            playerNumber,
            money,
            jailTimeLeft,
            location;
    private boolean
            out;
    private ArrayList<Property>
            properties;

    //Getters
    public final int getPlayerNumber() {
        return playerNumber;
    }
    public final int getMoney() {
        return money;
    }
    public final int getJailTimeLeft() {
        return jailTimeLeft;
    }
    public final int getLocation() {
        return location;
    }
    public final boolean isOut() {
        return out;
    }
    public final ArrayList<Property> getProperties() {
        return properties;
    }

    //Setters
    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public void setJailTimeLeft(int jailTimeLeft) {
        this.jailTimeLeft = jailTimeLeft;
    }
    public void setLocation(int location) {
        this.location = location;
    }
    public void setOut(boolean out) {
        this.out = out;
    }
    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    //Money Methods
    public void gainMoney(int value, Log log){
        money += value;
        log.log("Player " + playerNumber + " gains $" + value);
    }
    public void loseMoney(int value, Log log){
        money -= value;
        log.log("Player " + playerNumber + " loses $" + value);
    }
    public void giveMoney(Player other, int value, Log log){
        loseMoney(value, log);
        other.gainMoney(value, log);
    }

    //Other methods
    public final int findProperty(int propId){
        for(int x = 0; x < properties.size(); x++){
            if(properties.get(x).getId() == propId){
                return x;
            }
        }
        return -1;
    }
    public final int findProperty(Property prop){
        return findProperty(prop.getId());
    }
    public final boolean isEqual(Player other){
        return playerNumber == other.getPlayerNumber();
    }
    public final boolean checkOut(){
        if(money <= 0){
            out = true;
            return true;
        }
        return false;
    }
    public final void gainProperty(Property prop, Log log){
        if(prop.getOwner() != null) {
            prop.getOwner().getProperties().remove(prop.getOwner().findProperty(prop));
        }
        properties.add(prop);
        prop.setOwner(this);
        log.log("Player " + playerNumber + " is given " + prop.getName());
    }

    //Events
    public abstract void beforeTurn(Log log);
    public abstract void passGo(Log log);
    public abstract void landOn(Space space, int rollOne, int rollTwo, Log log);
    public abstract boolean leaveJail(Log log);
    public abstract void betweenTurn(Log log);
    public abstract boolean buyOpenProperty(Property prop, Log log);
    public abstract int auction(Property prop, int highBid,  Log log);
    public abstract void outWarning(Log log);

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == this.getClass() && playerNumber == ((Player)obj).playerNumber;
    }
}