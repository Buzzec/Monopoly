package com.buzzec.monopoly.space;

/**
 * Created by bbett on 7/2/2017.
 */
public class DrawCard extends Space {
    //TODO Add list of cards
    private String
            identifier;

    //Constructors
    public DrawCard(int id, String name, String identifier){
        super(id, name);
        setIdentifier(identifier);
    }

    //Getters
    public String getIdentifier() {
        return identifier;
    }

    //Setters
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
