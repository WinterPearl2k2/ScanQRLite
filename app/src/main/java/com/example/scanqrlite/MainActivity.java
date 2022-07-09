package com.example.scanqrlite;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.scanqrlite.adapter.MenuAdapter;
import com.example.scanqrlite.create.Create;
import com.example.scanqrlite.history.History;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;
import com.example.scanqrlite.scan.Scan;
import com.example.scanqrlite.setting.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private static ViewPager2 viewPager2;
    MenuAdapter menuAdapter;
    RelativeLayout layout_menu, layout_permisson;
    Button btn_permisson;
    Language language;
    TextColorStatusbar textColorStatusbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        language = new Language(this);
        language.Language();
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ORM(); // Ánh xạ
        SetUpViewPager();
        EventButtonNavigation();
        PermissionCheck();
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
                language.Language();
                return false;
            }
        });
    }

    private void SetUpViewPager() {
        menuAdapter = new MenuAdapter(this);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setAdapter(menuAdapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int typeColor;
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.Scan).setChecked(true);
                        typeColor = 1;
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.Create).setChecked(true);
                        typeColor = 2;
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.History).setChecked(true);
                        typeColor = 2;
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.Setting).setChecked(true);
                        typeColor = 2;
                        break;
                    default:
                        navigationView.getMenu().findItem(R.id.Scan).setChecked(true);
                        typeColor = 1;
                        break;
                }
                textColorStatusbar.changeColor(typeColor);
                language.Language();
            }
        });
    }


    private void sendTutorial() {
        SharedPreferences.Editor preferences = getSharedPreferences("tutorial", MODE_PRIVATE).edit();
        preferences.putBoolean("tutorial", true);
        preferences.apply();
    }

    @Override
    protected void onStart() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                layout_menu.setVisibility(View.GONE);
                layout_permisson.setVisibility(View.VISIBLE);
            } else {
                layout_menu.setVisibility(View.VISIBLE);
                layout_permisson.setVisibility(View.GONE);
            }
        }

        super.onStart();
    }

    private void PermissionCheck() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
////                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
//            } else {
//                layout_menu.setVisibility(View.VISIBLE);
//                layout_permisson.setVisibility(View.GONE);
//            }
//        }
        btn_permisson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Permission();
            }
        });
    }

    private void Permission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if(getFromPref(this, "ALLOWED")) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, 200);
                } else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[] {Manifest.permission.CAMERA}, 101);
                }
            } else {
                layout_menu.setVisibility(View.VISIBLE);
                layout_permisson.setVisibility(View.GONE);
                sendTutorial();
            }
        } else {
            layout_menu.setVisibility(View.VISIBLE);
            layout_permisson.setVisibility(View.GONE);
            sendTutorial();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            layout_menu.setVisibility(View.VISIBLE);
            layout_permisson.setVisibility(View.GONE);
            sendTutorial();
        } else if(requestCode == 101 && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            layout_menu.setVisibility(View.VISIBLE);
            layout_permisson.setVisibility(View.GONE);
            sendTutorial();
        }
    }

    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences mPrefs = context.getSharedPreferences("camera_pref", MODE_PRIVATE);
        SharedPreferences.Editor prefersEditor = mPrefs.edit();
        prefersEditor.putBoolean(key, allowed);
        prefersEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences mPrefs = context.getSharedPreferences("camera_pref", MODE_PRIVATE);
        return (mPrefs.getBoolean(key, false));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    layout_menu.setVisibility(View.VISIBLE);
                    layout_permisson.setVisibility(View.GONE);
                    sendTutorial();
                    break;
                }
                String permission = permissions[0];
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                    if (showRationale) {
                        //Nhap cai gi do
                    } else if (!showRationale) {
                        saveToPreferences(this, "ALLOWED", true);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void ORM() {
        navigationView = findViewById(R.id.btnNavigation);
        viewPager2 = findViewById(R.id.vPager);
        layout_menu = findViewById(R.id.layout_menu);
        layout_permisson = findViewById(R.id.layout_permission);
        btn_permisson = findViewById(R.id.btn_permisson);
        textColorStatusbar = new TextColorStatusbar(MainActivity.this);
    }
}