package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Warren on 11/18/2017.
 */

public class MainActivity extends Activity {
    private Texture bg;
    private TextureAtlas atlas;
    private Skin skin;
    private Stage stage;

    public MainActivity(final ActivityManager activityManager){
        super(activityManager);
        viewport.apply();
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        bg = new Texture("pixel_mars.jpg");
        atlas = new TextureAtlas("skin\\quantum-horizon-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin\\quantum-horizon-ui.json"), atlas);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        TextButton resumeButton = new TextButton("Resume Game", skin);
        TextButton newGameButton = new TextButton("New Game", skin);
        newGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                activityManager.set(new GameboardActivity(activityManager));
            }
        });
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton creditsButton = new TextButton("Credits", skin);

        Image imageLogo = new Image();
        imageLogo.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Title.png")))));
        imageLogo.setScaling(Scaling.fit);

        mainTable.add(imageLogo).center().colspan(2);
        mainTable.row();

        mainTable.add(resumeButton);
        mainTable.row();
        mainTable.add(newGameButton);
        mainTable.row();
        mainTable.add(tutorialButton);
        mainTable.row();
        mainTable.add(creditsButton);
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

