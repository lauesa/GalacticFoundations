package com.github.t7.galacticfoundations.activities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class ActivityManager {
    private Stack<Activity> activities;

    public ActivityManager(){
        activities = new Stack<Activity>();
    }

    public void push(Activity activity){activities.push(activity);}

    public void pop(){activities.pop().dispose();}

    public void set(Activity state){
        activities.pop().dispose();
        activities.push(state);
    }

    public Activity peek(){return activities.peek();}

    public void render(SpriteBatch sb){activities.peek().render(sb);}
}
