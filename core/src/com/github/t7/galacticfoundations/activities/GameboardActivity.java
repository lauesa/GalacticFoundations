package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.galacticfoundations;
import com.github.t7.galacticfoundations.hud.GameboardHUD;
import com.github.t7.galacticfoundations.states.GameState;
import com.github.t7.galacticfoundations.states.HexState;

import java.util.Arrays;

import static com.github.t7.galacticfoundations.actors.Hex.HexType.BASE;
import static com.github.t7.galacticfoundations.actors.Hex.HexType.GENERAL;
import static com.github.t7.galacticfoundations.actors.Hex.HexType.SPECIAL;

/**
 * Created by Warren on 11/19/2017.
 */

public class GameboardActivity extends Activity {
    public static final int TILE_WIDTH = 65;
    public static final int TILE_HEIGHT = 55;
    private Texture bg;
    private float zoomScale;
    private Stage stage;
    private GestureDetector gestureDetector;
    private InputListener inputListener;
    private GameboardHUD gameboardHUD;
    private InputMultiplexer multiplexer;
    private StateMachine<GameboardActivity, GameState> stateMachine;
    private Hex focus;
    private Array<Hex> focusAdjacents;
    public enum BoardMode{
        DEFAULT,
        ATTACK,
        DEFEND,
        EXPAND;
    };
    private BoardMode boardMode;



    public GameboardActivity(ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();
        //bg = new Texture("gameboard_bg.png");
        bg = new Texture("marswide.jpg");
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        zoomScale = 1f;
        focus = null;
        focusAdjacents = new Array<Hex>();


        stateMachine = new DefaultStateMachine<GameboardActivity, GameState>(this, GameState.PLAYER_TURN);

        stage = new Stage(viewport);


        //Initiate gameboardHUD
        gameboardHUD = new GameboardHUD(galacticfoundations.batch, this);

        setBoardMode(BoardMode.DEFAULT);




        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter() {
            private Vector2 oldInitialFirstPointer = null, oldInitialSecondPointer = null;
            private float oldScale;

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {

                cam.update();
                //System.out.printf("Cam X: %f, Cam Y: %f\n", cam.position.x, cam.position.y);

                if ((cam.position.x > 0 && cam.position.x < 500) && (cam.position.y > 300 && cam.position.y < 500)) {
                    cam.position.add(
                            cam.unproject(new Vector3(0, 0, 0))
                                    .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                    );
                } else {
                    if (cam.position.x <= 0) {


                        if (deltaX <= 0 && (cam.position.y > 300 && cam.position.y < 500)) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.y > 300 && cam.position.y < 500) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }

                    }
                    if (cam.position.x >= 500) {

                        if (deltaX >= 0 && (cam.position.y > 300 && cam.position.y < 500)) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.y > 300 && cam.position.y < 500) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if (cam.position.y <= 300) {

                        if (deltaY >= 0) {//&& (cam.position.x > 0 && cam.position.x < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.x > 0 && cam.position.x < 500) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                    if (cam.position.y >= 500) {

                        if (deltaY <= 0) {// && (cam.position.x > 0 && cam.position.x < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.x > 0 && cam.position.x < 500) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                }

                stage.cancelTouchFocus();
                return true;
            }

            @Override
            public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
                if (!(initialFirstPointer.equals(oldInitialFirstPointer) && initialSecondPointer.equals(oldInitialSecondPointer))) {
                    oldInitialFirstPointer = initialFirstPointer.cpy();
                    oldInitialSecondPointer = initialSecondPointer.cpy();
                    oldScale = cam.zoom;
                }
                Vector3 center = new Vector3(
                        (firstPointer.x + initialSecondPointer.x) / 2,
                        (firstPointer.y + initialSecondPointer.y) / 2,
                        0
                );
                zoomCamera(center, oldScale * initialFirstPointer.dst(initialSecondPointer) / firstPointer.dst(secondPointer));
                stage.cancelTouchFocus();
                return true;
            }


            private void zoomCamera(Vector3 origin, float scale) {
                cam.update();
                Vector3 oldUnprojection = cam.unproject(origin.cpy()).cpy();
                cam.zoom = scale; //Larger value of zoom = small images, border view
                cam.zoom = Math.min(2.0f, Math.max(cam.zoom, 0.5f));
                cam.update();
                Vector3 newUnprojection = cam.unproject(origin.cpy()).cpy();
                cam.position.add(oldUnprojection.cpy().add(newUnprojection.cpy().scl(-1f)));
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                Vector2 currentCoords = new Vector2(x, y);
                Vector2 stageCoords = stage.screenToStageCoordinates(currentCoords);
                Actor target = stage.hit(stageCoords.x, stageCoords.y, true);
                if(target != null){
                    System.out.printf("Tap Target: %s\n", target.getName());
                    handleTap(target);
                }

                //handleTap(target);
//                System.out.printf("%s\n", target.getName());
//                if(target.getName() == "Hex"){
//                    focus = (Hex)target;
//                }
//                System.out.printf("%s\n", focus.getName());
                return true;
            }
        });


        multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(gameboardHUD.getStage());
        multiplexer.addProcessor(gestureDetector);
        //multiplexer.addProcessor(stage);


        if(MainActivity.RESUME == 1){
            loadGameState();
            MainActivity.RESUME = 0;
        } else {
            generateBoard();
        }



        
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {




    }
    //overide getactors
    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, -350, -100);
        sb.end();
        stage.act();
        stage.draw();

        galacticfoundations.batch.setProjectionMatrix(gameboardHUD.stage.getCamera().combined);
        gameboardHUD.stage.draw();

        // Goes to main menu when back button pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            galacticfoundations.backpressed=true;
            activityManager.set(new MainActivity(activityManager)); // go to main menu if back button pressed
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

    }

    /*
    * loadGameState()
    *
    * This function to be used on resume game. It will read from the gamestate.txt file and
    * generate the board from that data.
    *
    * Create 3d array of Strings[i][j][k], and recreate generateBoard utilizing the kth elements as
    * Hextype and Hexstate
    * */

    public void loadGameState(){
        System.out.printf("loadGameState Called\n");
        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();

        if(isLocAvailable == true){
            String locRoot = Gdx.files.getLocalStoragePath();
            System.out.printf("%s", locRoot);
        } else {
            System.out.printf("No Local Path");
            return;
        }

        int i = 0;
        int j = 0;
        FileHandle file = Gdx.files.local("gamestate.txt");

        HexState state;
        Hex.HexType type;

        String text = file.readString();
        String[] data = text.split("\\s+");


        //Create 3d array to store items
        String[][][] master = new String[21][5][3];
        int k = 0;
        for(i = 0; i < 21; i++){
            if(i % 2 == 0) {
                for (j = 0; j < 5; j++){
                    master[i][j][0] = data[k];
                    k++;
                    master[i][j][1] = data[k];
                    System.out.printf("%s %s %d \n", master[i][j][0], master[i][j][1], k);
                    k++;
                }
            } else {
                for (j = 0; j < 4; j++) {
                    master[i][j][0] = data[k];
                    k++;
                    master[i][j][1] = data[k];
                    System.out.printf("\n%s %s %d", master[i][j][0], master[i][j][1], k);
                    k++;
                }
            }
        }
        //copied code from generateBoard, altered to build from existing state and type data
        float xOffset = 19.5f;
        float yOffset = 120;
        int boardWidth = 5;
        int boardHeight = 21;
        for(i = 0; i < boardHeight; i++){

            if(i % 2 == 0) {

                for (j = 0; j < boardWidth; j++) {

                    float x = (float)1.47*TILE_WIDTH*j + xOffset;
                    float y = (float)TILE_HEIGHT*(i/2) + yOffset;
                    if(master[i][j][0].equals("GENERAL")){
                        type = GENERAL;
                    } else if(master[i][j][0].equals("SPECIAL")){
                        type = SPECIAL;
                    } else {
                        type = BASE;
                    }

                    if(master[i][j][1].equals("UNOWNED")){
                        state = HexState.UNOWNED;
                    } else if(master[i][j][1].equals("PLAYER_ACTIVE")){
                        state = HexState.PLAYER_ACTIVE;
                    } else if(master[i][j][1].equals("PLAYER_INACTIVE")){
                        state = HexState.PLAYER_INACTIVE;
                    } else if(master[i][j][1].equals("AI_ACTIVE")){
                        state = HexState.AI_ACTIVE;
                    } else {
                        state = HexState.AI_INACTIVE;
                    }

                    Hex newHex = new Hex(type, x, y);
                    newHex.setState(state);
                    stage.addActor(newHex);

                }
            }else{
                float oddXOffset = xOffset + TILE_WIDTH*0.74f;
                float oddYOffset = yOffset + TILE_HEIGHT*0.49f;

                //this for loop generates all actors and adds them to the board
                for(j = 0; j < (boardWidth-1); j++){
                    float x = (float)(1.47*TILE_WIDTH*j) + oddXOffset;
                    float y = (float)(TILE_HEIGHT*(i/2)) + oddYOffset;
                    if(master[i][j][0].equals("GENERAL")){
                        type = GENERAL;
                    } else if(master[i][j][0].equals("SPECIAL")){
                        type = SPECIAL;
                    } else {
                        type = BASE;
                    }

                    if(master[i][j][1].equals("UNOWNED")){
                        state = HexState.UNOWNED;
                    } else if(master[i][j][1].equals("PLAYER_ACTIVE")){
                        state = HexState.PLAYER_ACTIVE;
                    } else if(master[i][j][1].equals("PLAYER_INACTIVE")){
                        state = HexState.PLAYER_INACTIVE;
                    } else if(master[i][j][1].equals("AI_ACTIVE")){
                        state = HexState.AI_ACTIVE;
                    } else {
                        state = HexState.AI_INACTIVE;
                    }

                    Hex newHex = new Hex(type, x, y);
                    newHex.setState(state);
                    stage.addActor(newHex);

                }
            }

        }
    }

    public void saveGameState(){

        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();

        if(isLocAvailable == true){
            String locRoot = Gdx.files.getLocalStoragePath();
            System.out.printf("%s", locRoot);
        } else {
            System.out.printf("No Local Path");
            return;
        }

        FileHandle file = Gdx.files.local("gamestate.txt");
        Gdx.files.local("gamestate.txt").delete();
        Array<Actor> listActors = stage.getActors();

        for (Actor hex: listActors) {
            file.writeString(hex.toString(), true);
        }
    }

    public void generateGameState(){

        Array<Actor> listActors = stage.getActors();
        int id = 0;
        for (Actor hex: listActors) {
            System.out.printf("%d:%s.\n",id, hex.toString());
            id++;
        }
    }

    public void generateBoard(){
        //stage.getActors().clear();
        float xOffset = 19.5f;
        float yOffset = 120;
        int boardWidth = 5;
        int boardHeight = 21;
        for(int i = 0; i < boardHeight; i++){

            if(i % 2 == 0) {

                for (int j = 0; j < boardWidth; j++) {

                    float x = (float)1.47*TILE_WIDTH*j + xOffset;
                    float y = (float)TILE_HEIGHT*(i/2) + yOffset;
                    Hex newHex = new Hex(GENERAL, x, y);
                    stage.addActor(newHex);

                }
            }else{
                float oddXOffset = xOffset + TILE_WIDTH*0.74f;
                float oddYOffset = yOffset + TILE_HEIGHT*0.49f;

                //this for loop generates all actors and adds them to the board
                for(int j = 0; j < (boardWidth-1); j++){
                    float x = (float)(1.47*TILE_WIDTH*j) + oddXOffset;
                    float y = (float)(TILE_HEIGHT*(i/2)) + oddYOffset;
                    stage.addActor(new Hex(GENERAL, x, y));

                }
            }

        }
        placeTiles();




    }

    //Check if a click is within a hex
    public boolean checkBounds(float originX, float originY, float x, float y){
        double radius = TILE_WIDTH/2;
        double deltaX = Math.abs(x - originX);
        double deltaY = Math.abs(y - originY);
        //Find distance from origin
        double distance = Math.sqrt((Math.pow(deltaX,2)+Math.pow(deltaY,2)));

        //Determine the distance within bounds
        double angleRads = Math.atan2(deltaY, deltaX);


        double angleDegrees = Math.toDegrees(angleRads) % 60;

        double boundary = (Math.sqrt(3)*radius) / (Math.sqrt(3)*(Math.cos(Math.toRadians(angleDegrees)) + Math.sin(Math.toRadians(angleDegrees))));;
        //System.out.printf("Degrees %f, Distance: %f, Boundary: %f, radius: %f\n", angleDegrees, distance, boundary, radius);
        if(distance <= boundary){return true;}
        else{
            return false;
        }

    }

    private void handleTap(Actor target){
        Hex hexTarget;
        if(target.getName().equals("Hex")){
            hexTarget = (Hex)target;
            //If you click a new tile
            if(focus != hexTarget){
                //if board is default and player selects another of their own tiles, change focus
                if(boardMode == BoardMode.DEFAULT){
                    if(hexTarget.getState() == HexState.PLAYER_ACTIVE) {
                        setFocus(hexTarget);
                        gameboardHUD.hideTileHUD(false);
                    }
                }
                else if(boardMode == BoardMode.ATTACK){
                    for(Hex current:focusAdjacents){
                        if(((hexTarget.getState() == HexState.AI_ACTIVE) || (hexTarget.getState() == HexState.AI_INACTIVE)) && hexTarget == current){
                            //PUT POINTS DEDUCTION HERE
                            //IMPLEMENT FORITFY
                            hexTarget.setState(HexState.UNOWNED);
                            focus.setState(HexState.PLAYER_INACTIVE);
                        }
                    }
                }
                else if(boardMode == BoardMode.EXPAND){
                    for(int i = 0; i < focusAdjacents.size; i++){
                        Hex current = focusAdjacents.get(i);
                        if((hexTarget.getState() == HexState.UNOWNED) && (hexTarget == current)){
                            hexTarget.setState(HexState.PLAYER_ACTIVE);
                            focus.setState(HexState.PLAYER_INACTIVE);
                            setBoardMode(BoardMode.DEFAULT);
                            unhightlightTiles();
                        }
                    }
                }
                else if(boardMode == BoardMode.DEFEND){
                    //IMPLEMENT POINT DEDUCTION

                }



            //If you click the focused tile again, remove focus
            }else{
                focus.highlight(false);
                focus = null;
                setBoardMode(BoardMode.DEFAULT);
                //System.out.println("Focus Removed");
            }
        }

    }

    public void initPlayerTurn(){
        //re-enable inputs
        gameboardHUD.stage.getRoot().setTouchable(Touchable.enabled);
        stage.getRoot().setTouchable(Touchable.enabled);
        System.out.println("Player's turn");
        saveGameState();
        //~Implement Point Gathering Here~
    }

    public void initAiTurn(){
        //cleanup, dont touch anything during AI's turn
        unhightlightTiles();
        gameboardHUD.stage.getRoot().setTouchable(Touchable.disabled);
        stage.getRoot().setTouchable(Touchable.disabled);
        System.out.println("Ai's turn");
        passTurn();
    }

    public void setFocus(Hex newfocus){
        if(focus != null){
           focus.highlight(false);
            if(focusAdjacents.size > 0) {
                for (Hex current : focusAdjacents) {
                    current.highlight(false);
                }
            }

        }
        focus = newfocus;

        newfocus.highlight(true);
        //System.out.printf("Current Focus: %s\n", this.focus.getName());
        collectAdjacents();


    }

    public void unhightlightTiles(){
        if(focus != null) {
            focus.highlight(false);
        }
        if(focusAdjacents.size > 0) {
            for (Hex current : focusAdjacents) {
                current.highlight(false);
            }
        }
    }

    public Hex getFocus(){
        return focus;
    }

    private void collectAdjacents(){
        //clear out old adjacent (may rework to just return array)
        focusAdjacents.clear();
        Vector2 localCoords = new Vector2(focus.getOriginX(), focus.getOriginY());
        Vector2 stageCoords = focus.localToStageCoordinates(localCoords);
        Vector2 hitCoords;
        for(int i = 30; i < 390; i+=60){
            double radsI = Math.toRadians(i);
            hitCoords = new Vector2((float)(stageCoords.x+(TILE_WIDTH*Math.cos(radsI))), (float)(stageCoords.y + (TILE_WIDTH*Math.sin(radsI))));
            //System.out.printf("Hitting target at x=%f, y=%f\n", hitCoords.x, hitCoords.y);
            Actor target = stage.hit(hitCoords.x, hitCoords.y, true);
            if(target != null){
                //System.out.printf("Tap Target: %s\n", target.getName());
                if(target.getName().equals("Hex")){
                    focusAdjacents.add((Hex)target);
                    //System.out.println("Added Adjacent");
                }
            }
        }


    }

    public void setBoardMode(BoardMode mode){
        boardMode = mode;
        if(mode == BoardMode.DEFAULT){
            gameboardHUD.hideTileHUD(true);
        }
        else if(mode == BoardMode.ATTACK){
            for(Hex current:focusAdjacents){
                if(current.getState()==HexState.AI_ACTIVE || current.getState()==HexState.AI_INACTIVE){
                    current.highlight(true);
                }
            }
        }
        else if(mode == BoardMode.EXPAND){

            for(Hex current:focusAdjacents){
                if(current.getState() == HexState.UNOWNED){
                    current.highlight(true);


                }
            }

        }
        else if(mode == BoardMode.DEFEND){
            focus.setFortify(true);
        }
    }

    private void placeTiles(){


        //Place Base Tiles
        placeTileAt(18, 1, HexState.PLAYER_ACTIVE, Hex.HexType.BASE);
        placeTileAt(4, 4, HexState.AI_ACTIVE, Hex.HexType.BASE);

        //Place Special Tiles
        placeTileAt(16, 3, HexState.UNOWNED, Hex.HexType.SPECIAL);
        placeTileAt(10, 1, HexState.UNOWNED, Hex.HexType.SPECIAL);
    }

    private void placeTileAt(int row, int col, HexState state, Hex.HexType type){
        Vector2 screenCoords = boardToScreenCoords(row, col);
        Vector2 stageCoords = stage.screenToStageCoordinates(screenCoords);
        Actor target = stage.hit(stageCoords.x, stageCoords.y, true);
        if(target != null){
            if(target.getName().equals("Hex")){
                Hex targetHex = (Hex)target;
                targetHex.setState(state);
                targetHex.setHexType(type);
            }
        }
    }

    //fetches Origin of a hex in screen coordinates
    private Vector2 boardToScreenCoords(int row, int col){
        int x = col;
        int y = row;
        float xOffset = 19.5f;
        float yOffset = 120;
        float oddXOffset = xOffset + TILE_WIDTH*0.74f;
        float oddYOffset = yOffset + TILE_HEIGHT*0.49f;

        if(y % 2 == 0){
            //Even row calculations
            float resultX = (float)1.47*TILE_WIDTH*x + xOffset - TILE_WIDTH/2;
            float resultY = (float)TILE_HEIGHT*(y/2) + yOffset - TILE_HEIGHT/2;
            return new Vector2(resultX,resultY);
        }else{
            //Odd Row calculations
            float resultX = (float)(1.47*TILE_WIDTH*x) + oddXOffset - TILE_WIDTH/2;
            float resultY = (float)(TILE_HEIGHT*(y/2)) + oddYOffset - TILE_HEIGHT/2;
            return new Vector2(resultX,resultY);
        }
    }

    public void passTurn(){
        if(stateMachine.getCurrentState() == GameState.PLAYER_TURN){
            stateMachine.changeState(GameState.AI_TURN);
        }
        else if(stateMachine.getCurrentState() == GameState.AI_TURN){
            stateMachine.changeState(GameState.PLAYER_TURN);
        }
    }





}
