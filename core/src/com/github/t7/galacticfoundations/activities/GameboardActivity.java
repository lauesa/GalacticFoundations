package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.actors.PlayerHex;
import com.github.t7.galacticfoundations.galacticfoundations;
import com.github.t7.galacticfoundations.hud.GameboardHUD;

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


    public GameboardActivity(ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();
        bg = new Texture("gameboard_bg.png");
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        zoomScale = 1f;
        stage = new Stage(viewport);

        //Initiate gameboardHUD
        gameboardHUD = new GameboardHUD(galacticfoundations.batch);


        GestureDetector gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter(){
            private Vector2 oldInitialFirstPointer=null, oldInitialSecondPointer=null;
            private float oldScale;
            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {

                cam.update();
                //System.out.printf("Cam X: %f, Cam Y: %f\n", cam.position.x, cam.position.y);

                if((cam.position.x > 0 && cam.position.x < 500) && (cam.position.y > 0 && cam.position.y < 500)) {
                    cam.position.add(
                            cam.unproject(new Vector3(0, 0, 0))
                                    .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                    );
                }

                else{
                    if(cam.position.x <= 0){


                        if(deltaX <= 0 && (cam.position.y > 0 && cam.position.y < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        }else if(cam.position.y > 0 && cam.position.y < 500){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }

                    }
                    if(cam.position.x >= 500){

                        if(deltaX >= 0 && (cam.position.y > 0 && cam.position.y < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if(cam.position.y > 0 && cam.position.y < 500){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if(cam.position.y <= 0){

                        if(deltaY >= 0 ){//&& (cam.position.x > 0 && cam.position.x < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else if(cam.position.x > 0 && cam.position.x < 500){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                    if(cam.position.y >= 500){

                        if(deltaY <= 0){// && (cam.position.x > 0 && cam.position.x < 500)){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        }else if(cam.position.x > 0 && cam.position.x < 500){
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
            public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
                if(!(initialFirstPointer.equals(oldInitialFirstPointer)&&initialSecondPointer.equals(oldInitialSecondPointer))){
                    oldInitialFirstPointer = initialFirstPointer.cpy();
                    oldInitialSecondPointer = initialSecondPointer.cpy();
                    oldScale = cam.zoom;
                }
                Vector3 center = new Vector3(
                        (firstPointer.x+initialSecondPointer.x)/2,
                        (firstPointer.y+initialSecondPointer.y)/2,
                        0
                );
                zoomCamera(center, oldScale*initialFirstPointer.dst(initialSecondPointer)/firstPointer.dst(secondPointer));
                return true;
            }
            private void zoomCamera(Vector3 origin, float scale){
                cam.update();
                Vector3 oldUnprojection = cam.unproject(origin.cpy()).cpy();
                cam.zoom = scale; //Larger value of zoom = small images, border view
                cam.zoom = Math.min(2.0f, Math.max(cam.zoom, 0.5f));
                cam.update();
                Vector3 newUnprojection = cam.unproject(origin.cpy()).cpy();
                cam.position.add(oldUnprojection.cpy().add(newUnprojection.cpy().scl(-1f)));
            }
        });

//        stage.addListener(new InputListener() {
//            public boolean scrolled(InputEvent event, float x, float y, int amount){
//                System.out.printf("Zoom:%f\n", cam.zoom);
//                if(cam.zoom > 0.5f && cam.zoom < 1f){
//                        cam.zoom += 0.05*amount;
//                }
//                else{
//                    if(cam.zoom > 1f){
//                        if(amount < 0){
//                            cam.zoom += 0.05*amount;
//                        }
//                    }
//                    if(cam.zoom < 0.5){
//                        if(amount > 0){
//                            cam.zoom += 0.05*amount;
//                        }
//                    }
//                }
//                return true;
//            }
//        });


        //Gdx.input.setInputProcessor(new GestureDetector(gestureListener));
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(gestureDetector);
        multiplexer.addProcessor(stage);

//        //Toss in actors
//        Hex hex1 = new PlayerHex(Hex.HexType.GENERAL, 130,100);
////        hex1.setName("Hex1");
//        Hex hex2 = new PlayerHex(Hex.HexType.GENERAL, 20,120);
////        hex2.setName("Hex2");
//        stage.addActor(hex1);
//        stage.addActor(hex2);

        generateBoard();

        //generateGameState();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {


    }

    @Override
    public void render(SpriteBatch sb) {
        cam.update();
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, 0, 0);
        sb.end();
        stage.act();
        stage.draw();

        galacticfoundations.batch.setProjectionMatrix(gameboardHUD.stage.getCamera().combined);
        gameboardHUD.stage.draw();

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

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
                    stage.addActor(new Hex(Hex.HexType.GENERAL, x, y));

                }
            }else{
                float oddXOffset = xOffset + TILE_WIDTH*0.74f;
                float oddYOffset = yOffset + TILE_HEIGHT*0.49f;

                //this for loop generates all actors and adds them to the board
                for(int j = 0; j < (boardWidth-1); j++){
                    float x = (float)(1.47*TILE_WIDTH*j) + oddXOffset;
                    float y = (float)(TILE_HEIGHT*(i/2)) + oddYOffset;
                    stage.addActor(new Hex(Hex.HexType.GENERAL, x, y));

                }
            }

        }
    }
}
