package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.github.t7.galacticfoundations.states.HexState;


import static com.github.t7.galacticfoundations.actors.Hex.HexType.GENERAL;
import static com.github.t7.galacticfoundations.actors.Hex.HexType.SPECIAL;

/**
 * Created by Warren on 11/20/2017.
 */

public class Hex extends Actor {
    private Pixmap highlightPixmap;
    private Pixmap fortifyPixmap;

    protected Texture texture;
    private Texture defaultTexture;
    private Texture highlightTexture;
    private Texture fortifyTexture;
    private Texture fortifyHighlightTexture;
    protected Vector2 position;
    private StateMachine<Hex, HexState> stateMachine;
    private float x;
    private float y;
    private boolean foritifed;


    public enum HexType {
        GENERAL,
        SPECIAL,
        BASE;
    };
    protected HexType hexType;
//    public enum HexState{
//        UNOWNED,
//        PLAYER_ACTIVE,
//        PLAYER_INACTIVE,
//        AI_ACTIVE,
//        AI_INACTIVE;
//    };
//  protected HexState hexState;
    @Override
    public String toString() {
        //function has been altered for save state
        return "" + hexType + " " + stateMachine.getCurrentState() + " ";
    }





    public Hex(HexType type, float x, float y) {
        super();
        hexType = type;
        this.x = x;
        this.y = y;
        position = new Vector2(x, y);
        stateMachine = new DefaultStateMachine<Hex, HexState>(this, HexState.UNOWNED);
        foritifed = false;

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
        stateMachine.changeState(state);
//        if(texture != null){
//            texture.dispose();
//        }
//        hexState = state;
//        switch (state){
//            case UNOWNED:{
////                pixmap = new Pixmap(new FileHandle("blanktile.png"));
////                defaultTexture = new Texture(pixmap);
////                texture = new Texture(pixmap);
////                generateHighlight();
////                highlightTexture = new Texture(pixmap);
////                break;
//
//            }
//            case PLAYER_ACTIVE:{
//                texture = new Texture("greentile.png");
//                break;
//            }
//            case PLAYER_INACTIVE:{
//                texture = new Texture("darkgreentile.png");
//                break;
//            }
//            case AI_ACTIVE:{
//                texture = new Texture("redtile.png");
//                break;
//            }
//            case AI_INACTIVE:{
//                texture = new Texture("darkredtile.png");
//                break;
//            }
//        }
//
//        this.setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }



    //Check if a click is within a hex
//    public boolean checkBounds(float originX, float originY, float x, float y){
//        double radius = texture.getWidth()/2;
//        double deltaX = Math.abs(x - originX);
//        double deltaY = Math.abs(y - originY);
//        //Find distance from origin
//        double distance = Math.sqrt((Math.pow(deltaX,2)+Math.pow(deltaY,2)));
//
//        //Determine the distance within bounds
//        double angleRads = Math.atan2(deltaY, deltaX);
//
//
//        double angleDegrees = Math.toDegrees(angleRads) % 60;
//
//        double boundary = (Math.sqrt(3)*radius) / (Math.sqrt(3)*(Math.cos(Math.toRadians(angleDegrees)) + Math.sin(Math.toRadians(angleDegrees))));;
//        //System.out.printf("Degrees %f, Distance: %f, Boundary: %f, radius: %f\n", angleDegrees, distance, boundary, radius);
//        if(distance <= boundary){return true;}
//        else{
//            return false;
//        }
//
//    }

    public void highlight(boolean isOn){
        if(isOn){
            if(foritifed){
                texture = fortifyHighlightTexture;
            }
            else{
                texture = highlightTexture;
            }

        }else{
            if(foritifed){
                texture = fortifyTexture;
            }
            else{
                texture = defaultTexture;
            }

        }
    }

    public void setFortify(boolean isOn){
        if(isOn){
            foritifed = true;
            texture = fortifyTexture;
        }else{
            foritifed = false;
            texture = fortifyTexture;
        }
    }

    private void generateColorMods(){
        Color color = new Color();

        for (int x = 0; x < highlightPixmap.getWidth(); x++)
        {
            for (int y = 0; y < highlightPixmap.getHeight(); y++)
            {
                int val = highlightPixmap.getPixel(x, y);
                Color.rgba8888ToColor(color, val);
                int A = (int) (color.a * 255f);
                int R = (int) (color.r * 255f);
                int G = (int) (color.g * 255f);
                int B = (int) (color.b * 255f);
                highlightPixmap.setColor(0.85f, 1f, 0.85f, 0.75f);
                if(A != 0 && R != 0 && G != 0 && B != 0)
                    highlightPixmap.drawPixel(x, y);
            }
        }
        highlightTexture = new Texture(highlightPixmap);

        for (int x = 0; x < highlightPixmap.getWidth(); x++)
        {
            for (int y = 0; y < fortifyPixmap.getHeight(); y++)
            {
                int val = fortifyPixmap.getPixel(x, y);
                Color.rgba8888ToColor(color, val);
                int A = (int) (color.a * 255f);
                int R = (int) (color.r * 255f);
                int G = (int) (color.g * 255f);
                int B = (int) (color.b * 255f);
                fortifyPixmap.setColor(0.537f, 0.56f, 0.647f, 0.5f);
                if(A != 0 && R != 0 && G != 0 && B != 0)
                    fortifyPixmap.drawPixel(x, y);
            }
        }
        fortifyTexture = new Texture(fortifyPixmap);

        for (int x = 0; x < fortifyPixmap.getWidth(); x++)
        {
            for (int y = 0; y < fortifyPixmap.getHeight(); y++)
            {
                int val = fortifyPixmap.getPixel(x, y);
                Color.rgba8888ToColor(color, val);
                int A = (int) (color.a * 255f);
                int R = (int) (color.r * 255f);
                int G = (int) (color.g * 255f);
                int B = (int) (color.b * 255f);
                fortifyPixmap.setColor(0.85f, 1f, 0.85f, 0.75f);
                if(A != 0 && R != 0 && G != 0 && B != 0)
                    fortifyPixmap.drawPixel(x, y);
            }
        }
        fortifyHighlightTexture = new Texture(fortifyPixmap);
    }

    public void enterUnowned(){
        if(hexType == HexType.SPECIAL){
            generateNewTextureSet("blankSRtile.png");
        } else {
            generateNewTextureSet("blanktile.png");
        }
    }

    public void exitUnowned(){
    }
    public void enterPlayerActivated(){
        if(hexType == HexType.SPECIAL){
            generateNewTextureSet("greenSRtile.png");
        } else if (hexType == HexType.BASE){
            generateNewTextureSet("basetile.png");
        } else {
            generateNewTextureSet("greentile.png");
        }

    }
    public void exitPlayerActivated(){

    }
    public void enterPlayerDeactivated(){
        if (hexType == HexType.BASE){
            generateNewTextureSet("darkbasetile.png");
        } else if (hexType == HexType.SPECIAL) {
        } else {
            generateNewTextureSet("darkgreentile.png");
        }
    }
    public void exitPlayerDeactivated(){

    }
    public void enterAiActivated(){
        generateNewTextureSet("redtile.png");

    }
    public void exitAiActivated(){

    }
    public void enterAiDeactivated(){

    }
    public void exitAiDeactivated(){
        generateNewTextureSet("darkredtile.png");

    }

    private void generateNewTextureSet(String filepath){
        if(highlightPixmap != null){
            highlightPixmap.dispose();
        }
        if(fortifyPixmap != null){
            fortifyPixmap.dispose();
        }
        if(defaultTexture != null){
            defaultTexture.dispose();
        }
        if(highlightTexture != null){
            highlightTexture.dispose();
        }
        if(fortifyPixmap != null){
            fortifyTexture.dispose();
        }

        highlightPixmap = new Pixmap(Gdx.files.internal(filepath));
        fortifyPixmap = new Pixmap(Gdx.files.internal(filepath));
        defaultTexture = new Texture(highlightPixmap);
        texture = new Texture(highlightPixmap);
        generateColorMods();
        highlightTexture = new Texture(highlightPixmap);
        this.setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    public HexState getState(){return stateMachine.getCurrentState();}

    public void setHexType(HexType type){
        hexType = type;
    }


}
