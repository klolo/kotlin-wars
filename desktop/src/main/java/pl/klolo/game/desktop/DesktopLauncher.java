package pl.klolo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.typesafe.config.Config;
import pl.klolo.game.engine.Profile;
import pl.klolo.game.engine.GameEngine;

import static pl.klolo.game.engine.GameEngineFactoryKt.createGameEngine;

public class DesktopLauncher {

    public static void main(String[] arg) {
        final GameEngine gameEngine = createGameEngine(Profile.DESKTOP);

        LwjglApplicationConfiguration appConfig = new LwjglApplicationConfiguration();
        Config applicationConfigFromFile = gameEngine.getConfig("application");
        appConfig.title = applicationConfigFromFile.getString("title");
        appConfig.width = applicationConfigFromFile.getInt("width");
        appConfig.height = applicationConfigFromFile.getInt("height");
        appConfig.fullscreen = applicationConfigFromFile.getBoolean("fullscreen");

        new LwjglApplication(gameEngine, appConfig);
    }

}
