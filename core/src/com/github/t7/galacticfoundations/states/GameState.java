package com.github.t7.galacticfoundations.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.github.t7.galacticfoundations.activities.GameboardActivity;

/**
 * Created by Warren on 11/27/2017.
 */

public enum GameState implements State<GameboardActivity> {
    PLAYER_TURN(){
        @Override
        public void enter(GameboardActivity entity) {
            entity.initPlayerTurn();

        }

        @Override
        public void update(GameboardActivity entity) {

        }

        @Override
        public void exit(GameboardActivity entity) {

        }

        @Override
        public boolean onMessage(GameboardActivity entity, Telegram telegram) {
            return false;
        }
    },
    AI_TURN(){
        @Override
        public void enter(GameboardActivity entity) {
            entity.initAiTurn();

        }

        @Override
        public void update(GameboardActivity entity) {

        }

        @Override
        public void exit(GameboardActivity entity) {

        }

        @Override
        public boolean onMessage(GameboardActivity entity, Telegram telegram) {
            return false;
        }
    }
}
