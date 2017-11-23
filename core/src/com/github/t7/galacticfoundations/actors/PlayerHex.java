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
