package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Warren on 11/18/2017.
 */

public abstract class Activity {
    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected ActivityManager activityManager;
    protected Viewport viewport;

    protected Activity(ActivityManager activityManager){
        this.activityManager = activityManager;
        cam = new OrthographicCamera();
        viewport = new StretchViewport(galacticfoundations.WIDTH, galacticfoundations.HEIGHT, cam);
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
    public abstract void resize(int width, int height);
}