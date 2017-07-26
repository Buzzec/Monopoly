package com.buzzec.monopoly.runnables;

import com.buzzec.monopoly.Game;
import com.buzzec.monopoly.space.property.Utility;
import com.buzzec.monopoly.util.logging.Log;
import com.buzzec.monopoly.player.Player;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Log log = new Log("logs\\test", null, true);
        ArrayList<Player> players = new ArrayList<>();
        Game game = new Game("boards\\AmericanBoard.txt", players, 4000, log);
        System.out.println(game);
        log.log("YAY");
        log.log("YAY2");
        log.log(Utility.class.toString());
    }
}
