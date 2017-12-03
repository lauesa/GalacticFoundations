package com.github.t7.galacticfoundations.activities;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.github.t7.galacticfoundations.galacticfoundations;

/**
 * Created by Scott on 11/27/2017.
 */

public class SettingsActivity extends Activity {

        private static final int BUTTONWIDTH = galacticfoundations.WIDTH/3;
        private Texture bg;
        private Stage stage;
        private InputMultiplexer multiplexer;

        //Variables for creating buttons
        private TextureAtlas atlas;
        private Skin skin;

        public SettingsActivity(final ActivityManager activityManager) {
            super(activityManager);
            viewport.apply();
            bg = new Texture("pixel_mars.jpg");
            cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
            stage = new Stage(viewport);

            //Define button skin, textureAtlas
            atlas = new TextureAtlas("skin\\GameBoardHUD_skin\\star-soldier-ui.atlas");
            skin = new Skin(Gdx.files.internal("skin\\GameBoardHUD_skin\\star-soldier-ui.json"), atlas);

            //Define slider
            final Slider slider = new Slider(0, 10, 1, false, skin);
            slider.setValue(5);
            Label sliderLabel = new Label("Volume" ,skin);
            sliderLabel.setFontScale(2);

            //Define next and prev buttons

            TextButton backButton = new TextButton("Back", skin);
            backButton.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(0.25f), Actions.fadeIn(0.40f)));

            //Add button listeners
            backButton.addListener(new ActorGestureListener() {
                @Override
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    super.tap(event, x, y, count, button);
                    activityManager.set(new MainActivity(activityManager));
                }
            });

            if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                galacticfoundations.backpressed=true;
                activityManager.set(new MainActivity(activityManager)); // go to main menu if back button pressed
            }

            //Table of buttons
            Table table = new Table();
            table.setFillParent(true);
            table.bottom().padBottom(5).padTop(20);

            table.add(sliderLabel);
            table.row();
            table.add(slider);
            table.row();
            table.add().fillY().expandY();
            table.row();
            table.add(backButton);
            stage.addActor(table);

            multiplexer = new InputMultiplexer();
            Gdx.input.setInputProcessor(multiplexer);
            multiplexer.addProcessor(stage);

            slider.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    Gdx.app.log("UITest", "slider: " + slider.getValue());
                    MainActivity.mastervol = slider.getValue()/100;
                    Gdx.app.log("mastervol", "val: " + MainActivity.mastervol);
                    MainActivity.music.setVolume(MainActivity.mastervol);
                }
            });
        }

        /* Overrides */
        @Override

        public void resize(int width, int height) {}

        @Override
        protected void handleInput() {}

        @Override
        public void update(float dt) {}

        @Override
        public void render(SpriteBatch sb) {
            cam.update();
            sb.setProjectionMatrix(cam.combined);
            sb.begin();
            sb.draw(bg, 0, 0);
            sb.end();
            stage.act();
            stage.draw();

            // Goes to main menu when back button pressed
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
                galacticfoundations.backpressed=true;
                activityManager.set(new MainActivity(activityManager)); // go to main menu if back button pressed
            }
        }

        @Override
        public void dispose() {

        }
    }
