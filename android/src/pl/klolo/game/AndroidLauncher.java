package pl.klolo.game;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import pl.klolo.game.configuration.Profile;

import static pl.klolo.game.engine.GameEngineFactoryKt.createGameEngine;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(createGameEngine(Profile.ANDROID), config);
	}
}
