package pl.klolo.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.typesafe.config.Config;
import pl.klolo.game.engine.GameEngine;
import pl.klolo.game.configuration.Profile;

import static pl.klolo.game.engine.GameEngineFactoryKt.createGameEngine;

public class DesktopLauncher {

    public static void main(String[] arg) {
        GameEngine gameEngine = createGameEngine(Profile.DESKTOP);

        LwjglApplicationConfiguration appConfig = new LwjglApplicationConfiguration();
        Config applicationConfigFromFile = gameEngine.getConfig("application");
        appConfig.title = applicationConfigFromFile.getString("title");
        appConfig.width = applicationConfigFromFile.getInt("width");
        appConfig.height = applicationConfigFromFile.getInt("height");
        appConfig.fullscreen = applicationConfigFromFile.getBoolean("fullscreen");

        new LwjglApplication(gameEngine, appConfig);
    }

}
