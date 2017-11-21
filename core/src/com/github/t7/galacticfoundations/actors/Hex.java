package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

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


    public Hex(HexType type, float x, float y) {
        super();
        hexType = type;
        position = new Vector2(x, y);
        setPosition(x, y);
        setState(HexState.UNOWNED);
        setOrigin(texture.getWidth()/2, texture.getHeight()/2);
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
                texture = new Texture("hexagontiles//Tiles//tileGrass_tile.png");
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
