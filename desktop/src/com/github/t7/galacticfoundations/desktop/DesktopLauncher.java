package com.github.t7.galacticfoundations.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.t7.galacticfoundations.galacticfoundations;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = galacticfoundations.WIDTH;
		config.height = galacticfoundations.HEIGHT;
		config.title = galacticfoundations.TITLE;
		new LwjglApplication(new galacticfoundations(), config);
	}
}
