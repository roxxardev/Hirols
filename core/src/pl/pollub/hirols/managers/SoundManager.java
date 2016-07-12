package pl.pollub.hirols.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;


/**
 * Created by Eryk on 2015-12-02.
 */
public class SoundManager {

    private Music music;
    private AssetManager assetManager;
    public SoundManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public void loadMusic(String filePath) {
        music = assetManager.get(filePath,Music.class);
    }

    public void configureMusic(float volume, boolean looping, float pan, float position) {
        music.setLooping(looping);
        music.setVolume(volume);
        music.setPan(pan,volume);
        music.setPosition(position);
    }

    public void playMusic() {
        if(music !=null) {
         //   music.play();
        }
    }

    public void pauseMusic() {
        if(music!=null) {
            music.pause();
        }
    }

    public void stopMusic() {
        if(music!=null) {
            music.stop();
        }
    }

    public void dispose() {
        music.dispose();
    }

    public boolean isMusicPlaying() {
        return music.isPlaying();
    }

    public float getMusicPostion() {
        return music.getPosition();
    }

    public float getMusicVolume() {
        return music.getVolume();
    }

    public boolean isMusicLooping() {
        return music.isLooping();
    }

}
