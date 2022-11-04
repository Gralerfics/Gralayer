package tech.gralerfics.gralayer;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import tech.gralerfics.gralayer.databinding.ActivityMainBinding;
import tech.gralerfics.gralayer.ui.player.PlayerFragment;
import tech.gralerfics.gralayer.ui.list.ListFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initViewPager();
        initBottomNavigationBar();
    }

    public static ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private void initViewPager() {
        viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;

                switch (position) {
                    case 0:
                        fragment = new PlayerFragment();
                        break;
                    case 1:
                        fragment = new ListFragment();
                        break;
                }

                assert fragment != null;
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initBottomNavigationBar() {
        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_player:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_list:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return false;
            }
        });
    }
}
