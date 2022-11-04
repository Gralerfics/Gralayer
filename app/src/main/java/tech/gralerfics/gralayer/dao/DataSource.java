package tech.gralerfics.gralayer.dao;

import android.content.Context;

import java.util.ArrayList;

import tech.gralerfics.gralayer.R;
import tech.gralerfics.gralayer.pojo.MusicInfo;

public class DataSource {
    public Context context;

    ArrayList<MusicInfo> contents = new ArrayList<>();

    public DataSource(Context context) {
        this.context = context;

        contents.add(new MusicInfo("月笹舟", "mp3_1", R.drawable.mp3_1));
        contents.add(new MusicInfo("富士山下", "mp3_2", R.drawable.mp3_2));
        contents.add(new MusicInfo("疯狂的朋友", "mp3_3", R.drawable.mp3_3));
        contents.add(new MusicInfo("寂静之声", "mp3_4", R.drawable.mp3_4));
        contents.add(new MusicInfo("三年幻想郷", "mp3_5", R.drawable.mp3_5));
        contents.add(new MusicInfo("爱的纪念", "flac_3", R.drawable.flac_3456));
        contents.add(new MusicInfo("梦中的婚礼", "flac_4", R.drawable.flac_3456));
        contents.add(new MusicInfo("秋日的私语", "flac_5", R.drawable.flac_3456));
        contents.add(new MusicInfo("水边的阿狄丽娜", "flac_6", R.drawable.flac_3456));
    }

    public ArrayList<MusicInfo> getItemNameList() {
        return contents;
    }
}
