package com.github.t7.galacticfoundations.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by Warren on 11/20/2017.
 */

public class PlayerHex extends Hex {

    public PlayerHex(HexType type, float x, float y) {
        super(type, x, y);
        setState(HexState.PLAYER_ACTIVE);
        //listen for down click and verifies that it is the lowest level actor.
        addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //if(getParent().hit(x, y, true).equals(event.getTarget())){
                if(checkBounds(event.getTarget().getOriginX(), event.getTarget().getOriginY(), x, y)){
                    System.out.printf("%s\n", event.getTarget().getName());
                    setState(HexState.UNOWNED);
                    return true;
                }else{
                    return false;
                }

            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
