package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.activities.GameboardActivity;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import static com.github.t7.galacticfoundations.activities.GameboardActivity.*;

/**
 * Created by Borzantag on 11/25/2017.
 */

public class AI_Activity {
    public static int resources = 0;    //Count of resourcesa
    public static Array<Hex> Tiles;     //Array of Hexes that make up the board
    public static Random rand;          //Instantiation of random class

    public static void AI_turn(Array<Hex> a){   //Function that drives AI turn
        Tiles = a;                              //Initialize Tiles Array with Hex values
        List<Integer> tileWeights = new ArrayList<Integer> (); //List of weights for each tile
        int tileLocations[][] = new int[11][9];                 //List of tile IDs
        int count = 1;                                         //Count for making sure each row gets a different weight
        int weight = 1;                                         //Initial weight, updated for each row
        /*
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
        */
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

        int[][] adjacencyMatrix = hexAdjacent();                            //Gets adjacency matrix from function
        ArrayList<Integer> Adj_Tiles = new ArrayList<Integer>();                 //ArrayList to hold all Tiles adjacent to a tile
        ArrayList<Integer> Max_ID = new ArrayList<Integer>();                   //ArrayList to hold all forward moving Tiles adjacent to a tile
        int max = 0;                                                        //Max variable for use in determining forward-most tiles
        while(AI_Tiles.size() >= 0 || resources != 0){                                        //Loop will continue until out of tiles to use or out of resources
            for(int i = 0; i < 95; i++){                                                      //Loop to go through and find adjacent tiles to first tile in AI_Tiles
                if(adjacencyMatrix[AI_Tiles.get(0)][i] == 1){                                  //Checks if an ID for a tile is adjacent
                    Adj_Tiles.add(adjacencyMatrix[AI_Tiles.get(0)][1]);                        //If so, add tile to Adjacency ArrayList
                }
            }

            while(Max_ID.size() < 3){                                      //Loop that will continue to run until forwardmost half of adjacent tiles are added to ArrayList
                for(int i = 0; i < Adj_Tiles.size(); i++){                  //Loop to go through Adjacent Tiles and find the max ID
                    if(Adj_Tiles.get(i) > max){                             //Checks if current tile has a greater ID than the max ID
                        max = Adj_Tiles.get(i);                             //If so, sets current max to current tile's ID
                    }
                }
                Max_ID.add(max);                                        //Adds max value to array of Max IDs
                Adj_Tiles.remove(Adj_Tiles.indexOf(max));               //Removes max value from Array so that secondary or tertiary max ID can be found
                max = 0;                                                //Sets max back to zero
            }

            int count = 0;                                              //Count variable to count enemy tiles in forward positions
            ArrayList<Integer> EnemyTiles = new ArrayList<Integer>();   //ArrayList to hold IDs of enemy tiles
            for(int i = 0; i < Max_ID.size(); i++){                     //Loop to go through forward tiles and search for enemy tiles
                if(Tiles.get(Max_ID.get(i)).toString().split(" ")[1].equals("PLAYER_ACTIVE")){  //Checks if a tile belongs to a player
                    count++;                                                                        //If yes, increments count
                    EnemyTiles.add(Max_ID.get(i));                                                  //Also adds the enemy tile's ID to ArrayList
                }
            }

            if(count >= 2){                                                         //If count of enemy tiles greater than 2, make AoE attack
                AOE_Attack(Tiles.get(AI_Tiles.get(0)), Adj_Tiles, Max_ID);            //Passes tile attack origin and adjacency matrix to AoE attack function
            }
            if(count == 1){                                                         //If count of enemy tiles is 1, make line attack
                lineAttack(Tiles.get(AI_Tiles.get(0)), Tiles.get(EnemyTiles.get(0)));     //Passes tile attack origin and target
            }
            if(count == 0){                                                     //If no enemy tiles, expand
                int randTile = randomTile(Max_ID);                              //Gets a random tile from forward most tiles to expand to
                expand(Tiles.get(randTile));                                    //Expands to target tile
            }
            AI_Tiles.remove(0);                                             //Removes tile just used from ArrayList
        }

    }

    public void aiFE() {


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
        Input:Hex tile to originate attack from and Hex tile to target attack towards
        Output:None
     */
    public static void lineAttack(Hex origin, Hex target){
    }

    /*
        Method to make an AoE attack
        Input: Hex tile that is origin of AoE attack and adjacentTiles from two different ArrayLists
        Output: none
     */
    public static void AOE_Attack(Hex hex, ArrayList<Integer> adjacentTiles, ArrayList<Integer> Max){

    }

    public static void fortify(){
    }

    public static void expand(Hex target){

    }

    public static void stockpile(){
    }

    /*
        Function to take in a list of tiles and return a random tile ID
        Input: ArrayList of Ints corresponding to tile IDs
        Output: Random integer corresponding to a tile ID
     */
    public static int randomTile(ArrayList<Integer> ActionTiles){
         rand = new Random();
         int randTile = ActionTiles.get(rand.nextInt(ActionTiles.size()));
         return randTile;
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
                        if(j == 5 || j == 9){           //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i == 90){                        //Checks if i is top left corner
                        if(j == 81 || j == 86){         //If yes, adds adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i==4){                           //Checks if i is bottom right corner
                        if(j == 8 || j == 13){          //If yes, creates adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i == 94){                        //Checks if i is top right corner
                        if(j == 85 || j == 89){         //If yes, creates adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else {
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!= 0 && i!= 90 && i%9 == 0){            //Checks if i is on left edge
                        if(j == (i+9) || j== (i+5) || j == (i-4) || j == (i-9) ){   //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=8 && i!=94 && i%9 == 8 ){             //Checks if i is on right edge
                        if(j == (i+9) || j == (i-9) || j == (i-5) || j == (i+4)){      //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <= 7 && i>= 1 && i%2 == 0){            //Checks if i is on bottom edge and even
                        if(j == (i+4) || j == (i+5) || j == (i+9)){             //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <=7 && i >=1 && i%2 == 1){             //Checks if i is on bottom edge and odd
                        if(j == (i-4) || j == (i-5) || j == (i+9) || j == (i+4) || j == (j+5)){    //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <= 94 && i >= 90 && i!= 90 && i!= 94){ //Checks if i is top outermost edge
                        if(j == (i-9) || j == (i - 4) || j == (i-5)){ //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i <= 89 && i >= 86){                         //Checks if i is top innermost edge
                        if(j == (i-9) || j == (i+4) || j == (i+5)|| j == (i-4) || j== (i-5)){   //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=0 && i!= 8 && i != 90 && i!= 94 && i > 7 && i%9 != 0 && i%9 != 8 && i < 86 && i%2 == 1) {   //Checks if i is non-edge tile and odd
                        if (j == (i + 9) || j == (i + 4) || j == (i + 5) || j == (i - 9) || j == (i - 4) || j == (i - 5)) {    //If yes, add adjacency
                            adjacencyMatrix[i][j] = 1;
                        }
                        else{
                            adjacencyMatrix[i][j] = 0;
                        }
                    }
                    if(i!=0 && i!= 8 && i != 90 && i != 94 && i > 7 && i%9 != 0 && i%9 != 8 && i < 86 && i%2 ==0){    //Checks if i is non-edge tile and even
                        if(j == (i-9) || j == (i-4) || j == (i-5) || j == (i+4) || j == (i + 5) || j == (i+9)){    //If yes, add adjacency
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
