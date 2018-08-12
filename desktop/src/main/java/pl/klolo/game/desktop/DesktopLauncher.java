package pl.klolo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.typesafe.config.Config;
import pl.klolo.game.GameEngine;

import static pl.klolo.game.GameEngineFactoryKt.createGameEngine;

public class DesktopLauncher {

    public static void main(String[] arg) {
        GameEngine gameEngine = createGameEngine();

        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        Config applicationConfigFromFile = gameEngine.getConfig("application");
        lwjglApplicationConfiguration.title =  applicationConfigFromFile.getString("title");
        lwjglApplicationConfiguration.width = applicationConfigFromFile.getInt("width");
        lwjglApplicationConfiguration.height = applicationConfigFromFile.getInt("height");

        new LwjglApplication(gameEngine, lwjglApplicationConfiguration);
    }

}
