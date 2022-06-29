package com.example.scanqrlite.scan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.widget.ImageButton;

import com.example.scanqrlite.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

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
    private ImageButton  btnGallery;
    private ImageButton btnFlash, btnPhoto_Library;
    private boolean mFlash = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        ORM(); //Ánh xạ
        ScanByGallery();
        //CopyToClipboard();
        return view;
    }

    private void ScanByGallery() {
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setType("image/*");
                startActivityForResult(gallery, 102);
            }
        });
        previewView=view.findViewById(R.id.cameraPreviewView);


        btnFlash=view.findViewById(R.id.btn_flash);
        btnPhoto_Library=view.findViewById(R.id.btn_gallery);
        btnPhoto_Library.setOnClickListener(view1 -> startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));
        cameraExecutor=Executors.newSingleThreadExecutor();
        cameraProviderFuture=ProcessCameraProvider.getInstance(requireActivity());
    }

    private void FlashSwitch(Camera camera) {
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFlash == false) {
                    camera.getCameraControl().enableTorch(!mFlash);
                    mFlash = true;
                    btnFlash.setImageResource(R.drawable.btn_flah_on);
                } else if(mFlash == true) {
                    camera.getCameraControl().enableTorch(!mFlash);
                    mFlash = false;
                    btnFlash.setImageResource(R.drawable.btn_flah_off);
                }
            }
        });
    }

    private void ORM() {
        previewView = view.findViewById(R.id.cameraPreviewView);
//        getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

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
                        readerBarcodeData(barcodes);
                        // Task completed successfully
                        // ...
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

    private void readerBarcodeData(List<Barcode> barcodes) {
        for (Barcode barcode: barcodes) {
            Rect bounds = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();

            String rawValue = barcode.getRawValue();

            int valueType = barcode.getValueType();
            int bar = barcode.getFormat();
            // See API reference for complete list of supported types
            if (bar != 256) {
                String id = barcode.getDisplayValue();
                Toast.makeText(getActivity(), id + "\n", Toast.LENGTH_SHORT);
            } else {
                Beep();
                Vibrate();
                Intent intent = new Intent(getActivity(), ResultScan.class);
                switch (valueType) {
                    case Barcode.TYPE_WIFI:
                        String SSID = barcode.getWifi().getSsid();
                        String password = barcode.getWifi().getPassword();
                        int type = barcode.getWifi().getEncryptionType();
                        String security;
                        if(type == 1) security = "nopass";
                        else if(type == 2) security ="WPA";
                        else security = "WEP";

                        String content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
                        intent.putExtra("create_txt", content);
                        intent.putExtra("S", SSID);
                        intent.putExtra("P", password);
                        intent.putExtra("T", security);
                        intent.putExtra("create_title", "Wifi");
                        intent.putExtra("type", "QRcode");
                        
                        break;
                    case Barcode.TYPE_URL:
                        String url = barcode.getUrl().getUrl();
                        Toast.makeText(getActivity(),"URL: "+ url + "\n", Toast.LENGTH_SHORT);
                        intent.putExtra("create_txt", url);
                        intent.putExtra("create_title", "URL");
                        intent.putExtra("type", "QRcode");
                        break;
                    case Barcode.TYPE_TEXT:
                        String text = barcode.getDisplayValue();
                        intent.putExtra("create_txt", text);
                        intent.putExtra("create_title", "Text");
                        intent.putExtra("type", "QRcode");
                        break;
                }


                onPause();
                startActivity(intent);

                break;
            }
        }
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
    public void Viber(Context ct, String value) {
        if (value.equals("on")) {
            Vibrator v = (Vibrator) ct.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(300);
        }
    }

    public void onPause() {
        super.onPause();
        ProcessCameraProvider processCameraProvider = null;
        try {
            processCameraProvider = cameraProviderFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        processCameraProvider.unbindAll();
    }

    @Override
    public void onResume() {
        super.onResume();
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