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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.galacticfoundations;
import com.github.t7.galacticfoundations.hud.GameboardHUD;
import com.github.t7.galacticfoundations.states.GameState;
import com.github.t7.galacticfoundations.states.HexState;
import java.util.Random;

import static com.github.t7.galacticfoundations.actors.Hex.HexType.BASE;
import static com.github.t7.galacticfoundations.actors.Hex.HexType.GENERAL;
import static com.github.t7.galacticfoundations.actors.Hex.HexType.SPECIAL;
import static com.github.t7.galacticfoundations.hud.GameboardHUD.getCurrent;

import java.util.ArrayList;

/**
 * Created by Warren on 11/19/2017.
 */

public class GameboardActivity extends Activity {
    //Hex Constants
    static final int TILE_WIDTH = 65;
    static final int TILE_HEIGHT = 55;

    //Background
    private Texture bg;

    //Stage that holds all Hexes
    private Stage stage;

    //HUD instance, holds buttons and points
    private GameboardHUD gameboardHUD;

    //State Machine that manages turn transitions
    private StateMachine<GameboardActivity, GameState> stateMachine;

    //Current Hex user is acting on
    private Hex focus;

    //The adjacent Hexes to focus
    private Array<Hex> focusAdjacents;

    //Enum for managing how user inputs are processed
    public enum BoardMode{
        DEFAULT,
        ATTACK,
        DEFEND,
        MENU,
        EXPAND;
    };
    private BoardMode boardMode;

    //AI class
    private AI_Activity ai;

    //Variables to check on the status of both base tiles to detect win conditions
    private Hex playerBase;
    private Hex aiBase;



    public GameboardActivity(ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);

        //Background
        bg = new Texture("marswide.jpg");

        //initialize AI instance
        ai = new AI_Activity();

        //When user is not interacting with a Hex, focus is null
        focus = null;
        focusAdjacents = new Array<Hex>();

        //Initialize board state machine
        stateMachine = new DefaultStateMachine<GameboardActivity, GameState>(this, GameState.PLAYER_TURN);

        //Initialize game board stage
        stage = new Stage(viewport);

        //Initialize gameboardHUD
        gameboardHUD = new GameboardHUD(galacticfoundations.batch, this);

        //When there is no focus, boardMode is default
        setBoardMode(BoardMode.DEFAULT);

        //Create screen input handlers
        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter() {
            private Vector2 oldInitialFirstPointer = null, oldInitialSecondPointer = null;
            private float oldScale;

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {

                cam.update();
                int minX = 0;
                int maxX = 500;
                int minY = 300;
                int maxY = 500;

                //Check screen boundaries while panning
                if ((cam.position.x > minX && cam.position.x < maxX) && (cam.position.y > minY && cam.position.y < maxY)) {
                    cam.position.add(
                            cam.unproject(new Vector3(0, 0, 0))
                                    .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                    );
                } else {
                    if (cam.position.x <= minX) {
                        if (deltaX <= 0 && (cam.position.y > minY && cam.position.y < maxY)) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.y > minY && cam.position.y < maxY) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if (cam.position.x >= maxX) {
                        if (deltaX >= 0 && (cam.position.y > minY && cam.position.y < maxY)) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.y > minY && cam.position.y < maxY) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if (cam.position.y <= minY) {
                        if (deltaY >= 0) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.x > minX && cam.position.x < maxX) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                    if (cam.position.y >= maxY) {
                        if (deltaY <= 0) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if (cam.position.x > minX && cam.position.x < maxX) {
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                }
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

            //HandleTap is called here, whenever tap input is detected
            @Override
            public boolean tap(float x, float y, int count, int button) {
                Vector2 currentCoords = new Vector2(x, y);
                Vector2 stageCoords = stage.screenToStageCoordinates(currentCoords);

                //Select Actor from board Stage and call handleTap
                Actor target = stage.hit(stageCoords.x, stageCoords.y, true);
                if(target != null){
                    handleTap(target);
                }

                //Event Handled
                return true;
            }
        });

        //InputMultiplexer will take in input handlers and pass them inputs based on order of insertion.
        //If a handler did not handle a specific event, the multiplexer gives it to the next handler in the list.
        InputMultiplexer multiplexer = new InputMultiplexer();

        //Set current input handler
        Gdx.input.setInputProcessor(multiplexer);

        //Add HUD so it handles tap gestures first
        multiplexer.addProcessor(gameboardHUD.getStage());
        //Add board gesture detector so it handles inputs second
        multiplexer.addProcessor(gestureDetector);

        //Resumes game or starts new game based on user selection
        if(MainActivity.RESUME == 1){
            loadGameState();
            MainActivity.RESUME = 0;
        } else {
            generateBoard();
        }



        
    }


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
        bg.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {}

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
        //System.out.printf("loadGameState Called\n");
        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();

        if(isLocAvailable == true){
            String locRoot = Gdx.files.getLocalStoragePath();
            //System.out.printf("%s", locRoot);
        } else {
            //System.out.printf("No Local Path");
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
                    //System.out.printf("%s %s %d \n", master[i][j][0], master[i][j][1], k);
                    k++;
                }
            } else {
                for (j = 0; j < 4; j++) {
                    master[i][j][0] = data[k];
                    k++;
                    master[i][j][1] = data[k];
                    //System.out.printf("\n%s %s %d", master[i][j][0], master[i][j][1], k);
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

        placeTileAt(130, 220, HexState.PLAYER_ACTIVE, Hex.HexType.BASE);
        placeTileAt(400, 600, HexState.AI_INACTIVE, Hex.HexType.BASE);

        //set currentpoints and resources
        int num = Integer.parseInt(data[190]);
        GameboardHUD.setCurrentPoints(num);
        System.out.print(num);
        num = Integer.parseInt(data[191]);
        AI_Activity.setResources(num);
        System.out.print(num);
        gameboardHUD.updatePointsLabel();


    }

    public void saveGameState(){

        boolean isLocAvailable = Gdx.files.isLocalStorageAvailable();

        if(isLocAvailable == true){
            String locRoot = Gdx.files.getLocalStoragePath();
            //System.out.printf("%s", locRoot);
        } else {
            //System.out.printf("No Local Path");
            return;
        }

        FileHandle file = Gdx.files.local("gamestate.txt");
        Gdx.files.local("gamestate.txt").delete();
        Array<Actor> listActors = stage.getActors();

        for (Actor hex: listActors) {
            file.writeString(hex.toString(), true);
        }
        String out = "" + GameboardHUD.getCurrent() + " " + AI_Activity.getResources() + " ";
        file.writeString(out, true);
    }


    public void generateBoard(){
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

        GameboardHUD.setCurrentPoints(5);
        gameboardHUD.updatePointsLabel();
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

    //user gameplay logic utilizing the current tile focus and the current board mode
    private void handleTap(Actor target){
        Hex hexTarget;
        if(target.getName().equals("Hex")){
            hexTarget = (Hex)target;
            //If you click a new tile
            if(focus != hexTarget){
                //if board is default and player selects another of their own tiles, change focus
                switch(boardMode){
                    case DEFAULT:{
                        if(hexTarget.getState() == HexState.PLAYER_ACTIVE) {
                            setFocus(hexTarget);
                            gameboardHUD.hideTileHUD(false);
                        }
                        break;
                    }
                    case ATTACK:{
                        //check if user has enough points
                        if(gameboardHUD.getCurrentPoints() >= 5) {
                            for (Hex current : focusAdjacents) {
                                if (((hexTarget.getState() == HexState.AI_ACTIVE) || (hexTarget.getState() == HexState.AI_INACTIVE)) && hexTarget == current) {
                                    //Deduct points
                                    gameboardHUD.addPoints(-5);
                                    rayAttack(focus, hexTarget);
                                    //Detect Victory
                                    if(aiBase.getState() == HexState.UNOWNED){
                                        activityManager.set(new VictoryActivity(activityManager));
                                    }
                                    //hexTarget.setState(HexState.UNOWNED);
                                    focus.setState(HexState.PLAYER_INACTIVE);
                                }
                            }
                            setBoardMode(BoardMode.DEFAULT);
                        }
                        break;
                    }
                    case EXPAND:{
                        //check if user has enough points to expand
                        if(gameboardHUD.getCurrentPoints() >= 1) {
                            for (int i = 0; i < focusAdjacents.size; i++) {
                                Hex current = focusAdjacents.get(i);
                                if ((hexTarget.getState() == HexState.UNOWNED) && (hexTarget == current)) {

                                    if(hexTarget.getHexType() == Hex.HexType.GENERAL){
                                        hexTarget.setState(HexState.PLAYER_ACTIVE);
                                        hexTarget.setCanAttack(false);
                                        //Deduct points
                                        gameboardHUD.addPoints(-1);
                                        focus.setState(HexState.PLAYER_INACTIVE);
                                        setBoardMode(BoardMode.DEFAULT);
                                        unhightlightTiles();
                                    }
                                    else if(hexTarget.getHexType() == Hex.HexType.SPECIAL && gameboardHUD.getCurrentPoints() >= 3){
                                        hexTarget.setState(HexState.PLAYER_ACTIVE);
                                        hexTarget.setCanAttack(false);
                                        //Deduct points
                                        gameboardHUD.addPoints(-3);
                                        focus.setState(HexState.PLAYER_INACTIVE);
                                        setBoardMode(BoardMode.DEFAULT);
                                        unhightlightTiles();
                                    }
                                }
                            }
                        }
                        break;
                    }
                }

            //If you click the focused tile again, remove focus
            }else{
                focus.highlight(false);
                setBoardMode(BoardMode.DEFAULT);
            }
        }
    }

    //This attack will attempt to hit 3 tiles in the direction if the first tile selected
    public void rayAttack(Hex origin, Hex target1){
        HexState targetState = HexState.UNOWNED;
        //if the origin is a Player tile
        if(origin.getState() == HexState.PLAYER_ACTIVE){
            targetState = HexState.AI_INACTIVE;
        }
        //If the origin is an AI tile
        else if(origin.getState() == HexState.AI_ACTIVE){
            targetState = HexState.PLAYER_INACTIVE;
        }
        if(target1.getState() == targetState){
            //If attack hits a fortify, stop attack
            if(target1.getFortifyStatus()){
                target1.setFortify(false);
            }else{
                if(target1.getHexType() == BASE){
                    if(targetState == HexState.AI_INACTIVE) {
                        activityManager.set(new VictoryActivity(activityManager));
                    }else if(targetState == HexState.PLAYER_INACTIVE){
                        activityManager.set(new DefeatActivity(activityManager));
                    }
                }
                target1.setState(HexState.UNOWNED);

                Vector2 focusLocalCoords = new Vector2(origin.getOriginX(), origin.getOriginY());
                Vector2 focusStageCoords = origin.localToStageCoordinates(focusLocalCoords);
                Vector2 targetLocalCoords = new Vector2(target1.getOriginX(), target1.getOriginY());
                Vector2 targetStageCoords = target1.localToStageCoordinates(targetLocalCoords);
                double theta = Math.toDegrees(Math.atan2((double)(targetStageCoords.y-focusStageCoords.y), (double)(targetStageCoords.x - focusStageCoords.x)));
                theta = (theta < 0)? (360d + theta):theta;
                double rads = Math.toRadians(theta);
                //find the vector that would hit the target hex
                Vector2 hit2Coords = new Vector2((float)(targetStageCoords.x+(TILE_HEIGHT*Math.cos(rads))), (float)(targetStageCoords.y + (TILE_HEIGHT*Math.sin(rads))));
                //look at target 2
                Actor hitTarget2 = stage.hit(hit2Coords.x, hit2Coords.y, true);
                if(hitTarget2 != null){
                    if(hitTarget2.getName().equals("Hex")){
                        Hex target2 = (Hex)hitTarget2;
                        if(target2.getState() == targetState){
                            if(target2.getFortifyStatus()){
                                target2.setFortify(false);
                            }
                            else {
                                if(target2.getHexType() == BASE){
                                    if(targetState == HexState.AI_INACTIVE) {
                                        activityManager.set(new VictoryActivity(activityManager));
                                    }else if(targetState == HexState.PLAYER_INACTIVE){
                                        activityManager.set(new DefeatActivity(activityManager));
                                    }
                                }
                                target2.setState(HexState.UNOWNED);

                                //Now that target2 was successful, do the same for target3
                                Vector2 hit3Coords = new Vector2((float)(hit2Coords.x+(TILE_HEIGHT*Math.cos(rads))), (float)(hit2Coords.y + (TILE_HEIGHT*Math.sin(rads))));
                                Actor hitTarget3 = stage.hit(hit3Coords.x, hit3Coords.y, true);
                                if (hitTarget3 != null) {
                                    if (hitTarget3.getName().equals("Hex")) {
                                        Hex target3 = (Hex) hitTarget3;
                                        if (target3.getState() == targetState) {
                                            if (target3.getFortifyStatus()) {
                                                target3.setFortify(false);
                                            } else {
                                                if(target3.getHexType() == BASE){
                                                    if(targetState == HexState.AI_INACTIVE) {
                                                        activityManager.set(new VictoryActivity(activityManager));
                                                    }else if(targetState == HexState.PLAYER_INACTIVE){
                                                        activityManager.set(new DefeatActivity(activityManager));
                                                    }
                                                }
                                                target3.setState(HexState.UNOWNED);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void initPlayerTurn(){
        //re-enable inputs
        collectConnected(0);
        gameboardHUD.stage.getRoot().setTouchable(Touchable.enabled);
        stage.getRoot().setTouchable(Touchable.enabled);
        saveGameState();

    }

    public void initAiTurn(){
        ArrayList<Hex> boardHex = new ArrayList<Hex>();
        Array<Actor> boardActors = stage.getActors();
        //cleanup, dont touch anything during AI's turn
        deactivate(1);
        deactivate(0);
        //board mode change also un-highlights tiles
        setBoardMode(boardMode.DEFAULT);
        gameboardHUD.stage.getRoot().setTouchable(Touchable.disabled);
        stage.getRoot().setTouchable(Touchable.disabled);

        for(Actor current: boardActors){
            if(current.getName().equals("Hex")){
                boardHex.add((Hex)current);
            }
        }
        collectConnected(1);
        //ai.addResources(5);
        ai.AI_turn(boardHex, this);
        deactivate(1);
        passTurn();
    }

    //Set current user selection tile
    public void setFocus(Hex newfocus){
        //clear previous selection
        if(focus != null){
           focus.highlight(false);
            if(focusAdjacents.size > 0) {
                for (Hex current : focusAdjacents) {
                    current.highlight(false);
                }
            }

        }
        focus = newfocus;
        if(newfocus != null) {
            newfocus.highlight(true);
            collectAdjacents();
        }


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
            Actor target = stage.hit(hitCoords.x, hitCoords.y, true);
            if(target != null){
                if(target.getName().equals("Hex")){
                    focusAdjacents.add((Hex)target);
                }
            }
        }
    }

    //Adjacent Funtion for Points collection
    public Array<Hex> adjacentHexes(Hex hexTarget){
        //result array
        Array<Hex> result = new Array<Hex>();
        Vector2 localCoords = new Vector2(hexTarget.getOriginX(), hexTarget.getOriginY());
        Vector2 stageCoords = hexTarget.localToStageCoordinates(localCoords);
        Vector2 hitCoords;
        for(int i = 30; i < 390; i+=60){
            double radsI = Math.toRadians(i);
            hitCoords = new Vector2((float)(stageCoords.x+(TILE_WIDTH*Math.cos(radsI))), (float)(stageCoords.y + (TILE_WIDTH*Math.sin(radsI))));
            Actor target = stage.hit(hitCoords.x, hitCoords.y, false);
            if(target != null){
                if(target.getName().equals("Hex")){
                    result.add((Hex)target);
                }
            }
        }
        return result;
    }

    //Deactivate tiles
    private void deactivate(int team){
        //Player Team
        for(Actor current:stage.getActors()){
            if(current.getName().equals("Hex")){
                Hex currentHex = (Hex)current;
                //Searching for Player Tiles
                if(team == 0){
                    if(currentHex.getState() == HexState.PLAYER_ACTIVE){
                        currentHex.setState(HexState.PLAYER_INACTIVE);
                    }
                }else{
                    if(currentHex.getState() == HexState.AI_ACTIVE){
                        currentHex.setState(HexState.AI_INACTIVE);
                    }
                }
            }
        }
    }

    //Points collection function
    private void collectConnected(int team){
        Hex origin;
        if(team == 1){
            origin = aiBase;
        } else {
            origin = playerBase;
        }

        Array<Hex> unchecked = new Array<Hex>();
        for(Actor current:stage.getActors()){
            if(current.getName().equals("Hex")){
                Hex currentHex = (Hex)current;
                //Searching for Player Tiles
                if(team == 0){
                    if(currentHex.getState() == HexState.PLAYER_INACTIVE){
                        unchecked.add(currentHex);
                    }
                }else{
                    if(currentHex.getState() == HexState.AI_INACTIVE){
                        unchecked.add(currentHex);
                    }
                }
            }
        }

        //Remove Origin
        Array<Hex> checked = new Array<Hex>();
        checked.add(origin);
        unchecked.removeValue(origin, true);


        //Get Base Tile
        Hex current = checked.get(0);
        //Calculate adjacent tiles

        Array<Hex> adjacents = adjacentHexes(current);

            //Work from Checked List. Init origin
            for(Hex source: checked){ //these have valid paths to BASE
                //clear list and recheck
                adjacents.clear();
                adjacents = adjacentHexes(source);
                for(Hex adj : adjacents){ //check each member of the adjacency list with unchecked
                    if(unchecked.contains(adj, true)){ //if unchecked is in the list of adj
                        int i = unchecked.indexOf(adj, true);
                        //int index = checked.indexOf(check, true);
                        checked.add(unchecked.get(i));
                        unchecked.removeIndex(i);
                    }
                }

        }



        //set active and collect points
        int sum = 0;
        for(Hex item:checked){
            sum +=item.getValue();
            if(team == 0){
                item.setState(HexState.PLAYER_ACTIVE);
                //new turn, all tiles can attack
                item.setCanAttack(true);
                gameboardHUD.addPoints(item.getValue());
            }
            else{
                item.setState(HexState.AI_ACTIVE);
                ai.addResources(item.getValue());
            }
        }



    }


    public void setBoardMode(BoardMode mode){
        boardMode = mode;
        unhightlightTiles();
        if(mode == BoardMode.DEFAULT){
            focus = null;
            gameboardHUD.hideTileHUD(true);
        }
        //if attack button was pushed and focus can attack
        else if(mode == BoardMode.ATTACK){
            for(Hex current:focusAdjacents){
                if(current.getState()==HexState.AI_ACTIVE || current.getState()==HexState.AI_INACTIVE){
                    current.highlight(true);
                }
            }
        }
        else if(mode == BoardMode.EXPAND){
            //if player cant afford expansion, don't highlight
            if(gameboardHUD.getCurrentPoints() >= 1) {

                for (Hex current : focusAdjacents) {
                    if (current.getState() == HexState.UNOWNED) {
                        if(current.getHexType() == Hex.HexType.GENERAL) {
                            current.highlight(true);
                        }
                        else if(current.getHexType() == Hex.HexType.SPECIAL && gameboardHUD.getCurrentPoints() >= 3){
                            current.highlight(true);
                        }
                    }
                }
            }else{
                //If user cant afford expansion, keep focus highlighted when button pushed
                focus.highlight(true);
            }

        }
        else if(mode == BoardMode.DEFEND){
            //if player can afford to fortify and it hasn't happened already
            if(gameboardHUD.getCurrentPoints() >= 2 && !focus.getFortifyStatus() && focus.getHexType() != Hex.HexType.BASE){
                focus.setFortify(true);
                gameboardHUD.addPoints(-2);
                setBoardMode(boardMode.DEFAULT);
            }else{
                focus.highlight(true);
            }

        }
        else if(mode == BoardMode.MENU){
            //go to menu
            activityManager.set(new MainActivity(activityManager));
        }
    }

    private void placeTiles(){


        //Place Base Tiles
        placeTileAt(130, 220, HexState.PLAYER_ACTIVE, Hex.HexType.BASE);
        placeTileAt(400, 600, HexState.AI_ACTIVE, Hex.HexType.BASE);

        //Place Special Tiles at
        //placeTileAt(300, 440, HexState.UNOWNED, Hex.HexType.SPECIAL);
        randSpecial(130, 220);
        randSpecial(400, 600);
        randSpecial(100, 600);
        randSpecial(400, 200);
        randSpecial(350, 350);
    }

    private void randSpecial(int x, int y){
        Random random = new Random();
        int choice = random.nextInt(5);

        Actor target = stage.hit(x, y, true);
        if((target != null) &&(target.getName().equals("Hex"))) {
            Hex targetHex = (Hex) target;
            Array<Hex> SRTplace = adjacentHexes(targetHex);
            targetHex = SRTplace.get(choice);
            targetHex.setHexType(Hex.HexType.SPECIAL);
            targetHex.setState(HexState.UNOWNED);
        }
    }

    private void placeTileAt(int x, int y, HexState state, Hex.HexType type){
        Actor target = stage.hit(x, y, true);
        if(target != null){
            if(target.getName().equals("Hex")){
                Hex targetHex = (Hex)target;
                targetHex.setHexType(type);
                targetHex.setState(state);
                if(type == Hex.HexType.BASE){
                    if(state == HexState.PLAYER_ACTIVE || state == HexState.PLAYER_INACTIVE){
                        playerBase = targetHex;
                    }
                    else if(state == HexState.AI_ACTIVE || state == HexState.AI_INACTIVE){
                        aiBase = targetHex;
                    }
                }
            }
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

    public Stage getStage(){
        return stage;
    }

    public Hex getPlayerBase(){return playerBase;}

    public ActivityManager getActivityManager(){return activityManager;}







}
