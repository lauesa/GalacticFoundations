package com.github.t7.galacticfoundations.hud;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.t7.galacticfoundations.activities.GameboardActivity;
import com.github.t7.galacticfoundations.actors.Hex;
import com.github.t7.galacticfoundations.galacticfoundations;
import com.github.t7.galacticfoundations.states.HexState;

public class GameboardHUD implements Disposable{
    private static final int BUTTON_WIDTH = 160;
    public Stage stage;
    private Viewport viewport;

    //Variables for creating buttons
    private TextureAtlas atlas;
    private Skin skin;


    public static int getCurrent() {
        return currentPoints;
    }

    public static void setCurrentPoints(int currentPoints) {
        GameboardHUD.currentPoints = currentPoints;
    }

    private static int currentPoints;

    //Scene2D widgets
    private Label currentPointsTitleLabel;
    //Label pointsLabel;

    private TextButton attackButton;
    private TextButton fortifyButton;
    private TextButton expandButton;

    //Instance of game board
    private GameboardActivity board;

    public GameboardHUD(SpriteBatch sb, GameboardActivity _board){
        //Define tracking variables


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
        TextButton menuButton = new TextButton("MENU", skin);

        //define points label, is updated when points value changes
        currentPointsTitleLabel = new Label(String.format("CP: %d", currentPoints), skin);

        //Add button listeners
        attackButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.ATTACK);
            }
        });

        menuButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.MENU);

            }
        });

        fortifyButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.DEFEND);
            }
        });

        expandButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                board.setBoardMode(GameboardActivity.BoardMode.EXPAND);
            }
        });

        stockpileButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                //Transfer Turn to AI
                board.passTurn();
            }
        });

        //Setup new camera
        viewport = new StretchViewport(galacticfoundations.WIDTH, galacticfoundations.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize GameboardHUD's labels
        Table table = new Table();
        table.setFillParent(true);
        table.left().padBottom(5).padTop(20);
        table.add(currentPointsTitleLabel).padLeft(50);
        table.add(menuButton).colspan(3).right().padRight(30).row();
        table.add().fillY().expandY();
        table.row();
        table.add(attackButton).left();
        table.add(fortifyButton).left().padLeft(10);
        table.add(expandButton).left();
        table.add(stockpileButton).right().padRight(30);

        //add table to the stage
        stage.addActor(table);

    }


    public void dispose() { stage.dispose(); }

    public Stage getStage(){
        return stage;
    }

    //Updates points label to display current points value
    public void updatePointsLabel(){
        currentPointsTitleLabel.setText(String.format("CP: %d", currentPoints));
    }

    //Will hid all action buttons for when a tile is not focused
    //Will only show buttons according to what points allow for
    public void hideTileHUD(boolean hide){
        if(hide){
            attackButton.setVisible(false);
            fortifyButton.setVisible(false);
            expandButton.setVisible(false);
        }else{
            //Only show button if focus can attack and player has 5 points or more
            if(board.getFocus().getCanAttack() && currentPoints >= 5) {
                boolean inRange = false;
                for(Hex current:board.adjacentHexes(board.getFocus())){
                    if(current.getState() == HexState.AI_INACTIVE){
                        inRange = true;
                    }
                }
                //only show button if you can attack a tile
                if(inRange) {
                    attackButton.setVisible(true);
                }
            }
            //only show fortify if player has 2 points or more
            if(currentPoints >= 2) {
                fortifyButton.setVisible(true);
            }
            //Only show expand if player has 1 point or more
            if(currentPoints >= 1) {
                expandButton.setVisible(true);
            }
        }
    }

    //update point value from game board
    public void addPoints(int points){
        currentPoints += points;
        updatePointsLabel();
        //System.out.printf("\nCurrent Points: %d\n", currentPoints);
    }

    //obtain current point value for functionality in game board
    public int getCurrentPoints(){
        return currentPoints;
    }

}