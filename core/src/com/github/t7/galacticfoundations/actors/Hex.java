package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.github.t7.galacticfoundations.activities.GameboardActivity;

/**
 * Created by Warren on 11/20/2017.
 */

public class Hex extends Actor {
    protected Texture texture;
    protected Vector2 position;


    public enum HexType {
        GENERAL,
        SPECIAL,
        BASE;
    };
    protected HexType hexType;
    public enum HexState{
        UNOWNED,
        PLAYER_ACTIVE,
        PLAYER_INACTIVE,
        AI_ACTIVE,
        AI_INACTIVE;
    };
    protected HexState hexState;
    protected boolean dragged;


    public Hex(HexType type, float x, float y) {
        super();
        hexType = type;
        position = new Vector2(x, y);
        setPosition(x, y);
        setState(HexState.UNOWNED);
        setOrigin(texture.getWidth()/2, texture.getHeight()/2);
        dragged = false;

        addListener( new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if (checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)) {
                    //dragged = false;
                    System.out.println("Tapped");
                    //System.out.printf("%s\n", event.getTarget().getName());

                }
            }
        });

      


//        //listen for down click and verifies that it is the lowest level actor and checks hex bounds.
//        addListener(new ClickListener(){
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
//                if(checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)){
//                    dragged = false;;
//                    System.out.println("TouchDown");
//                    //System.out.printf("%s\n", event.getTarget().getName());
//                    return true;
//                }else{
//                    return false;
//                }
//
//            }
//        });

        //If not dragged, select hex.
//        addListener(new ClickListener(){
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
//                if(!GameboardActivity.zoomed && !GameboardActivity.panned){
//                    System.out.println("TouchUp");
//                    if(hexState == HexState.UNOWNED){
//                        setState(HexState.PLAYER_ACTIVE);
//                    }
//                    else if(hexState == HexState.PLAYER_ACTIVE){
//                        setState(HexState.PLAYER_INACTIVE);
//                    }
//                    else if(hexState == HexState.PLAYER_INACTIVE){
//                        setState(HexState.AI_ACTIVE);
//                    }
//                    else if(hexState == HexState.AI_ACTIVE){
//                        setState(HexState.AI_INACTIVE);
//                    }
//                    else if(hexState == HexState.AI_INACTIVE){
//                        setState(HexState.UNOWNED);
//                    }
//                }
//                cancel();
//
//            }
//        });

        //Do not select hex if dragged
//        addListener(new DragListener(){
//           public void touchDragged(InputEvent event, float x, float y, int pointer){
//               dragged = true;
//               //System.out.println("Dragged");
//
//           }
//        });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    public void setState(HexState state){
        if(texture != null){
            texture.dispose();
        }
        hexState = state;
        switch (state){
            case UNOWNED:{
                texture = new Texture("greytile.png");
                break;
            }
            case PLAYER_ACTIVE:{
                texture = new Texture("greentile.png");
                break;
            }
            case PLAYER_INACTIVE:{
                texture = new Texture("darkgreentile.png");
                break;
            }
            case AI_ACTIVE:{
                texture = new Texture("redtile.png");
                break;
            }
            case AI_INACTIVE:{
                texture = new Texture("darkredtile.png");
                break;
            }
        }

        this.setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    //Check if a click is within a hex
    public boolean checkBounds(float originX, float originY, float x, float y){
        double radius = texture.getWidth()/2;
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


}
