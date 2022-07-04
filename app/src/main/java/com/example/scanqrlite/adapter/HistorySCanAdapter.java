package com.example.scanqrlite.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.HistoryScanItem;
import com.example.scanqrlite.scan.ResultScan;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.skydoves.powermenu.CircularEffect;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HistorySCanAdapter extends RecyclerView.Adapter<HistorySCanAdapter.ScanViewHolder> {
    List<HistoryScanItem> scanItemList;
    Bitmap bitmap;
    Context context;
    LifecycleOwner lifecycleOwner;

    public HistorySCanAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HistoryScanItem> data) {
        this.scanItemList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        return new ScanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanViewHolder holder, int position) {

        HistoryScanItem historyScanItem = scanItemList.get(position);
        if(historyScanItem == null) {
            return;
        }

        holder.txtTitle.setText(historyScanItem.getTitle());
        holder.txtContent.setText(historyScanItem.getContent());
        holder.txtDate.setText(historyScanItem.getDate());

        try {
            holder.imgQr.setImageBitmap(CreateImage(historyScanItem.getResult(), historyScanItem.getTypeScan()));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ResultScan.class);
                String valueType = historyScanItem.getTypeScan();
                switch (valueType) {
                    case "QRcode":
                        intent.putExtra("create_title", historyScanItem.getTitle());
                        intent.putExtra("create_txt", historyScanItem.getResult());
                        if(historyScanItem.getTitle().equals("Wifi")) {
                            intent.putExtra("S", historyScanItem.getContent());
                            intent.putExtra("P", historyScanItem.getPassword());
                            intent.putExtra("T", historyScanItem.getSecurity());
                        }
                        intent.putExtra("type", "QRcode");
                        intent.putExtra("type_barcode", historyScanItem.getTypeScan());
                        break;
                    case "Code_128":
                    case "Code_39":
                    case "EAN_13":
                    case "EAN_8":
                    case "Code_2of5":
                    case "UPC_A":
                    case "UPC_E":
                        intent.putExtra("create_title", historyScanItem.getTitle());
                        intent.putExtra("create_txt", historyScanItem.getResult());
                        intent.putExtra("type", "Barcode");
                        intent.putExtra("type_barcode", historyScanItem.getTypeScan());
                        break;
                }

                context.startActivity(intent);
            }
        });

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(context)
                        .addItem(new PowerMenuItem("Copy Text", R.drawable.ic_copy))
                        .addItem(new PowerMenuItem("Search", R.drawable.ic_search))
                        .addItem(new PowerMenuItem("Share", R.drawable.ic_share))
                        .setAutoDismiss(true)
                        .setLifecycleOwner(lifecycleOwner)
                        .setMenuShadow(10f)
                        .setMenuRadius(10f)
                        .setIconSize(18)
                        .setCircularEffect(CircularEffect.INNER)
                        .setTextSize(18)
                        .setSelectedMenuColor(ContextCompat.getColor(context, R.color.primary_btn))
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                            @Override
                            public void onItemClick(int position, PowerMenuItem item) {

                            }
                        })
                        .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                            @Override
                            public void onItemClick(int position, PowerMenuItem item) {
                                switch (position) {
                                    case 0:
                                        coppy(historyScanItem.getResult());
                                        break;
                                    case 1:
                                        search(historyScanItem.getResult());
                                        break;
                                    case 2:
                                        share(historyScanItem.getResult());
                                        break;
                                    default: break;
                                }
                            }
                        })
                        .setInitializeRule(Lifecycle.Event.ON_CREATE, 0)
                        .build();
                powerMenu.showAsDropDown(view, -450,-100);
            }
        });

    }

    private void share(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }

    private void search(String content) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.google.com/search?q=" +
                content));
        context.startActivity(intent);
    }

    private void coppy(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", content);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
    }

    private Bitmap CreateImage(String result, String content) throws WriterException {
        int sizeWidth = 660;
        int sizeHeight = 264;

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = null;
        String type = content;
        switch (type) {
            case "QRcode":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth, hints);
                break;
            case "EAN_13":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.EAN_13, sizeWidth, sizeHeight, hints);
                break;
            case "EAN_8":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.EAN_8, sizeWidth, sizeHeight, hints);
                break;
            case "UPC_A":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.UPC_A, sizeWidth, sizeHeight, hints);
                break;
            case "UPC_E":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.UPC_E, sizeWidth, sizeHeight, hints);
                break;
            case "Code_128":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.CODE_128, sizeWidth, sizeHeight, hints);
                break;
            case "Code_2of5":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.ITF, sizeWidth, sizeHeight, hints);
                break;
            case "Code_39":
                matrix = new MultiFormatWriter().encode(result, BarcodeFormat.CODE_39, sizeWidth, sizeHeight, hints);
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

        return bitmap;
    }

    @Override
    public int getItemCount() {
        return scanItemList == null ? 0 : scanItemList.size();
    }

    public class ScanViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutItem;
        private TextView txtTitle, txtContent, txtDate;
        private ImageView imgQr, imgMore;

        public ScanViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.title_history_item);
            txtContent = itemView.findViewById(R.id.infor_history_item);
            txtDate = itemView.findViewById(R.id.date_history_item);
            imgQr = itemView.findViewById(R.id.qr_image_history_item);
            layoutItem = itemView.findViewById(R.id.history_recycleview_item);
            imgMore = itemView.findViewById(R.id.more_history_item);
        }
    }
}
