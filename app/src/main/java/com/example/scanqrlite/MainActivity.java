package com.example.scanqrlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.example.scanqrlite.adapter.MenuAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private static ViewPager2 viewPager2;
    MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ORM(); // Ánh xạ
        SetUpViewPager();
        EventButtonNavigation();
    }

    private void EventButtonNavigation() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Scan:
                        viewPager2.setCurrentItem(0, false);
                        break;
                    case R.id.Create:
                        viewPager2.setCurrentItem(1, false);
                        break;
                    case R.id.History:
                        viewPager2.setCurrentItem(2, false);
                        break;
                    case R.id.Setting:
                        viewPager2.setCurrentItem(3, false);
                        break;
                }
                return false;
            }
        });
    }

    private void SetUpViewPager() {
        menuAdapter = new MenuAdapter(this);
        viewPager2.setAdapter(menuAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.Scan).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.Create).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.History).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.Setting).setChecked(true);
                        break;
                    default:
                        navigationView.getMenu().findItem(R.id.Scan).setChecked(true);
                        break;
                }
            }
        });
    }

    private void ORM() {
        navigationView = findViewById(R.id.btnNavigation);
        viewPager2 = findViewById(R.id.vPager);
    }
}