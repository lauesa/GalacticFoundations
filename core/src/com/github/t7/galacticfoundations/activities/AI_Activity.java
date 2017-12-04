package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.activities.GameboardActivity;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.math.Vector2;
import com.github.t7.galacticfoundations.states.HexState;

import static com.github.t7.galacticfoundations.activities.GameboardActivity.*;

/**
 * Created by Borzantag on 11/25/2017.
 */

public class AI_Activity{
    public static int getResources() {
        return resources;
    }

    public static void setResources(int resources) {
        AI_Activity.resources = resources;
    }

    public static int resources = 0;    //Count of resourcesa
    public static ArrayList<Hex> Tiles = new ArrayList<Hex>();     //Array of Hexes that make up the board
    public static Random rand;          //Instantiation of random class
    private static GameboardActivity gameBoard;

    public static void AI_turn(ArrayList<Hex> a,  GameboardActivity g){   //Function that drives AI turn
        Tiles = a;                              //Initialize Tiles Array with Hex values
        gameBoard = g;

                                             //Evaluate board and take action while AI has resources
        eval_board();
        if(resources > 0){

        }
        return;

    }
    /*
     Evaluates the current game board for best course of action
     Inputs:Game board, turn counter
     Outputs:None
     */
    public static void eval_board(){
        ArrayList<Integer> AI_Tiles = new ArrayList<Integer>();                   //Array to hold IDs of AI tiles
        for(int i = 0; i < Tiles.size();i++){                                  //Goes through all tiles to find active AI tiles
            if(Tiles.get(i).toString().split(" ")[1].equals("AI_ACTIVE")){     //Checks if tile is active AI tile
               AI_Tiles.add(i);
            }
        }

        Array<Array<Hex>> adjacencyMatrix = hexAdjacent();                            //Gets adjacency matrix from function
        ArrayList<Hex> Min_ID = new ArrayList<Hex>();                   //ArrayList to hold all forward moving Tiles adjacent to a tile
        while(AI_Tiles.size() > 0 && resources != 0){                                        //Loop will continue until out of tiles to use or out of resources

            //while(Min_ID.size() < 3){                                      //Loop that will continue to run until forwardmost half of adjacent tiles are added to ArrayList
                //for(int i = 0; i < adjacencyMatrix.get(AI_Tiles.get(0)).size; i++){
                    float sourceX = Tiles.get(AI_Tiles.get(0)).getOriginX();
                    float sourceY = Tiles.get(AI_Tiles.get(0)).getOriginY();
                    Vector2 stageLocal = new Vector2(sourceX, sourceY);
                    Vector2 stageCoords = Tiles.get(AI_Tiles.get(0)).localToStageCoordinates(stageLocal);
                    for(int i = 30; i < 390; i+=60){
                        double radsI = Math.toRadians(i);
                        Vector2 hitCoords = new Vector2((float)(stageCoords.x+(GameboardActivity.TILE_WIDTH*Math.cos(radsI))), (float)(stageCoords.y+(GameboardActivity.TILE_WIDTH*Math.sin(radsI))));
                        Actor target = gameBoard.getStage().hit(hitCoords.x, hitCoords.y, false);
                        if(target != null){
                            if(((Hex)target).getHexType()== Hex.HexType.SPECIAL && ((Hex)target).toString().split(" ")[1].equals("UNOWNED")){
                                Min_ID.clear();
                                Min_ID.add((Hex) target);
                                break;
                            }
                        }
                        if(target != null && i >= 150 && i <= 270) {
                            if (target.getName().equals("Hex")) {
                                Min_ID.add((Hex) target);
                            }
                        }
                    }
            //}

            int enemyCount = 0;                                              //Count variable to count enemy tiles in forward positions
            int  friendCount = 0;
            ArrayList<Hex> EnemyTiles = new ArrayList<Hex>();   //ArrayList to hold IDs of enemy tiles
            if(Min_ID.size() > 1) {
                for (int i = 0; i < Min_ID.size(); i++) {                     //Loop to go through forward tiles and search for enemy tiles
                    if (Min_ID.get(i).toString().split(" ")[1].equals("PLAYER_INACTIVE")) {  //Checks if a tile belongs to a player
                        enemyCount++;                                                                        //If yes, increments count
                        EnemyTiles.add(Min_ID.get(i));                                                  //Also adds the enemy tile's ID to ArrayList
                    }
                    if (Min_ID.get(i).toString().split(" ")[1].equals("AI_ACTIVE")) {
                        friendCount++;
                        Min_ID.remove(Min_ID.get(i));
                    }
                }
            }

            if(resources <= 0){
                return;
            }
            /*if(enemyCount >= 2 && resources >= 5){                                                         //If count of enemy tiles greater than 2, make AoE attack
                AOE_Attack(Tiles.get(AI_Tiles.get(0)), Adj_Tiles, Min_ID);            //Passes tile attack origin and adjacency matrix to AoE attack function
                resources-=5;
            }*/
            else if(Tiles.get(AI_Tiles.get(0)).getHexType() == Hex.HexType.SPECIAL && resources >= 2){
                Tiles.get(AI_Tiles.get(0)).setFortify(true);
                resources -=2;
            }
            else if(enemyCount >= 1 && resources >= 5){                                                         //If count of enemy tiles is 1, make line attack
                Hex target = randomTile(EnemyTiles);
                gameBoard.rayAttack(Tiles.get(AI_Tiles.get(0)), target);     //Passes tile attack origin and target
                resources-=5;
            }
            else if(enemyCount == 0 && resources >= 1 && friendCount < 3 && Min_ID.size() > 0){                                                     //If no enemy tiles, expand
                if(Min_ID.size() == 1 && Min_ID.get(0).getHexType() == Hex.HexType.SPECIAL && resources >= 5){
                    expand(Min_ID.get(0), Tiles.get(AI_Tiles.get(0)));
                    resources -= 5;
                }
                else {
                    Hex randTile = randomTile(Min_ID);                              //Gets a random tile from forward most tiles to expand to
                    expand(randTile, Tiles.get(AI_Tiles.get(0)));                                    //Expands to target tile
                    resources -= 1;
                    AI_Tiles.add(Tiles.indexOf(randTile));
                }
            }
            Min_ID.clear();
            AI_Tiles.remove(0);                                             //Removes tile just used from ArrayList
        }
        return;

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

    public static void expand(Hex target, Hex source){
        target.setState((HexState.AI_ACTIVE));
        target.setState((HexState.AI_INACTIVE));
    }

    public static void stockpile(){

    }

    /*
        Function to take in a list of tiles and return a random tile ID
        Input: ArrayList of Ints corresponding to tile IDs
        Output: Random integer corresponding to a tile ID
     */
    public static Hex randomTile(ArrayList<Hex> ActionTiles){
         rand = new Random();
         Hex randTile = ActionTiles.get(rand.nextInt(ActionTiles.size()));
         return randTile;
    }

    /*
        Method to create adjacency matrix for every tile
        Input: None
        Output: 95x95 adjacency matrix
     */
    public static Array<Array<Hex>> hexAdjacent(){
        Array<Array<Hex>> adjacencyMatrix = new Array<Array<Hex>>();
        for(int i = 0; i < Tiles.size(); i++){
            adjacencyMatrix.add(gameBoard.adjacentHexes(Tiles.get(i)));
        }
        return adjacencyMatrix; //Returns the adjacency matrix
    }

    //Just a add points function
    public void addResources(int addResources){
        resources += addResources;
    }
}
