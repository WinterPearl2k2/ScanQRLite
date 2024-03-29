package com.example.scanqrlite.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanqrlite.DateTime;
import com.example.scanqrlite.Language;
import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.HistoryScanItem;
import com.example.scanqrlite.history.History_Menu.database.ScanDatabase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scan extends Fragment {
    public static final int GET_FROM_GALLERY =3;
    public boolean beep = true , vibrate = true;
    private ListenableFuture <ProcessCameraProvider> cameraProviderFuture;
    private ExecutorService cameraExecutor;
    private PreviewView previewView;
    private View view;
    private ImageAnalysis.Analyzer analyzer;
    private LinearLayout btnGallery, btnFlash;
    private ImageButton btnCloseTutorial;
    private boolean mFlash = false;
    private String title, typeScan, result, content, type;
    private boolean checkWifi = false;
    String security, SSID, password;
    private AdView mAdView;
    AdRequest adRequest;
    ImageView imgBtnGallery;
    TextView txtBtnGallery;
    boolean checkTutorial = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        ORM(); //Ánh xạ
        ScanByGallery();
        showAds();
        return view;
    }

    private void showAds() {
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void Tutorial() {
        SharedPreferences preferences = getActivity().getSharedPreferences("tutorial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        checkTutorial = preferences.getBoolean("tutorial", false);
        if(checkTutorial) {
            editor.putBoolean("tutorial", false);
            editor.apply();
            BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
            View view = getLayoutInflater().inflate(R.layout.tutorial, null);
            sheetDialog.setContentView(view);
            btnCloseTutorial = view.findViewById(R.id.btn_ok_tutorial);
            btnCloseTutorial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sheetDialog.dismiss();
                    checkTutorial = false;
                }
            });
            sheetDialog.show();

            sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    checkTutorial = false;
                }
            });
        }
    }

    private void ScanByGallery() {
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                btnGallery.setEnabled(false);
                startActivityForResult(gallery, 102);
            }
        });
    }

    private void FlashSwitch(Camera camera) {
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlash == false) {
                    camera.getCameraControl().enableTorch(!mFlash);
                    mFlash = true;
                    flashOptions(R.drawable.cus_btn_scan_checked, R.color.black, R.drawable.ic_flash_on);
                } else if(mFlash == true) {
                    camera.getCameraControl().enableTorch(!mFlash);
                    mFlash = false;
                    flashOptions(R.drawable.cus_btn_scan, R.color.white, R.drawable.ic_flash_off);
                }
            }
        });
    }

    private void flashOptions(int p, int p2, int p3) {
        btnFlash.setBackgroundResource(p);
        txtBtnGallery.setTextColor(getResources().getColor(p2));
        imgBtnGallery.setImageResource(p3);
    }

    private void ORM() {
        previewView = view.findViewById(R.id.cameraPreviewView);
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity());
        analyzer = new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                scanbarcode(image);
            }
        };
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 101);
                    }else {
                        ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                        bindpreview(processCameraProvider);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(requireActivity()));

        btnFlash = view.findViewById(R.id.btn_flash);
        btnGallery = view.findViewById(R.id.btn_gallery);
        mAdView = view.findViewById(R.id.adsViewScan);
        imgBtnGallery = view.findViewById(R.id.img_btn_gallery);
        txtBtnGallery = view.findViewById(R.id.txt_btn_gallery);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 102) {
            btnGallery.setEnabled(true);
            if(data == null || data.getData() == null) {
                Log.e("TAG", "The uri is null, probably the user Fcancelled the image selection process using the back button.");
                return;
            }

            Uri uri = data.getData();

            InputStream inputStream = null;
            try {
                inputStream = getActivity().getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
            scanbarcodegalerry(inputImage);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 101 && grantResults.length > 0) {
            ProcessCameraProvider processCameraProvider = null;
            try {
                processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bindpreview(processCameraProvider);
        }
    }

    private void bindpreview(ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        processCameraProvider.unbindAll();

        ImageCapture imageCapture = new ImageCapture.Builder().build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer);

        Camera camera = processCameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, imageCapture, imageAnalysis);

        FlashSwitch(camera);
    }


    private void scanbarcodegalerry(InputImage image) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_CODE_93,Barcode.FORMAT_EAN_8,Barcode.FORMAT_EAN_13
                        ,Barcode.FORMAT_CODE_39,Barcode.FORMAT_ALL_FORMATS
                        ,Barcode.FORMAT_CODABAR,Barcode.FORMAT_ITF,Barcode.FORMAT_UPC_A,Barcode.FORMAT_UPC_E)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        Language language = new Language(getContext());
                        language.Language();
                        if (!barcodes.isEmpty()) {
                            if (!readerBarcodeData(barcodes))
                                Toast.makeText(getContext(), getResources().getString(R.string.cant_scan), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), getResources().getString(R.string.cant_scan), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void scanbarcode(ImageProxy image) {
        @SuppressLint("UnsafeOptInUsageError") Image image1 = image.getImage();
        assert image1 != null;
        InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_CODE_93,Barcode.FORMAT_EAN_8,Barcode.FORMAT_EAN_13
                        ,Barcode.FORMAT_CODE_39,Barcode.FORMAT_ALL_FORMATS
                        ,Barcode.FORMAT_CODABAR,Barcode.FORMAT_ITF,Barcode.FORMAT_UPC_A,Barcode.FORMAT_UPC_E)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        if(!checkTutorial)
                            readerBarcodeData(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        // ...
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {
                        image.close();
                    }
                });
    }

    private boolean readerBarcodeData(List<Barcode> barcodes) {
        for (Barcode barcode: barcodes) {

            String rawValue = barcode.getRawValue();

            int valueType = barcode.getValueType();
            int bar = barcode.getFormat();

            Intent intent = new Intent(getActivity(), ResultScan.class);
            if(bar == barcode.FORMAT_QR_CODE) {
                switch (valueType) {
                    case Barcode.TYPE_WIFI:
                        SSID = barcode.getWifi().getSsid();
                        password = barcode.getWifi().getPassword();
                        int type = barcode.getWifi().getEncryptionType();
                        if (type == 1) security = "nopass";
                        else if (type == 2) security = "WPA";
                        else security = "WEP";
                        content = SSID;
                        result = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
                        title = "Wifi";

                        intent.putExtra("S", SSID);
                        intent.putExtra("P", password);
                        intent.putExtra("T", security);
                        checkWifi = true;
                        break;
                    case Barcode.TYPE_URL:
                        String url = barcode.getUrl().getUrl();
                        content = url;
                        result = url;
                        title = "URL";
                        break;
                    case Barcode.TYPE_TEXT:
                        String text = barcode.getDisplayValue();
                        content = text;
                        result = text;
                        title = "Text";
                        break;
                    default:
                        return false;
                }
                type = "QRcode";
                typeScan = "QRcode";
                intent.putExtra("type", type);
                intent.putExtra("type_barcode", typeScan);
                intent.putExtra("create_title", title);
                intent.putExtra("create_txt", result);
            } else {
                switch (bar) {
                    case Barcode.FORMAT_CODE_128:
                        typeScan = "Code_128";
                        title = getString(R.string.title_text);
                        break;
                    case Barcode.FORMAT_CODE_39:
                        typeScan = "Code_39";
                        title = getString(R.string.title_text);
                        break;
                    case Barcode.FORMAT_EAN_13:
                        typeScan = "EAN_13";
                        title = getString(R.string.title_product);
                        break;
                    case Barcode.FORMAT_EAN_8:
                        typeScan = "EAN_8";
                        title = getString(R.string.title_product);
                        break;
                    case Barcode.FORMAT_ITF:
                        typeScan = "Code_2of5";
                        title = getString(R.string.title_product);
                        break;
                    case Barcode.FORMAT_UPC_A:
                        typeScan = "UPC_A";
                        title = getString(R.string.title_product);
                        break;
                    case Barcode.FORMAT_UPC_E:
                        typeScan = "UPC_E";
                        title = getString(R.string.title_product);
                        break;
                    default:
                        return false;
                }
                type = "Barcode";
                intent.putExtra("type", type);
                intent.putExtra("type_barcode", typeScan);
                intent.putExtra("create_title", title);
                intent.putExtra("create_txt", rawValue);
                content = rawValue;
                result = rawValue;
            }


            //Coppy content to clipboard
            if(doCopy())
                copyTextToClipboard(rawValue);

            DateTime dateTime = new DateTime();
            HistoryScanItem historyScanItem = new HistoryScanItem(title, content, dateTime.getDateTime(), result);
            historyScanItem.setTypeScan(typeScan);
            if(checkWifi) {
                historyScanItem.setSecurity(security);
                historyScanItem.setPassword(password);
            }
            ScanDatabase.getInstance(getContext()).scanItemDAO().insertItem(historyScanItem);
            Vibrate();
            Beep();
            onPause();
            startActivity(intent);
            break;
        }
        return true;
    }

    public boolean doCopy(){
        SharedPreferences coppy ;
        coppy = getActivity().getSharedPreferences("clipboard",0 );
        boolean check = coppy.getBoolean("clipboard",false);
        if(check){
            return true;
        }
        return false;
    }

    private void copyTextToClipboard(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", content);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getContext(), R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    public void Beep(){
        SharedPreferences beep;
        beep = getContext().getSharedPreferences("beep",0);
        boolean check = beep.getBoolean("beep",false);
        if(check ==true){
            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 500);
            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
        }

    }
    public void Vibrate(){
        SharedPreferences vibrate;
        vibrate = getContext().getSharedPreferences("vibrate",0);
        boolean check = vibrate.getBoolean("vibrate", false);
        if(check == true){
            Viber(getContext(),"on");
        }
        else
            Viber(getContext(),"off");
    }

    @JavascriptInterface
    public void Viber(Context ct, String value){
        if(value.equals("on")) {
            Vibrator v = (Vibrator) ct.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
        }
    }

    public void onPause() {

        if (mAdView != null) {
            mAdView.pause();
        }

        super.onPause();

        ProcessCameraProvider processCameraProvider = null;
        try {
            processCameraProvider = cameraProviderFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        processCameraProvider.unbindAll();
        flashOptions(R.drawable.cus_btn_scan, R.color.white, R.drawable.ic_flash_off);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        ProcessCameraProvider processCameraProvider = null;
        try {
            processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bindpreview(processCameraProvider);
        Tutorial();
    }
}