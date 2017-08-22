package com.buzzec.monopoly.util;

import com.buzzec.monopoly.player.Player;
import com.buzzec.monopoly.space.DrawCard;
import com.buzzec.monopoly.space.Cost;
import com.buzzec.monopoly.space.Space;
import com.buzzec.monopoly.space.property.Property;
import com.buzzec.monopoly.space.property.Railroad;
import com.buzzec.monopoly.space.property.Utility;
import com.buzzec.monopoly.util.logging.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

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

    //TODO add logging to generateBoard()
    public static ArrayList<Space> generateBoard(String fileName, Log log){
        String line;
        ArrayList<Space> board = new ArrayList<>();
        try{
            //Create buffered reader to read file specified
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            //Finds split character defined in file
            String split = br.readLine();
            //While stuff left to read store that stuff in line
            while((line = br.readLine()) != null){
                //Split the line
                String[] splitLine = line.split(split);
                //Print board for testing
                /*
                for(String x:splitLine){
                    System.out.print(x + "_");
                }
                System.out.println();
                */
                //Analyse line and place in board
                switch(splitLine.length) {
                    //Property
                    //ex: 1/Mediterranean Avenue/60/50/2/10/30/90/160/250/0/2
                    case 12:
                        int[] rent = new int[6];
                        for (int x = 0; x < rent.length; x++) {
                            rent[x] = Integer.parseInt(splitLine[x + 4]);
                        }
                        board.add(new Property(Integer.parseInt(splitLine[0]),
                                splitLine[1],
                                Integer.parseInt(splitLine[2]),
                                Integer.parseInt(splitLine[3]),
                                rent,
                                Integer.parseInt(splitLine[10]),
                                Integer.parseInt(splitLine[11])));
                        break;
                    //Space
                    //ex: 0/GO
                    case 2:
                        board.add(new Space(Integer.parseInt(splitLine[0]),
                                splitLine[1]));
                        break;
                    //DrawCard or Cost
                    case 3:
                        switch(splitLine[1]){
                            //DrawCard
                            //ex: 2/DrawCard/Community Chest
                            case "DrawCard":
                                board.add(new DrawCard(Integer.parseInt(splitLine[0]),
                                        splitLine[1],
                                        splitLine[2]));
                                break;
                            //Cost
                            //ex: 4/Cost/200
                            case "Cost":
                                board.add(new Cost(Integer.parseInt(splitLine[0]),
                                        splitLine[1],
                                        Integer.parseInt(splitLine[2])));
                                break;
                            //Default
                            default:
                                System.out.println("Unknown Space: ");
                                for(String x : splitLine){
                                    System.out.println(x);
                                }
                        }
                        break;
                    //Railroad
                    //ex: 5/Reading Railroad/200/25/50/100/200
                    case 7:
                        rent = new int[4];
                        for (int x = 0; x < rent.length; x++) {
                            rent[x] = Integer.parseInt(splitLine[x + 3]);
                        }
                        board.add(new Railroad(Integer.parseInt(splitLine[0]),
                                splitLine[1],
                                Integer.parseInt(splitLine[2]),
                                rent));
                        break;

                    //Helper
                    //ex: 12/Electric Company/150/4/10
                    case 5:
                        rent = new int[2];
                        for (int x = 0; x < rent.length; x++) {
                            rent[x] = Integer.parseInt(splitLine[x + 3]);
                        }
                        board.add(new Utility(Integer.parseInt(splitLine[0]),
                                splitLine[1],
                                Integer.parseInt(splitLine[2]),
                                rent));
                        break;
                    //Default
                    default:
                        System.out.println("Unknown Space: ");
                        for(String x : splitLine){
                            System.out.println(x);
                        }
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return board;
    }
}
