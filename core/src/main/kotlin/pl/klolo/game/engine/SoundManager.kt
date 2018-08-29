package pl.klolo.game.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import pl.klolo.game.event.EventProcessor
import pl.klolo.game.event.PlaySound

enum class Song(val filename: String) {
    MENU("sound/bensound-littleplanet.mp3"),
    GAME("sound/bensound-extremeaction.mp3")
}

enum class SoundEffect(val filename: String) {
    PLAYER_SHOOT("sound/laser-shot-silenced.wav"),
}

class SoundManager(private val eventProcessor: EventProcessor) {
    private var currentMusic: Music? = null

    private val sounds: Map<SoundEffect, Sound> by lazy {
        mapOf<SoundEffect, Sound>(
                SoundEffect.PLAYER_SHOOT to Gdx.audio.newSound(Gdx.files.internal(SoundEffect.PLAYER_SHOOT.filename))
        )
    }

    fun initialize() {
        println("SoundManager creating...")

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