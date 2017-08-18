package com.buzzec.monopoly.util;

import com.buzzec.monopoly.player.Player;
import com.buzzec.monopoly.space.property.Property;
import com.buzzec.monopoly.util.logging.Log;

public class Helper {
    public static int rollDie(Log log){
        int roll = (int)(Math.random() * Reference.SIDES_ON_DIE) + 1;
        log.log("Rolled: " + roll);
        return roll;
    }

    public static void buyProperty(Property prop, Player player, Log log){
        player.loseMoney(prop.getValue(), log);
        log.log("Player " + player.getPlayerNumber() + " buys " + prop.getName());
        player.gainProperty(prop, log);
    }
}
