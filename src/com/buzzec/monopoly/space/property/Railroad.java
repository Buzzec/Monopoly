package com.buzzec.monopoly.space.property;

/**
 * Created by bbett on 7/2/2017.
 */
public class Railroad extends Property {
    //Constructors
    public Railroad(int id, String name, int value, int[] rents){
        super(id, name, value, -1, rents, -1, 4);
    }

    @Override
    public int getHouses() {
        return -1;
    }

    @Override
    public int getRent(int rollOne, int rollTwo) {
        return getRents()[getCount() - 1];
    }
}
