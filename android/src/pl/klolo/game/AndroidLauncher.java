package pl.klolo.game;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import pl.klolo.game.engine.Profile;

import static pl.klolo.game.engine.GameEngineFactoryKt.createGameEngine;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = getAndroidConfiguration();
        initialize(createGameEngine(Profile.ANDROID), config);
    }

    private AndroidApplicationConfiguration getAndroidConfiguration() {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.useCompass = false;
        return config;
    }
}
