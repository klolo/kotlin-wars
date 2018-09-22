package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.PlaySound
import pl.klolo.game.event.StopMusic

enum class Song(val filename: String) {
    MENU("assets/sound/bensound-littleplanet.mp3"),
    GAME("assets/sound/bensound-extremeaction.mp3")
}

enum class SoundEffect(val filename: String) {
    PLAYER_SHOOT("assets/sound/laser-shot-silenced.wav"),
    SHIELD_COLLISION("assets/sound/collision.ogg"),
    FOUND_BONUS("assets/sound/bonus.wav"),
    YIPEE("assets/sound/yipee.wav"),
    PLAYER_COLLISION("assets/sound/playerCollision.wav"),
    DESTROY_PLAYER("assets/sound/destroy.wav")
}

class SoundManager(private val eventProcessor: EventProcessor) {
    private var currentMusic: Music? = null

    private var musicVolume = GameEngine.applicationConfiguration.getConfig("sound")
            .getDouble("musicVolume")
            .toFloat()

    private var isSoundEnable = GameEngine.applicationConfiguration.getConfig("sound")
            .getBoolean("enable")

    private val sounds: Map<SoundEffect, Sound> by lazy {
        SoundEffect.values().map { it to assetManager.get(it.filename, Sound::class.java) }.toMap()
    }

    fun initialize() {
        Gdx.app.debug(this.javaClass.name, "createSubscription")

        if (!isSoundEnable) {
            return
        }

        eventProcessor
                .subscribe(-2)
                .onEvent(PlaySound::class) {
                    sounds[it.soundEffect]?.play()
                }
                .onEvent(StopMusic) {
                    currentMusic?.stop()
                }
    }

    fun playSong(song: Song) {
        if (!isSoundEnable) {
            return
        }

        currentMusic?.stop()
        currentMusic = assetManager.get(song.filename, Music::class.java)
        currentMusic?.play()
        currentMusic?.isLooping = true;
        currentMusic?.volume = musicVolume
    }
}