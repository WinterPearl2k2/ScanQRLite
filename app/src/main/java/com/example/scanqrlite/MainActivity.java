package com.example.scanqrlite;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.scanqrlite.adapter.MenuAdapter;
import com.example.scanqrlite.create.Create;
import com.example.scanqrlite.history.History;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;
import com.example.scanqrlite.scan.Scan;
import com.example.scanqrlite.setting.Setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigationView;
    private static ViewPager2 viewPager2;
    MenuAdapter menuAdapter;
    RelativeLayout layout_menu, layout_permisson;
    Button btn_permisson;
    ImageButton btnCloseTutorial;
    private FrameLayout mainFragment;

    Fragment scanFragment = new Scan();
    Fragment createFragment = new Create();
    Fragment historyFragment = new History();
    Fragment settingFragment = new Setting();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ORM(); // Ánh xạ
        Layout();
        PermissionCheck();
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

//    @Override
//    protected void onResume() {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    != PackageManager.PERMISSION_GRANTED) {
////                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
//                layout_menu.setVisibility(View.GONE);
//                layout_permisson.setVisibility(View.VISIBLE);
//                onRestart();
//            } else {
//                layout_menu.setVisibility(View.VISIBLE);
//                layout_permisson.setVisibility(View.GONE);
//            }
//        }
//        super.onResume();
//    }

    private void Layout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, scanFragment).commit();
        navigationView.setSelectedItemId(R.id.Scan);

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.Scan:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, scanFragment).commit();
                    return true;
                case R.id.Create:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, createFragment).commit();
                    return true;
                case R.id.History:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, historyFragment).commit();
                    return true;
                case R.id.Setting:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, settingFragment).commit();
                    return true;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame, scanFragment).commit();
                    return true;
            }
        });

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
        layout_menu = findViewById(R.id.layout_menu);
        layout_permisson = findViewById(R.id.layout_permission);
        btn_permisson = findViewById(R.id.btn_permisson);
    }
}