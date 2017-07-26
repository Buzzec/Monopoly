package com.buzzec.monopoly.space.property;

/**
 * Created by bbett on 7/2/2017.
 */
public class Utility extends Property {
    //Constructors
    public Utility(int id, String name, int value, int[] rents){
        super(id, name, value, -1, rents, -2, 2);
    }

    @Override
    public int getHouses() {
        return -1;
    }

    @Override
    public int getRent(int rollOne, int rollTwo) {
        if(isMonopolized()){
            return (rollOne + rollTwo) * getRents()[1];
        }
        return (rollOne + rollTwo) * getRents()[0];
    }
}
