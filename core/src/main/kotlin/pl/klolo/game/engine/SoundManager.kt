package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.PlaySound

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

    private val sounds: Map<SoundEffect, Sound> by lazy {
        SoundEffect.values().map { it to Gdx.audio.newSound(Gdx.files.internal(it.filename)) }.toMap()
    }

    fun initialize() {
        Gdx.app.debug(this.javaClass.name,"createSubscription")

        eventProcessor
                .subscribe(-2)
                .onEvent(PlaySound::class.java) {
                    sounds[it.soundEffect]?.play()
                }
    }

    fun playSong(song: Song) {
        currentMusic?.stop()
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(song.filename))
        currentMusic?.play()
        currentMusic?.isLooping = true;
    }
}