package com.github.t7.galacticfoundations.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.github.t7.galacticfoundations.actors.Hex;

/**
 * Created by Warren on 11/28/2017.
 */

public enum HexState implements State<Hex> {
    UNOWNED(){
        @Override
        public void enter(Hex entity) {

        }

        @Override
        public void update(Hex entity) {

        }

        @Override
        public void exit(Hex entity) {

        }

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    PLAYER_ACTIVATED(){
        @Override
        public void enter(Hex entity) {

        }

        @Override
        public void update(Hex entity) {

        }

        @Override
        public void exit(Hex entity) {

        }

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    PLATER_DEACTIVATED(){
        @Override
        public void enter(Hex entity) {

        }

        @Override
        public void update(Hex entity) {

        }

        @Override
        public void exit(Hex entity) {

        }

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    AI_ACTIVATED(){
        @Override
        public void enter(Hex entity) {

        }

        @Override
        public void update(Hex entity) {

        }

        @Override
        public void exit(Hex entity) {

        }

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    AI_DEACTIVATED(){
        @Override
        public void enter(Hex entity) {

        }

        @Override
        public void update(Hex entity) {

        }

        @Override
        public void exit(Hex entity) {

        }

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    }
}
