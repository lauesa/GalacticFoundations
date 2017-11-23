package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

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


        //listen for down click and verifies that it is the lowest level actor and checks hex bounds.
        addListener(new ClickListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                if(checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)){
                    dragged = false;;
                    System.out.println("TouchDown");
                    //System.out.printf("%s\n", event.getTarget().getName());
                    return true;
                }else{
                    return false;
                }

            }
        });

        //If not dragged, select hex.
        addListener(new ClickListener(){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                if(!dragged){
                    //if(checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)) {
                    //System.out.println("TouchUp");
                    //System.out.printf("%s\n", event.getTarget().getName());
                    setState(HexState.UNOWNED);
                    //}
                }
                cancel();

            }
        });

        //Do not select hex if dragged
        addListener(new DragListener(){
           public void touchDragged(InputEvent event, float x, float y, int pointer){
               dragged = true;
               //System.out.println("Dragged");

           }
        });

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
                texture = new Texture("greentile.png");
                break;
            }
            case PLAYER_ACTIVE:{
                texture = new Texture("hexagontiles//Tiles//tileMagic_tile.png");
                break;
            }
            case PLAYER_INACTIVE:{
                texture = new Texture("hexagontiles//Tiles//tileRock_tile.png");
                break;
            }
            case AI_ACTIVE:{
                texture = new Texture("hexagontiles//Tiles//tileLava_tile.png");
                break;
            }
            case AI_INACTIVE:{
                texture = new Texture("hexagontiles//Tiles//tileSand_tile.png");
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
