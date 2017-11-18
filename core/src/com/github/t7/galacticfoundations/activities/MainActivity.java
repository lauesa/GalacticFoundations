package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Warren on 11/18/2017.
 */

public class MainActivity extends Activity {
    Texture bg;

    public MainActivity(ActivityManager activityManager){
        super(activityManager);
        viewport.apply();
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        bg = new Texture("badlogic.jpg");
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

    }

    @Override
    public void dispose() {
        bg.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }
}

