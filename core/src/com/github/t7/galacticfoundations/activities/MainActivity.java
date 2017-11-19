package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Warren on 11/18/2017.
 */

public class MainActivity extends Activity {
    private Texture bg;
    private TextureAtlas atlas;
    private Skin skin;
    private Stage stage;

    public MainActivity(ActivityManager activityManager){
        super(activityManager);
        viewport.apply();
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        bg = new Texture("badlogic.jpg");
        atlas = new TextureAtlas("skin\\quantum-horizon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin\\quantum-horizon-ui.json"), atlas);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();
        TextButton playButton = new TextButton("Play", skin);
        

        mainTable.add(playButton);
        stage.addActor(mainTable);

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
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        bg.dispose();
        skin.dispose();
        atlas.dispose();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }
}

