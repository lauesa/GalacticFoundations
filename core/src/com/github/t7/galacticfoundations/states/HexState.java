package com.github.t7.galacticfoundations.states;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.github.t7.galacticfoundations.actors.Hex;

public enum HexState implements State<Hex> {
    UNOWNED(){
        @Override
        public void enter(Hex entity) {
            entity.enterUnowned();
        }

        @Override
        public void update(Hex entity) {}

        @Override
        public void exit(Hex entity) {}

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    PLAYER_ACTIVE(){
        @Override
        public void enter(Hex entity) {
            entity.enterPlayerActivated();
        }

        @Override
        public void update(Hex entity) {}

        @Override
        public void exit(Hex entity) {}

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    PLAYER_INACTIVE(){
        @Override
        public void enter(Hex entity) {
            entity.enterPlayerDeactivated();
        }

        @Override
        public void update(Hex entity) {}

        @Override
        public void exit(Hex entity) {}

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    AI_ACTIVE(){
        @Override
        public void enter(Hex entity) {
            entity.enterAiActivated();
        }

        @Override
        public void update(Hex entity) {}

        @Override
        public void exit(Hex entity) {}

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    },
    AI_INACTIVE(){
        @Override
        public void enter(Hex entity) {
            entity.enterAiDeactivated();
        }

        @Override
        public void update(Hex entity) {}

        @Override
        public void exit(Hex entity) {}

        @Override
        public boolean onMessage(Hex entity, Telegram telegram) {
            return false;
        }
    }
}
