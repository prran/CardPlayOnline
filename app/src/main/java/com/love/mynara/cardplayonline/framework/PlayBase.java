package com.love.mynara.cardplayonline.framework;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class PlayBase {
    private LoadBase data;
    private SoundPool soundPlayer;
    private MediaPlayer musicPlayer;
    private boolean[] switcher;
    public static final int SOUND = 0;
    public static final int MOVIE = 1;

    private int onePlayKey = -1;

    public PlayBase(LoadBase data) {
        this.data = data;
        this.soundPlayer = data.getSoundPool();
        this.switcher = new boolean[]{true, true, true};
    }

    public void sound(final String fileName) {
        try {
            Integer e = (Integer)this.data.get(-2, fileName);
            if(e == null) {
                if(this.switcher[0]) {
                    this.musicPlayer = (MediaPlayer)this.data.get(2, fileName);
                    this.musicPlayer.setOnPreparedListener(new OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            PlayBase.this.switcher[1] = false;
                            mp.start();
                            Log.i("Log", "Playing " + fileName + " with MediaPlayer...");
                        }
                    });
                    this.switcher[0] = false;
                }

                if(this.switcher[1]) {
                    this.musicPlayer = (MediaPlayer)this.data.get(2, fileName);
                    this.musicPlayer.prepare();
                    switcher[1] = false;
                }

                if(this.musicPlayer.isPlaying() && this.switcher[2]) {
                    this.musicPlayer.setOnCompletionListener(new OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    this.switcher[2] = false;
                }
            } else {
                boolean streamID = true;

                int streamID1;
                do {
                    streamID1 = this.soundPlayer.play(e.intValue(), 1.0F, 1.0F, 2, 0, 1.0F);
                } while(streamID1 == 0);
            }
        } catch (IOException var4) {
            var4.printStackTrace();
            Log.e("Error!", "Fail to Play File");
        }

    }

    public void soundOnce(final String fileName, int key)
    {
        if(onePlayKey == key)
            return;
        else
            onePlayKey = key;

        try {
            Integer e = (Integer)this.data.get(-2, fileName);
            if(e == null) {
                if(this.switcher[0]) {
                    this.musicPlayer = (MediaPlayer)this.data.get(2, fileName);
                    this.musicPlayer.setOnPreparedListener(new OnPreparedListener() {
                        public void onPrepared(MediaPlayer mp) {
                            PlayBase.this.switcher[1] = false;
                            mp.start();
                            Log.i("Log", "Playing " + fileName + " with MediaPlayer...");
                        }
                    });
                    this.switcher[0] = false;
                }

                if(this.switcher[1]) {
                    this.musicPlayer = (MediaPlayer)this.data.get(2, fileName);
                    this.musicPlayer.prepare();
                    switcher[1] = false;
                }

                if(this.musicPlayer.isPlaying() && this.switcher[2]) {
                    this.musicPlayer.setOnCompletionListener(new OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            mp.start();
                        }
                    });
                    this.switcher[2] = false;
                }
            } else {
                boolean streamID = true;

                int streamID1;
                do {
                    streamID1 = this.soundPlayer.play(e.intValue(), 1.0F, 1.0F, 2, 0, 1.0F);
                } while(streamID1 == 0);
            }
        } catch (IOException var4) {
            var4.printStackTrace();
            Log.e("Error!", "Fail to Play File");
        }
    }

    public void done(int kindOfStatic) {
        switch(kindOfStatic) {
            case 0:
                this.musicPlayer.stop();
                this.musicPlayer.release();
                this.switcher[0] = true;
                this.switcher[1] = true;
                this.switcher[2] = true;
                return;
            case 1:
                return;
            default:
        }
    }

    public void releasePool() {
        this.soundPlayer.release();
    }
}
