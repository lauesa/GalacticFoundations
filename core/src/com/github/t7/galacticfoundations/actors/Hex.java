package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private float x;
    private float y;


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

    @Override
    public String toString() {
        //function has been altered for save state
        return "" + hexType + " " + hexState + " " + x + " " + y + " ";
    }

    protected HexState hexState;
    private GameboardActivity board;


    public Hex(HexType type, float x, float y, GameboardActivity boardInstance) {
        super();
        hexType = type;
        this.x = x;
        this.y = y;
        position = new Vector2(x, y);

        setPosition(x, y);
        setState(HexState.UNOWNED);
        setOrigin(texture.getWidth()/2, texture.getHeight()/2);
        setName("Hex");


/**Old Event Listener, remove when positive gameboard level logic works **/
//        addListener( new ActorGestureListener() {
//            @Override
//            public void tap(InputEvent event, float x, float y, int count, int button) {
//                super.tap(event, x, y, count, button);
//                if (checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)) {
//                    //dragged = false;
//                    Vector2 currentCoords = new Vector2(x,y);
//                    Vector2 stageCoords = localToStageCoordinates(currentCoords);
//                    System.out.println("Tapped");
//                    System.out.printf("%f", stageCoords.x);
//                    Actor toTheLeft = getParent().hit(stageCoords.x-getWidth()/2, stageCoords.y,true);
//                    toTheLeft.setName("Left Actor");
//                    System.out.printf("Current Actor Name: %s\n Left Actor Name: %s", getName(), toTheLeft.getName());
//
//
//                    //System.out.printf("%s\n", event.getTarget().getName());
//
//                }
//            }
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
                texture = new Texture("blanktile.png");
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

    private void handleTap(){


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

    public void highlight(boolean isOn){
        if(isOn){
            //run highlight texture modification
        }
    }


}
