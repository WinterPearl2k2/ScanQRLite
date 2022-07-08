package com.example.scanqrlite.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSuggestion;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ResultScan extends AppCompatActivity {
    ImageView btnBack, imgQR;
    Intent intent;
    TextView txtTitleActionbar, txtContent, txtTitle, txtBtnSave;
    TextView txtContentPass, txtContentSecurity;
    LinearLayout btnSave, btnURL, btnWifi, btnSaveBarcode;
    LinearLayout containerPass, containerSecurity;
    CardView btnCopy, btnSearch, btnShare;
    Bitmap bitmap;
    String content, S, P, T;
    WifiManager manager;
    private AdView adsViewResult;
    AdRequest adRequest;

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
        String type = intent.getStringExtra("type");
        ORM();
        CheckLayout(title, type);
        BackLayout();
        CoppyQR();
        SearchGoogle();
        ShareToOthersApp();
        SaveImage();
        GoToURL();
        ConnectToWifi();
        showAds();;
    }


    private void showAds() {
        adRequest = new AdRequest.Builder().build();
        adsViewResult.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if(adsViewResult != null)
            adsViewResult.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adsViewResult != null)
            adsViewResult.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adsViewResult != null)
            adsViewResult.resume();
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
//                    conf.preSharedKey = "\"" + P + "\"";
                    conf.status = WifiConfiguration.Status.DISABLED;
                    conf.priority = 40;
                    if(T.equals("nopass")) {
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        conf.allowedAuthAlgorithms.clear();
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                    } else if (T.equals("WEP")) {
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                        conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                        conf.wepKeys[0] = "\"".concat(P).concat("\"");
                    } else if (T.equals("WPA")) {
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                        conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                        conf.preSharedKey = "\"" + P + "\"";
                    }

                    int netWorkID = manager.addNetwork(conf);
                    manager.disconnect();
                    manager.enableNetwork(netWorkID, true);
                    manager.reconnect();
                    startActivity(wifi);
                } else {
                    if(intent.getStringExtra("P").length() != 0) {
                        WifiNetworkSuggestion.Builder wifiNetworkSuggestionBuilder1 = new WifiNetworkSuggestion.Builder();
                        wifiNetworkSuggestionBuilder1.setSsid(S);
                        wifiNetworkSuggestionBuilder1.setWpa2Passphrase(P);
                        WifiNetworkSuggestion wifiNetworkSuggestion = wifiNetworkSuggestionBuilder1.build();
                        List<WifiNetworkSuggestion> list = new ArrayList<>();
                        list.add(wifiNetworkSuggestion);
                        manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        manager.removeNetworkSuggestions(new ArrayList<WifiNetworkSuggestion>());
                        manager.addNetworkSuggestions(list);
                        copyTextToClipboard(P);
                    }
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
    OutputStream fos;

    private void SaveImage() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String mName = "Image-" + n + ".jpg";
                try {
                    if (ActivityCompat.checkSelfPermission(ResultScan.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(ResultScan.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {

                        OutputStream fos;
                        ContentResolver resolver = ResultScan.this.getContentResolver();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, mName);
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        fos = resolver.openOutputStream(imageUri);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        Objects.requireNonNull(fos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(ResultScan.this, getText(R.string.save_success), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && ActivityCompat.checkSelfPermission(ResultScan.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED)) {
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String mName = "Image-" + n + ".jpg";
            OutputStream fos;
            ContentResolver resolver = ResultScan.this.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, mName);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = resolver.openOutputStream(imageUri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Toast.makeText(ResultScan.this, getText(R.string.save_success), Toast.LENGTH_LONG).show();
        }
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

    private void Search() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.google.com/search?q=" +
                content));
        startActivity(intent);
    }

    private void SearchGoogle() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });
    }

    private void CoppyQR() {
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyTextToClipboard(content);
            }
        });
    }

    private void copyTextToClipboard(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", content);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(ResultScan.this, R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        doCopy();
    }

    public void doCopy(){
        SharedPreferences coppy ;
        coppy = getSharedPreferences("clipboard",0 );
        boolean check = coppy.getBoolean("clipboard",false);
        if(check==true){
            copyTextToClipboard(content);
        }
    }

    private void CheckLayout(String title, String type) {
        if(type.equals("QRcode")) {
            switch (title) {
                case "Text":
                    txtTitleActionbar.setText(R.string.title_text);
                    txtContent.setText(content);
                    txtTitle.setText(R.string.note);
                    btnSave.setVisibility(View.VISIBLE);
                    createQR(content);
                    break;
                case "Wifi":
                    txtTitleActionbar.setText(R.string.title_wifi);
                    txtTitle.setText(R.string.network_name);
                    btnSave.setVisibility(View.VISIBLE);
                    btnWifi.setVisibility(View.VISIBLE);
                    if(intent.getStringExtra("P").length() != 0) {
                        containerPass.setVisibility(View.VISIBLE);
                        P = intent.getStringExtra("P");
                        txtContentPass.setText(P);
                    }
                    containerSecurity.setVisibility(View.VISIBLE);
                    S = intent.getStringExtra("S");
                    T = intent.getStringExtra("T");
                    txtContent.setText(S);
                    txtContentSecurity.setText(T);
                    createQR(content );
                    break;
                case "URL":
                    txtTitleActionbar.setText(R.string.url);
                    txtContent.setText(content);
                    txtTitle.setText(R.string.url_result);
                    btnSave.setVisibility(View.VISIBLE);
                    btnURL.setVisibility(View.VISIBLE);
                    createQR(content);
                    break;
                default:
                    txtTitleActionbar .setText(R.string.title_text);
                    txtContent.setText(content);
                    txtTitle.setText(R.string.note);
                    btnSave.setVisibility(View.VISIBLE);
                    createQR(content);
                    break;
            }
        } else if(type.equals("Barcode")){
            switch (title) {
                case "Product":
                    txtTitleActionbar.setText(R.string.title_product);
                    txtTitle.setText(R.string.product);
                    break;
                case "Text":
                    txtTitleActionbar.setText(R.string.title_text);
                    txtTitle.setText(R.string.note);
                    break;
            }
            txtContent.setText(content);
            btnSave.setVisibility(View.VISIBLE);
            createQR(content);
            txtBtnSave.setText(R.string.save_barcode);
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
        int sizeWidth = 600;
        int sizeHeight = 264;

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = null;
        String type = intent.getStringExtra("type_barcode");
        switch (type) {
            case "QRcode":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth, hints);
                break;
            case "EAN_13":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.EAN_13, sizeWidth, sizeHeight, hints);
                break;
            case "EAN_8":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.EAN_8, sizeWidth, sizeHeight, hints);
                break;
            case "UPC_A":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.UPC_A, sizeWidth, sizeHeight, hints);
                break;
            case "UPC_E":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.UPC_E, sizeWidth, sizeHeight, hints);
                break;
            case "Code_128":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, sizeWidth, sizeHeight, hints);
                break;
            case "Code_2of5":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.ITF, sizeWidth, sizeHeight, hints);
                break;
            case "Code_39":
                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_39, sizeWidth, sizeHeight, hints);
                break;
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
//        //
//        HistoryCreateItem createItem = new HistoryCreateItem(txtTitle.toString(), txtContent.toString(), "10/11/2002", R.drawable.ic_qr_code );
//        CreateDatabase.getInstance(this).createItemDAO().insertItem(createItem);
//        //

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
        txtBtnSave = findViewById(R.id.txt_btn_save);
        adsViewResult = findViewById(R.id.adsViewResult);
    }
}