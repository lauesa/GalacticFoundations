package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.github.t7.galacticfoundations.states.HexState;

public class Hex extends Actor {
    //Pixel Maps for highlight and fortify
    private Pixmap highlightPixmap;
    private Pixmap fortifyPixmap;

    //asset textures
    private Texture texture;
    private Texture defaultTexture;

    //generated texture variants
    private Texture highlightTexture;
    private Texture fortifyTexture;
    private Texture fortifyHighlightTexture;

    //State Machine that manages who controls Hex
    private StateMachine<Hex, HexState> stateMachine;

    //position
    private float x;
    private float y;

    //Is Hex fortified
    private boolean fortifed;

    //Number of points the Hex is worth
    private int value;

    //Is Hex able to attack
    private boolean canAttack;

    //Enum for the types of Hexes defined here
    public enum HexType {
        GENERAL,
        SPECIAL,
        BASE
    }
    private HexType hexType;

    //Used by AI to get current state
    @Override
    public String toString() {
        //function has been altered for save state
        return "" + hexType + " " + stateMachine.getCurrentState() + " ";
    }

    public Hex(HexType type, float x, float y) {
        super();
        setHexType(type);
        this.x = x;
        this.y = y;
        //position = new Vector2(x, y);
        stateMachine = new DefaultStateMachine<Hex, HexState>(this, HexState.UNOWNED);
        fortifed = false;
        canAttack = true;

        setPosition(x, y);
        setState(HexState.UNOWNED);
        setOrigin(texture.getWidth()/2, texture.getHeight()/2);
        setName("Hex");
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    public void setState(HexState state){stateMachine.changeState(state);}

    //Depending on fortification, toggles use of highlight texture
    public void highlight(boolean isOn){
        if(isOn){
            if(fortifed){
                texture = fortifyHighlightTexture;
            }
            else{
                texture = highlightTexture;
            }
        }else{
            if(fortifed){
                texture = fortifyTexture;
            }
            else{
                texture = defaultTexture;
            }
        }
    }

    //Handles the action of fortifying a Hex, manages state and texture
    public void setFortify(boolean isOn){
        if(isOn){
            if(getState() == HexState.PLAYER_ACTIVE){
                setState(HexState.PLAYER_INACTIVE);
            }
            else if(getState() == HexState.AI_ACTIVE){
                setState(HexState.AI_INACTIVE);
            }
            fortifed = true;
            texture = fortifyTexture;
        }else{
            fortifed = false;
            texture = defaultTexture;
        }
    }

    //Generates all highlight and fortify texture variants using original assets and PixMaps
    private void generateColorMods(){
        Color color = new Color();

        //Generate Highlight texture
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

        //Generate Fortify texture
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
                fortifyPixmap.setColor(0.537f, 0.56f, 0.647f, 0.5f);
                if(A != 0 && R != 0 && G != 0 && B != 0)
                    fortifyPixmap.drawPixel(x, y);
            }
        }
        fortifyTexture = new Texture(fortifyPixmap);

        //Generate highlight texture for fortify texture
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

    //The following 'enter' functions are called when Hex enters a new state
    //These functions generate and set the new textures for that state
    public void enterUnowned(){
        if(hexType == HexType.SPECIAL){
            generateNewTextureSet("blankSRtile.png");
        } else {
            generateNewTextureSet("blanktile.png");
        }
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
    public void enterPlayerDeactivated(){
        if (hexType == HexType.BASE){
            generateNewTextureSet("darkbasetile.png");
        } else if (hexType == HexType.SPECIAL) {
            generateNewTextureSet("darkgreenSRtile.png");
        } else {
            generateNewTextureSet("darkgreentile.png");
        }
    }
    public void enterAiActivated(){
        if(hexType == HexType.BASE){
            generateNewTextureSet("AItile.png");
        }
        else if(hexType == HexType.SPECIAL){
            generateNewTextureSet("redSRtile.png");
        }
        else{
            generateNewTextureSet("redtile.png");
        }
    }
    public void enterAiDeactivated(){
        if(hexType == HexType.BASE){
            generateNewTextureSet("darkAItile.png");
        }
        else if(hexType == HexType.SPECIAL){
            generateNewTextureSet("darkredSRtile.png");
        }
        else{
            generateNewTextureSet("darkredtile.png");
        }
    }

    //Disposes of old textures and generates new from a filename
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

        //populate the new textures
        generateColorMods();

        //set starting texture
        if(!fortifed){
            texture = defaultTexture;
        }else{
            texture = fortifyTexture;
        }

        //update Hex bounds
        this.setBounds(getX(), getY(), texture.getWidth(), texture.getHeight());
    }

    public HexState getState(){return stateMachine.getCurrentState();}

    public HexType getHexType(){return hexType;}

    //sets the Hex type and updates Hex value
    public void setHexType(HexType type){
        hexType = type;
        if(type == HexType.GENERAL){
            value = 1;
        }
        else if(type == HexType.SPECIAL){
            value = 3;
        }else{
            value = 5;
        }
    }

    public int getValue(){return value;}

    public boolean getFortifyStatus(){
        return fortifed;
    }

    public boolean getCanAttack(){
        return canAttack;
    }

    public void setCanAttack(boolean state){
        canAttack = state;
    }

}
