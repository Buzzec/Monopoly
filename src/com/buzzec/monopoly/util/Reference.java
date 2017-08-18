package com.buzzec.monopoly.util;

import com.buzzec.monopoly.util.logging.Log;

/**
 * Created by bbett on 7/17/2017.
 */
public class Reference {
    public static final int
            DOUBLES_TO_JAIL = 3,
            JAIL_COST = 50,
            TURNS_IN_JAIL = 3,
            SIDES_ON_DIE = 6,
            MORTGAGE_DIVISOR = 2,
            BUILDING_SELL_DIVISOR = 2,
            STARTING_MONEY = 4000,
            GO_MONEY = 200;
    public static final double
            MORTGAGE_BUY_BACK_FACTOR = 1.1;
    public static final Log
            MAIN_LOG = new Log("logs\\main", "mainLog", false);
}
