package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Warren on 11/19/2017.
 */

public class GameboardActivity extends Activity {
    private Texture bg;
    private float zoomScale;
    private Stage stage;
    private GestureDetector.GestureListener gestureListener;
    private InputListener inputListener;


    public GameboardActivity(ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();
        bg = new Texture("badlogic.jpg");
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        zoomScale = 1f;
        stage = new Stage();
        gestureListener = new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                return false;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                if(zoomScale > 0.25 || zoomScale < 1){
                    zoomScale = zoomScale + ((distance - initialDistance)/100);
                    cam.zoom += (distance - initialDistance);
                }


                return true;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {


            }
        };

        stage.addListener(new InputListener() {
            public boolean scrolled(InputEvent event, float x, float y, int amount){
                System.out.printf("Zoom:%f\n", cam.zoom);
                if(cam.zoom >= 0.5f && cam.zoom <= 1f){
                        cam.zoom += 0.05*amount;
                }
                else{
                    if(cam.zoom > 1f){
                        if(amount < 0){
                            cam.zoom += 0.05*amount;
                        }
                    }
                    if(cam.zoom < 0.5){
                        if(amount > 0){
                            cam.zoom += 0.05*amount;
                        }
                    }
                }
                return true;
            }
        });



        //Gdx.input.setInputProcessor(new GestureDetector(gestureListener));
        InputMultiplexer multiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(multiplexer);
        multiplexer.addProcessor(new GestureDetector(gestureListener));
        multiplexer.addProcessor(stage);

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

    }

    @Override
    public void dispose() {

    }

    @Override
    public void resize(int width, int height) {

    }
}
