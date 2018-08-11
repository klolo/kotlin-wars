package pl.klolo.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.typesafe.config.Config;
import org.jetbrains.annotations.NotNull;
import pl.klolo.game.GameLauncher;

public class DesktopLauncher {
    public static void main(String[] arg) {
        new LwjglApplication(new GameLauncher(), getLwjglApplicationConfiguration());
    }

    @NotNull
    private static LwjglApplicationConfiguration getLwjglApplicationConfiguration() {
        LwjglApplicationConfiguration lwjglApplicationConfiguration = new LwjglApplicationConfiguration();
        Config applicationConfigFromFile = GameLauncher.Companion.getConfig("application");
        lwjglApplicationConfiguration.title =  applicationConfigFromFile.getString("title");
        lwjglApplicationConfiguration.width = applicationConfigFromFile.getInt("width");
        lwjglApplicationConfiguration.height = applicationConfigFromFile.getInt("height");
        return lwjglApplicationConfiguration;
    }
}
