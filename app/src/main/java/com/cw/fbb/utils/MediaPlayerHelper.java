package com.cw.fbb.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerHelper {

    public static MediaPlayer mediaPlayer = null;

    public static void payMedia(Context context, int id) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(context, id);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
