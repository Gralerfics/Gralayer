package tech.gralerfics.gralayer.adapter;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tech.gralerfics.gralayer.MainActivity;
import tech.gralerfics.gralayer.R;
import tech.gralerfics.gralayer.pojo.MusicInfo;
import tech.gralerfics.gralayer.ui.player.PlayerFragment;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    public ArrayList<MusicInfo> itemInfo = null;

    public RecyclerViewAdapter(ArrayList<MusicInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    @Override
    public int getItemCount() {
        return itemInfo.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.imageView.setBackgroundResource(itemInfo.get(position).coverId);
        holder.textView.setText(itemInfo.get(position).name);
        holder.fileText.setText(itemInfo.get(position).filename);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = itemInfo.get(position).name;
                String fileName = itemInfo.get(position).filename;
                int coverId = itemInfo.get(position).coverId;

                Message msg = PlayerFragment.handlerFromList.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("fileName", fileName);
                bundle.putInt("coverId", coverId);
                msg.setData(bundle);
                PlayerFragment.handlerFromList.sendMessage(msg);

                MainActivity.viewPager.setCurrentItem(0);
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.music_item, null);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView = null, fileText = null;
        public ImageView imageView = null;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.music_text);
            fileText = (TextView) view.findViewById(R.id.music_file);
            imageView = (ImageView) view.findViewById(R.id.music_image);
        }
    }

    public static class MyViewDeco extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, 5);
        }
    }
}
