package tech.gralerfics.gralayer.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import tech.gralerfics.gralayer.R;
import tech.gralerfics.gralayer.adapter.RecyclerViewAdapter;
import tech.gralerfics.gralayer.dao.DataSource;
import tech.gralerfics.gralayer.databinding.FragmentListBinding;
import tech.gralerfics.gralayer.pojo.MusicInfo;

public class ListFragment extends Fragment {
    private RecyclerView recyclerView;

    private FragmentListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DataSource dataSource = new DataSource(this.getActivity());
        ArrayList<MusicInfo> itemInfo = dataSource.getItemNameList();
        recyclerView = root.findViewById(R.id.listRecyclerView);
        recyclerView.setAdapter(new RecyclerViewAdapter(itemInfo));
        recyclerView.addItemDecoration(new RecyclerViewAdapter.MyViewDeco());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
