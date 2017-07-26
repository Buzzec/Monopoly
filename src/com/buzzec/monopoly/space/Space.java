package com.buzzec.monopoly.space;

import com.buzzec.monopoly.player.Player;

/**
 * @author Buzzec
 */
public class Space {
    private int
            id;
    private String
            name;

    //Constructors
    public Space(int id, String name){
        setId(id);
        setName(name);
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Player getOwner(){
        return null;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    //Object
    @Override
    public String toString() {
        return name;
    }
}