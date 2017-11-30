package com.github.t7.galacticfoundations.hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.t7.galacticfoundations.activities.GameboardActivity;
import com.github.t7.galacticfoundations.activities.MainActivity;
import com.github.t7.galacticfoundations.galacticfoundations;

public class GameboardHUD implements Disposable{
    private static final int BUTTON_WIDTH = 160;
    public Stage stage;
    private Viewport viewport;

    //Variables for creating buttons
    private TextureAtlas atlas;
    private Skin skin;

    private static int currentPoints;

    //Scene2D widgets
    Label currentPointsTitleLabel;
    Label pointsLabel;

    private TextButton attackButton;
    private TextButton fortifyButton;
    private TextButton expandButton;

    private GameboardActivity board;

    public GameboardHUD(SpriteBatch sb, GameboardActivity _board){
        //Define tracking variables
        currentPoints = 5;

        //Hold Gameboard
        board = _board;

        //Define button skin, textureAtlas
        atlas = new TextureAtlas("skin\\GameBoardHUD_skin\\star-soldier-ui.atlas");
        skin = new Skin(Gdx.files.internal("skin\\GameBoardHUD_skin\\star-soldier-ui.json"), atlas);

        //Create TextButtons
        attackButton = new TextButton("ATT", skin);
        fortifyButton = new TextButton("DEF", skin);
        expandButton = new TextButton("EXP", skin);
        TextButton stockpileButton = new TextButton("STO", skin);
        TextButton confirmButton = new TextButton("Confirm", skin);

        //define labels using the String, and a Label style consisting of a font and color
        currentPointsTitleLabel = new Label("Current Points", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        pointsLabel = new Label(String.format("%d", currentPoints), new Label.LabelStyle(new BitmapFont(), Color.BLACK));

        //Add button listeners
        attackButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                System.out.println("Attack button pressed.");
                board.setBoardMode(GameboardActivity.BoardMode.ATTACK);
                currentPoints--;
                System.out.printf("Current points: %d\n", currentPoints);
                pointsLabel.setText(String.format("%d", currentPoints));
            }
        });

        fortifyButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.DEFEND);
                System.out.println("Fortify button pressed.");
            }
        });

        expandButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.EXPAND);
                System.out.println("Expand button pressed.");
            }
        });

        stockpileButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                System.out.println("Stockpile button pressed.");
                board.passTurn();
            }
        });

        //Setup new camera
        viewport = new StretchViewport(galacticfoundations.WIDTH, galacticfoundations.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize GameboardHUD's labels
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(5);
        table.add(currentPointsTitleLabel);
        table.add(pointsLabel).width(50);
        table.add(confirmButton);

        //table.row().padTop(galacticfoundations.HEIGHT*0.87f);
        table.row().padTop(galacticfoundations.HEIGHT*0.80f);
        table.add(attackButton).expandX().width(galacticfoundations.WIDTH/4);
        table.add(fortifyButton).expandX().width(galacticfoundations.WIDTH/4);
        table.add(expandButton).expandX().width(galacticfoundations.WIDTH/4);
        table.add(stockpileButton).expandX().width(galacticfoundations.WIDTH/4);

        //add table to the stage
        stage.addActor(table);
    }

    //public static void changeCurrentPoints(int newPoints){
     //   currentPoints = newPoints;
      //  pointsLabel.setText(String.format("%d", newPoints));
    //}

    public void dispose() { stage.dispose(); }

    public Stage getStage(){
        return stage;
    }

    public void hideTileHUD(boolean hide){
        if(hide){
            attackButton.setVisible(false);
            fortifyButton.setVisible(false);
            expandButton.setVisible(false);
        }else{
            attackButton.setVisible(true);
            fortifyButton.setVisible(true);
            expandButton.setVisible(true);
        }
    }

    public void addPoints(int points){
        currentPoints += points;
        System.out.printf("\nCurrent Points: %d\n", currentPoints);
    }
    public int getCurrentPoints(){
        return currentPoints;
    }


}