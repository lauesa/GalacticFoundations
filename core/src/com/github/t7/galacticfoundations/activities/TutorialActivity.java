package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.github.t7.galacticfoundations.galacticfoundations;

import java.util.ArrayList;

/**
 * Created by jaken on 11/26/2017.
 */

public class TutorialActivity extends Activity {
    private static final int BUTTONWIDTH = galacticfoundations.WIDTH/3;
    private Texture bg;
    private Stage stage;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private int currentSlide = 0;
    private boolean end = false;

    //Variables for creating buttons
    private TextureAtlas atlas;
    private Skin skin;

    public TutorialActivity(final ActivityManager activityManager) {
        super(activityManager);
        viewport.apply();

        // Add tutorial images to imagePaths
        imagePaths.add("tutorial_images\\tut0.png");
        imagePaths.add("tutorial_images\\tut1.png");
        imagePaths.add("tutorial_images\\tut2.png");
        imagePaths.add("tutorial_images\\tut3.png");
        imagePaths.add("tutorial_images\\tut4.png");

        bg = new Texture(imagePaths.get(currentSlide));
        cam.setToOrtho(false, galacticfoundations.WIDTH, galacticfoundations.HEIGHT);
        stage = new Stage(viewport);

        //Define button skin, textureAtlas
        atlas = new TextureAtlas("skin\\GameBoardHUD_skin\\star-soldier-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin\\GameBoardHUD_skin\\star-soldier-ui.json"), atlas);

        //Define next and prev buttons
        TextButton prevButton = new TextButton("Prev", skin);
        prevButton.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(0.25f), Actions.fadeIn(0.40f)));
        final TextButton nextButton = new TextButton("Next", skin);
        nextButton.addAction(Actions.sequence(Actions.alpha(0),Actions.delay(0.25f), Actions.fadeIn(0.40f)));

        //Add button listeners
        prevButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                if(currentSlide >= 1)
                {
                    nextButton.setText("Next");
                    currentSlide--;
                    bg = new Texture(imagePaths.get(currentSlide));
                }
            }
        });

        nextButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);

                System.out.println(nextButton.getText());
                if(currentSlide < imagePaths.size() - 1)
                {
                    end = false;
                    currentSlide++;
                    bg = new Texture(imagePaths.get(currentSlide));
                }

                if(currentSlide == imagePaths.size() - 1 ){

                    nextButton.setText("End");
                    end = !end;
                    if(!end){
                        activityManager.set(new MainActivity(activityManager));
                    }
                }

            }
        });

        //Table of buttons
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(5);

        table.add(prevButton).expandX().width(BUTTONWIDTH);
        table.add(nextButton).expandX().width(BUTTONWIDTH);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    /* Overrides */
    @Override
    public void resize(int width, int height) {}



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
        bg.dispose();
        stage.dispose();
    }
}
