package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private GestureDetector gestureDetector;
    private InputListener inputListener;


    public GameboardActivity(ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();
        bg = new Texture("badlogic.jpg");
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        zoomScale = 1f;
        stage = new Stage();
//        gestureListener = new GestureDetector.GestureListener() {
//            @Override
//            public boolean touchDown(float x, float y, int pointer, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean tap(float x, float y, int count, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean longPress(float x, float y) {
//                return false;
//            }
//
//            @Override
//            public boolean fling(float velocityX, float velocityY, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean pan(float x, float y, float deltaX, float deltaY) {
//                return false;
//            }
//
//            @Override
//            public boolean panStop(float x, float y, int pointer, int button) {
//                return false;
//            }
//
//            @Override
//            public boolean zoom(float initialDistance, float distance) {
//                //float amount = (initialDistance - distance)/100;
//                float amount = distance / 100;
//                System.out.printf("Pinch level: %f\n", distance);
//                if(cam.zoom > 0.5 && cam.zoom < 1){
//                    cam.zoom = (float)(0.05*(amount));
//                }
//                else{
//                    if(cam.zoom >= 1f){
//                        if(amount < 0){
//                            cam.zoom += 0.05*amount;
//                        }
//                    }
//                    if(cam.zoom <= 0.5){
//                        if(amount > 0){
//                            cam.zoom += 0.05*amount;
//                        }
//                    }
//                }
//
//
//                return true;
//            }
//
//            @Override
//            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
//                return false;
//            }
//
//            @Override
//            public void pinchStop() {
//
//
//            }
//        };
        GestureDetector gestureDetector = new GestureDetector(new GestureDetector.GestureAdapter(){
            private Vector2 oldInitialFirstPointer=null, oldInitialSecondPointer=null;
            private float oldScale;
            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                cam.update();
                System.out.printf("Cam X: %f, Cam Y: %f\n", cam.position.x, cam.position.y);
                if((cam.position.x > 0 && cam.position.x < 500) && cam.position.y > 0 && cam.position.y < 500) {
                    cam.position.add(
                            cam.unproject(new Vector3(0, 0, 0))
                                    .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                    );
                }
                else if((cam.position.x <= 0 && cam.position.y <= 0) || (cam.position.x <= 0 && cam.position.y >= 500) || (cam.position.x >= 500 && cam.position.y <= 0) || (cam.position.x >= 500 && cam.position.y >= 500)){
                    if(deltaX < 0 && deltaY > 0) {
                        cam.position.add(
                                cam.unproject(new Vector3(0, 0, 0))
                                        .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                        );
                    }
                    if(deltaX < 0 && deltaY < 0){
                        cam.position.add(
                                cam.unproject(new Vector3(0, 0, 0))
                                        .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                        );
                    }
                    if(deltaX > 0 && deltaY > 0){
                        cam.position.add(
                                cam.unproject(new Vector3(0, 0, 0))
                                        .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                        );
                    }
                    if(deltaX > 0 && deltaY < 0){
                        cam.position.add(
                                cam.unproject(new Vector3(0, 0, 0))
                                        .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                        );
                    }

                }
                else{
                    if(cam.position.x <= 0){
                        if(deltaX < 0){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else{
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if(cam.position.x >= 500){
                        if(deltaX > 0){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else{
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(0, deltaY, 0)).scl(-1f))
                            );
                        }
                    }
                    if(cam.position.y <= 0){
                        if(deltaY > 0){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        } else{
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, 0, 0)).scl(-1f))
                            );
                        }
                    }
                    if(cam.position.y >= 500){
                        if(deltaY < 0){
                            cam.position.add(
                                    cam.unproject(new Vector3(0, 0, 0))
                                            .add(cam.unproject(new Vector3(deltaX, deltaY, 0)).scl(-1f))
                            );
                        }else{
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

        stage.addListener(new InputListener() {
            public boolean scrolled(InputEvent event, float x, float y, int amount){
                System.out.printf("Zoom:%f\n", cam.zoom);
                if(cam.zoom > 0.5f && cam.zoom < 1f){
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
        multiplexer.addProcessor(gestureDetector);
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
