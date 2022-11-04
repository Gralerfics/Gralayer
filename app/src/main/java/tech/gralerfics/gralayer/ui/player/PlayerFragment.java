package tech.gralerfics.gralayer.ui.player;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import tech.gralerfics.gralayer.MusicService;
import tech.gralerfics.gralayer.R;
import tech.gralerfics.gralayer.dao.DataSource;
import tech.gralerfics.gralayer.databinding.FragmentPlayerBinding;
import tech.gralerfics.gralayer.pojo.MusicInfo;

public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding binding;

    public static ImageView coverImg;
    public static SeekBar seekBar;
    public static TextView titleText, timeNowText, timeTotalText;
    public static Button playBtn, pauseBtn, prevBtn, nextBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        titleText = root.findViewById(R.id.Title);
        coverImg = root.findViewById(R.id.coverImg);
        seekBar = root.findViewById(R.id.seekBar);
        timeNowText = root.findViewById(R.id.timeCurrent);
        timeTotalText = root.findViewById(R.id.timeTotal);
        playBtn = root.findViewById(R.id.btnPlay);
        pauseBtn = root.findViewById(R.id.btnPause);
        prevBtn = root.findViewById(R.id.btnPrev);
        nextBtn = root.findViewById(R.id.btnNext);

        pauseBtn.setVisibility(View.INVISIBLE);

        animator = ObjectAnimator.ofFloat(coverImg, "rotation", 0.0f, 360.0f);
        animator.setDuration(20000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.start();
        animator.pause();

        Intent mIntent = new Intent(this.getActivity(), MusicService.class);
        this.getActivity().bindService(mIntent, connection, Context.BIND_AUTO_CREATE);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i == seekBar.getMax()) {
                    animator.pause();
                }
                if (controller != null) {
                    if (b) {
                        controller.seekTo(i);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (controller != null) {
                    controller.pausePlay();
                    animator.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (controller != null) {
                    controller.continuePlay();
                    animator.resume();
                }
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (controller != null && currentMusic != null) {
                    animator.resume();
                    controller.continuePlay();
                    playBtn.setVisibility(View.INVISIBLE);
                    pauseBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (controller != null && currentMusic != null) {
                    animator.pause();
                    controller.pausePlay();
                    playBtn.setVisibility(View.VISIBLE);
                    pauseBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller != null && currentMusic != null) {
                    DataSource dataSource = new DataSource(getActivity());
                    ArrayList<MusicInfo> list = dataSource.getItemNameList();
                    int pos = -1, i = 0;
                    for (MusicInfo m : list) {
                        if (m.filename.equals(currentMusic.filename)) {
                            pos = i;
                            break;
                        }
                        i ++;
                    }
                    if (pos < 0) return;
                    currentMusic = list.get((pos - 1 + list.size()) % list.size());
                    playSpecificMusic(currentMusic);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controller != null && currentMusic != null) {
                    DataSource dataSource = new DataSource(getActivity());
                    ArrayList<MusicInfo> list = dataSource.getItemNameList();
                    int pos = -1, i = 0;
                    for (MusicInfo m : list) {
                        if (m.filename.equals(currentMusic.filename)) {
                            pos = i;
                            break;
                        }
                        i ++;
                    }
                    if (pos < 0) return;
                    currentMusic = list.get((pos + 1) % list.size());
                    playSpecificMusic(currentMusic);
                }
            }
        });

        return root;
    }

    private static void playSpecificMusic(MusicInfo music) {
        titleText.setText(music.name);
        coverImg.setImageResource(music.coverId);
        playBtn.setVisibility(View.INVISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
        animator.start();

        if (controller != null) {
            controller.play(music.filename);
        }
    }

    public static Handler handlerFromList = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String name = bundle.getString("name");
            String fileName = bundle.getString("fileName");
            int coverId = bundle.getInt("coverId");
            currentMusic = new MusicInfo(name, fileName, coverId);
            playSpecificMusic(currentMusic);
        }
    };

    public static Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentDuration = bundle.getInt("currentDuration");

            seekBar.setMax(duration);
            seekBar.setProgress(currentDuration);

            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMin = minute + "";
            String strSec = second + "";
            if (minute < 10) strMin = "0" + strMin;
            if (second < 10) strSec = "0" + strSec;
            timeTotalText.setText(strMin + ":" + strSec);

            minute = currentDuration / 1000 / 60;
            second = currentDuration / 1000 % 60;
            strMin = minute + "";
            strSec = second + "";
            if (minute < 10) strMin = "0" + strMin;
            if (second < 10) strSec = "0" + strSec;
            timeNowText.setText(strMin + ":" + strSec);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        this.getActivity().unbindService(connection);
    }

    public static MusicInfo currentMusic = null;

    public static ObjectAnimator animator = null;

    public static MusicService.MusicController controller;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            controller = (MusicService.MusicController) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };
}
