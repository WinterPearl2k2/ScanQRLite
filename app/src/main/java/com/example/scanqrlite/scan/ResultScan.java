package com.example.scanqrlite.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanqrlite.MainActivity;
import com.example.scanqrlite.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResultScan extends AppCompatActivity {
    ImageView btnBack, imgQR;
    Intent intent;
    TextView txtTitleActionbar, txtContent, txtTitle;
    TextView txtContentPass, txtContentSecurity;
    LinearLayout btnSave, btnURL, btnWifi, btnSaveBarcode;
    LinearLayout containerPass, containerSecurity;
    CardView btnCopy, btnSearch, btnShare;
    Bitmap bitmap;
    String content, S, P, T;
    WifiManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.color_2));
        }
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_result_scan);
        setContentView(R.layout.fragment_result_generator);
        intent = getIntent();
        String title = intent.getStringExtra("create_title");
        content = intent.getStringExtra("create_txt");
        ORM();
        CheckLayout(title);
        BackLayout();
        CoppyToClipBoard();
        SearchGoogle();
        ShareToOthersApp();
        SaveImage();
        GoToURL();
        ConnectToWifi();
    }

    private void ConnectToWifi() {
        Intent wifi = new Intent(Settings.ACTION_WIFI_SETTINGS);
        manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        ConnectivityManager.NetworkCallback networkCallback;
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    WifiConfiguration conf = new WifiConfiguration();
                    conf.SSID = "\"" + S +"\"";
                    conf.preSharedKey = "\"" + P + "\"";

                    int netWorkID = manager.addNetwork(conf);
                    manager.disconnect();
                    manager.enableNetwork(netWorkID, true);
                    manager.reconnect();
                    startActivity(wifi);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    WifiNetworkSuggestion.Builder wifiNetworkSuggestionBuilder1 = new WifiNetworkSuggestion.Builder();
                    wifiNetworkSuggestionBuilder1.setSsid(S);
                    wifiNetworkSuggestionBuilder1.setWpa2Passphrase(P);
                    WifiNetworkSuggestion wifiNetworkSuggestion = wifiNetworkSuggestionBuilder1.build();
                    List<WifiNetworkSuggestion> list = new ArrayList<>();
                    list.add(wifiNetworkSuggestion);
                    manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    manager.removeNetworkSuggestions(new ArrayList<WifiNetworkSuggestion>());
                    manager.addNetworkSuggestions(list);
                    startActivity(new Intent("android.settings.panel.action.INTERNET_CONNECTIVITY"));
                } else {
                    Toast.makeText(ResultScan.this, "Hihi", Toast.LENGTH_SHORT).show();
                    final WifiNetworkSpecifier specifier =
                            new WifiNetworkSpecifier.Builder()
                                    .setSsid(S)
                                    .build();

                    final NetworkRequest request =
                            new NetworkRequest.Builder()
                                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                                    .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                                    .setNetworkSpecifier(specifier)
                                    .build();



                    ConnectivityManager connectivityManager = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);

                    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(@NonNull Network network) {
                            connectivityManager.bindProcessToNetwork(network);
                            super.onAvailable(network);
                        }
                    };
                    startActivity(new Intent("android.settings.panel.action.INTERNET_CONNECTIVITY"));
                }

            }
        });
    }

    private void GoToURL() {
        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if(content.startsWith("http://") || content.startsWith("https://")) {
                    intent.setData(Uri.parse(content));
                } else {
                    intent.setData(Uri.parse("https://www.google.com/search?q=" +
                            content));
                }
                startActivity(intent);
            }
        });
    }

    private void SaveImage() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String root = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString();
                File mFile = new File(root + "/save_images");
                mFile.mkdirs();

                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String mName = "Image-" + n + ".jpg";
                File file = new File(mFile, mName);
                if(file.exists()) file.delete();

                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                MediaScannerConnection.scanFile(ResultScan.this, new String[]{file.toString()}, null,
//                        new MediaScannerConnection.OnScanCompletedListener() {
//                            @Override
//                            public void onScanCompleted(String path, Uri uri) {
//                                Log.i("ExternalStorage", "Scanned " + path + ":");
//                                Log.i("ExternalStorage", "-> uri=" + uri);
//                            }
//                        });
                Toast.makeText(ResultScan.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShareToOthersApp() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }

    private void SearchGoogle() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.google.com/search?q=" +
                        content));
                startActivity(intent);
            }
        });
    }

    private void CoppyToClipBoard() {
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", content);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(ResultScan.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CheckLayout(String title) {
        if(title.equals("Text")) {
            txtTitleActionbar.setText("Text");
            txtContent.setText(content);
            txtTitle.setText("Note:");
            btnSave.setVisibility(View.VISIBLE);
            createQR(content);
        } else if(title.equals("Wifi")) {
            txtTitleActionbar.setText("Wifi");
            txtTitle.setText("Network name:");
            btnSave.setVisibility(View.VISIBLE);
            btnWifi.setVisibility(View.VISIBLE);
            containerPass.setVisibility(View.VISIBLE);
            containerSecurity.setVisibility(View.VISIBLE);
            S = intent.getStringExtra("S");
            P = intent.getStringExtra("P");
            T = intent.getStringExtra("T");
            txtContent.setText(S);
            txtContentPass.setText(P);
            txtContentSecurity.setText(T);
            createQR(content);
        } else if(title.equals("URL")) {
            txtTitleActionbar.setText("URL");
            txtContent.setText(content);
            txtTitle.setText("URL:");
            btnSave.setVisibility(View.VISIBLE);
            btnURL.setVisibility(View.VISIBLE);
            createQR(content);
        }
    }

    private void createQR(String content) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        try {
            imgQR.setImageBitmap(CreateImage(content));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    private Bitmap CreateImage(String content) throws WriterException {
        int sizeWidth = 660;
        int sizeHeight = 264;
        BitMatrix matrix = null;
        String type = intent.getStringExtra("type");
        switch (type) {
            case "QRcode":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth);
                break;
//            default:
//                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth);
//                break;
        }

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixel = new int[width * height];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if (matrix.get(j, i))
                    pixel[i * width + j] = 0xff000000;
                else
                    pixel[i * width + j] = 0xffffffff;
            }
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixel, 0, width, 0 , 0, width, height);
        return bitmap;
    }

    private void BackLayout() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ORM() {
        btnBack = findViewById(R.id.btn_back_layout);
        txtTitleActionbar = findViewById(R.id.type_name_qr);
        txtContent = findViewById(R.id.content_5);
        imgQR = findViewById(R.id.image_text);
        btnSave = findViewById(R.id.btn_save);
        btnURL = findViewById(R.id.btn_goto_url);
        btnWifi = findViewById(R.id.btn_connect_wifi);
        btnSaveBarcode = findViewById(R.id.btn_save_barcode);
        btnCopy = findViewById(R.id.btn_copy);
        btnSearch = findViewById(R.id.btn_search);
        btnShare = findViewById(R.id.btn_share);
        txtContentPass = findViewById(R.id.content_pass);
        txtContentSecurity = findViewById(R.id.content_security);
        containerPass = findViewById(R.id.container_content_pass);
        containerSecurity = findViewById(R.id.container_content_security);
        txtTitle = findViewById(R.id.title_5);
    }
}