package pl.klolo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.typesafe.config.Config;
import pl.klolo.game.GameEngine;
import pl.klolo.game.configuration.Profile;

import static pl.klolo.game.GameEngineFactoryKt.createGameEngine;

public class DesktopLauncher {

    public static void main(String[] arg) {
        GameEngine gameEngine = createGameEngine(Profile.DESKTOP);

        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        Config applicationConfigFromFile = gameEngine.getConfig("application");
        lwjglApplicationConfiguration.title = applicationConfigFromFile.getString("title");
        lwjglApplicationConfiguration.width = applicationConfigFromFile.getInt("width");
        lwjglApplicationConfiguration.height = applicationConfigFromFile.getInt("height");
        lwjglApplicationConfiguration.fullscreen = applicationConfigFromFile.getBoolean("fullscreen");
        new LwjglApplication(gameEngine, lwjglApplicationConfiguration);
    }

}
