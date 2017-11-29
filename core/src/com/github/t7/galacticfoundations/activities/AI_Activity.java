package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Borzantag on 11/25/2017.
 */

public class AI_Activity {
    public static int resources = 0;    //Count of resourcesa
    public static Array<Hex> Tiles;     //Array of Hexes that make up the board

    public static void AI_turn(Array<Hex> a){   //Function that drives AI turn
        Tiles = a;                              //Initialize Tiles Array with Hex values
        List<Integer> tileWeights = new ArrayList<Integer> (); //List of weights for each tile
        int tileLocations[][] = new int[11][9];                 //List of tile IDs
        int count = 1;                                         //Count for making sure each row gets a different weight
        int weight = 1;                                         //Initial weight, updated for each row

        for(int i = 0; i < Tiles.size; i++) {                   //Loop to go through each Hex tile and initialize weight and
            tileLocations[weight-1][count-1] = i;               //Initializing ID of each tile
            if(Tiles.get(i).toString().split(" ")[0].equals("BASE") || Tiles.get(i).toString().split(" ")[0].equals("SPECIAL")){ //if a tile is a base or special tile add 5 to weight
                tileWeights.add((weight+5));                //Add weight of tile to arraylist
            }
            else {                                          //If not special tile or base, initialize normally
                tileWeights.add(weight);                    //Add weight of tile to arrayList
            }

            if (count == 9) {                               //If count is 9, end of row, move to new row and increase weight
                weight += 1;                                //Increase weight for next row
                count = 0;                                  //Reset count
            }

            if (Tiles.get(i).toString().split(" ")[1].equals("AI_ACTIVE")) { //If tile is an active AI tile, add to resources
                resources++;
            }
            count++;                                                        //Increment count
        }
        Collections.reverse(tileWeights);                               //Reverse weights so that they start increasing from AI side of board

        while(resources != 0){                                          //Evaluate board and take action while AI has resources
            eval_board(tileWeights, tileLocations);
        }
    }
    /*
     Evaluates the current game board for best course of action
     Inputs:Game board, turn counter
     Outputs:None
     */
    public static void eval_board(List<Integer> weight, int location[][]){
        List<Integer> AI_Tiles = new ArrayList<Integer>();                   //Array to hold IDs of AI tiles
        for(int i = 0; i < Tiles.size;i++){                                  //Goes through all tiles to find active AI tiles
            if(Tiles.get(i).toString().split(" ")[1].equals("AI_ACTIVE")){     //Checks if tile is active AI tile
               AI_Tiles.add(i);
            }
        }

        int[][] adjacencyMatrix = hexAdjacent();
        /*
        while(AI_Tiles.size() >= 0){

        }
        */
    }

    /*
     Finds the tiles adjacent to a specific tile
     Inputs:Game board
     Outputs:None
     */
    public static void findAdjacent(){
    }

    /*
    Method to make a line attack

     */
    public static void lineAttack(){
    }

    public static void AOE_Attack(){
    }

    public static void fortify(){
    }

    public static void expand(){
    }

    public static void stockpile(){
    }

    /*
        Method to create adjacency matrix for every tile
        Input: None
        Output: 95x95 adjacency matrix
     */
    public static int[][] hexAdjacent(){

            int adjacencyMatrix[][] = new int[95][95];  //Initializing matrix
            for(int i =0; i < 95; i++){                 //Go through every tile
                for(int j =0; j<95; j++){               //Go through every possible tile connected to i
                    if(i == 0){                         //Checks if i is bottom left corner
                        if(j == 9 || j == 2){           //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i == 90){                        //Checks if i is top left corner
                        if(j == 81 || j == 82){         //If yes, adds adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i==8){                           //Checks if i is bottom right corner
                        if(j == 7 || j == 17){          //If yes, creates adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i == 94){                        //Checks if i is top right corner
                        if(j == 88 || j == 89){         //If yes, creates adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!= 0 && i!= 90 && i%9 == 0){            //Checks if i is on left edge
                        if(j == (i+9) || j== (i+1) || j == (i-9) || j == (i-8) ){   //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=8 && i!=94 && i%9 == 8 ){             //Checks if i is on right edge
                        if(j == (i+9) || j == (i-9) || j == (i-1) || j == (i-10)){      //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <= 7 && i>= 1 && i%2 == 0){            //Checks if i is on bottom edge and even
                        if(j == (i-1) || j == (i+1) || j == (i+9)){             //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <=7 && i >=1 && i%2 == 1){             //Checks if i is on bottom edge and odd
                        if(j == (i-1) || j == (i+1) || j == (i+9) || j == (i+10) || j == (j+8)){    //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=0 && i!= 8 && i != 90 && i!= 94 && i > 7 && i%9 != 0 && i%9 != 8 && i%2 == 1) {   //Checks if i is non-edge tile and odd
                        if (j == (i + 9) || j == (i + 8) || j == (i + 10) || j == (i - 9) || j == (i - 1) || j == (i + 1)) {    //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=0 && i!= 8 && i != 90 && i != 94 && i > 7 && i%9 != 0 && i%9 != 8 && i%2 ==0){    //Checks if i is non-edge tile and even
                        if(j == (i-9) || j == (i-8) || j == (i-10) || j == (i-1) || j == (i + 1) || j == (i+9)){    //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }

            }
        }

        return adjacencyMatrix; //Returns the adjacency matrix
    }

    //Just a add points function
    public void addResources(int addResources){
        resources += addResources;
    }
}
