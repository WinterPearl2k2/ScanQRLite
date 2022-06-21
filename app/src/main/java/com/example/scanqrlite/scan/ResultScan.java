package com.example.scanqrlite.scan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scanqrlite.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ResultScan extends AppCompatActivity {
    ImageView btnBack, imgQR;
    Intent intent;
    TextView txtTitle, txtContent;

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
        ORM();
        CheckLayout(title);
        BackLayout();
    }

    private void CheckLayout(String title) {
        String content = intent.getStringExtra("create_txt");
        if(title.equals("Text")) {
            txtTitle.setText("Text");
            txtContent.setText(content);
            createQR(content);
        } else if(title.equals("Wifi")) {
            txtTitle.setText("Wifi");
            txtContent.setText(content);
        } else if(title.equals("URL")) {
            txtTitle.setText("URL");
            txtContent.setText(content);
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
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
        txtTitle = findViewById(R.id.type_name_qr);
        txtContent = findViewById(R.id.content_5);
        imgQR = findViewById(R.id.image_text);
    }
}