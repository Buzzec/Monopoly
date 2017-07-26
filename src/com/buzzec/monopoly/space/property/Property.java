package com.buzzec.monopoly.space.property;

import com.buzzec.monopoly.player.Player;
import com.buzzec.monopoly.space.Space;
import com.buzzec.monopoly.util.Reference;
import com.buzzec.monopoly.util.logging.Log;

/**
 * Created by bbett on 7/1/2017.
 */
public class Property extends Space {
    private int
            value,
            buildingCost,
            houses,
            blockId,
            blockSize,
            count;
    private int[]
            rents;
    private boolean
            mortgaged,
            monopolized;
    private Player
            owner;

    //Constructors
    public Property(int id, String name, int value, int buildingCost, int[] rents, int blockId, int blockSize){
        super(id, name);
        setValue(value);
        setBuildingCost(buildingCost);
        setHouses(0);
        setRents(rents);
        setMortgaged(false);
        this.blockId = blockId;
        this.blockSize = blockSize;
        monopolized = false;
        count = 1;
    }

    //Getter Methods
    public int getValue() {
        return value;
    }
    public int getBuildingCost() {
        return buildingCost;
    }
    public int getHouses() {
        return houses;
    }
    public int[] getRents() {
        return rents;
    }
    public boolean isMortgaged() {
        return mortgaged;
    }
    public int getBlockId() {
        return blockId;
    }
    public boolean isMonopolized() {
        return monopolized;
    }
    public int getCount() {
        return count;
    }

    @Override
    public Player getOwner() {
        return owner;
    }
    public int getRent(int rollOne, int rollTwo){
        if(houses == 0){
            if(monopolized){
                return rents[0]*2;
            }
            else {
                return rents[0];
            }
        }
        return rents[houses];
    }
    public boolean mortgage(Log log){
        log.log("Attempting to mortgage " + getName());
        if(mortgaged || houses > 0){
            log.log("Mortgaging failed");
            return false;
        }
        log.log("Mortgaging successful");
        mortgaged = true;
        owner.gainMoney(value / Reference.MORTGAGE_DIVISOR, log);
        return true;
    }
    public boolean unmortgage(Log log){
        log.log("Attempting to unmortgage " + getName());
        if(!mortgaged){
            log.log("Unmortgaging failed");
            return false;
        }
        log.log("Unmortgaging successful");
        mortgaged = false;
        owner.loseMoney((int)(value * Reference.MORTGAGE_BUY_BACK_FACTOR), log);
        return true;
    }
    public boolean buildHouse(Log log){
        log.log("Attempting to build house on " + getName());
        if(!monopolized || mortgaged){
            log.log("Building failed");
            return false;
        }
        log.log("Building successful");
        houses++;
        owner.loseMoney(buildingCost, log);
        return true;
    }
    public boolean sellHouse(Log log){
        log.log("Attempting to sell house on " + getName());
        if(houses < 1 || mortgaged){
            log.log("Selling failed");
            return false;
        }
        log.log("Selling successful");
        houses--;
        owner.gainMoney(buildingCost / Reference.BUILDING_SELL_DIVISOR, log);
        return true;
    }

    //Setter Methods
    public void setValue(int value) {
        this.value = value;
    }
    public void setBuildingCost(int buildingCost) {
        this.buildingCost = buildingCost;
    }
    public void setHouses(int houses) {
        this.houses = houses;
    }
    public void setRents(int[] rents) {
        this.rents = rents;
    }
    public void setMortgaged(boolean mortgaged) {
        this.mortgaged = mortgaged;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
        checkMonopolized();
    }
    public void setMonopolized(boolean monopolized) {
        this.monopolized = monopolized;
    }

    private void checkMonopolized(){
        if(owner != null){
            count = 1;
            for (Property x : owner.getProperties()) {
                if (x.getBlockId() == blockId) {
                    count++;
                }
            }
            if (count == blockSize) {
                monopolized = true;
                for(Property x : owner.getProperties()){
                    if(x.getBlockId() == blockId){
                        x.setMonopolized(true);
                    }
                }
            }
            else{
                monopolized = false;
                for(Property x : owner.getProperties()){
                    if(x.getBlockId() == blockId){
                        x.setMonopolized(false);
                    }
                }
            }
        }
    }
}