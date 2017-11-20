package com.github.t7.galacticfoundations;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.t7.galacticfoundations.activities.ActivityManager;
import com.github.t7.galacticfoundations.activities.MainActivity;

public class galacticfoundations extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final String TITLE = "Galactic Foundations";
	private ActivityManager activityManager;
	public static SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		activityManager = new ActivityManager();
		Gdx.gl.glClearColor(1,0,0,1);
		activityManager.push(new MainActivity(activityManager));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		activityManager.update(Gdx.graphics.getDeltaTime());
		activityManager.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}