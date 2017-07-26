package com.buzzec.monopoly.space;

/**
 * Created by bbett on 7/2/2017.
 */
public class Cost extends Space {
    private int
            cost;

    //Constructors
    public Cost(int id, String name, int cost){
        super(id, name);
        setCost(cost);
    }

    //Getters
    public int getCost() {
        return cost;
    }

    //Setters
    private void setCost(int cost) {
        this.cost = cost;
    }
}
