package tech.gralerfics.gralayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

import tech.gralerfics.gralayer.ui.player.PlayerFragment;

public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;

    public MusicService() {}

    public void addTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (player != null) {
                        int duration = player.getDuration();
                        int currentPosition = player.getCurrentPosition();
                        Message message = PlayerFragment.handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentDuration", currentPosition);
                        message.setData(bundle);
                        PlayerFragment.handler.sendMessage(message);
                    }
                }
            };
            timer.schedule(task, 10, 500);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicController();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying()) player.stop();
            player.release();
            player = null;
        }
    }

    public class MusicController extends Binder {
        public void play(String file_name) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + file_name);
            try {
                player.reset();
                player = MediaPlayer.create(getApplicationContext(), uri);
                player.start();
                addTimer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }

        public void pausePlay() {
            player.pause();
        }

        public void continuePlay() {
            player.start();
        }

        public void seekTo(int progress) {
            player.seekTo(progress);
        }
    }
}