package com.example.scanqrlite.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
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

public class HistoryCreateAdapter extends RecyclerView.Adapter<HistoryCreateAdapter.CreateViewHolder> {
    List<HistoryCreateItem> createItemList;
    Bitmap bitmap;
    Context context;
    LifecycleOwner lifecycleOwner;

    public HistoryCreateAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HistoryCreateItem> data) {
        this.createItemList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
        return new CreateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_scale);
        holder.itemView.startAnimation(animation);

        HistoryCreateItem historyCreateItem = createItemList.get(position);
        if(historyCreateItem == null) {
            return;
        }

        holder.txtTitle.setText(historyCreateItem.getTitle());
        holder.txtContent.setText(historyCreateItem.getContent());
        holder.txtDate.setText(historyCreateItem.getDate());
        try {
            holder.imgQr.setImageBitmap(CreateImage(historyCreateItem.getResult()));
        } catch (WriterException e) {
            e.printStackTrace();
        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ResultScan.class);
                intent.putExtra("create_title", historyCreateItem.getTitle());
                intent.putExtra("create_txt", historyCreateItem.getResult());
                if(historyCreateItem.getTitle().equals("Wifi")) {
                    intent.putExtra("S", historyCreateItem.getContent());
                    intent.putExtra("P", historyCreateItem.getPassword());
                    intent.putExtra("T", historyCreateItem.getSecurity());
                }
                intent.putExtra("type", "QRcode");
                intent.putExtra("type_barcode", "QRcode");
                context.startActivity(intent);
            }
        });

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PowerMenu powerMenu = new PowerMenu.Builder(context)
                        .addItem(new PowerMenuItem(context.getText(R.string.copy_text), R.drawable.ic_copy))
                        .addItem(new PowerMenuItem(context.getText(R.string.search), R.drawable.ic_search))
                        .addItem(new PowerMenuItem(context.getText(R.string.share), R.drawable.ic_share))
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
                                        coppy(historyCreateItem.getResult());
                                        break;
                                    case 1:
                                        search(historyCreateItem.getResult());
                                        break;
                                    case 2:
                                        share(historyCreateItem.getResult());
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
        Toast.makeText(context, R.string.copy_success, Toast.LENGTH_SHORT).show();
    }

    private Bitmap CreateImage(String content) throws WriterException {
        int sizeWidth = 660;
        int sizeHeight = 264;

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.MARGIN, 1);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth, hints);
//        String type = intent.getStringExtra("type");
//        switch (type) {
//            case "QRcode":
//                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth);
//                break;
////            default:
////                matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, sizeWidth, sizeWidth);
////                break;
//        }

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

    @Override
    public int getItemCount() {
        return createItemList == null ? 0 : createItemList.size();
    }

    public class CreateViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout layoutItem;
        private TextView txtTitle, txtContent, txtDate;
        private ImageView imgQr, imgMore;

        public CreateViewHolder(@NonNull View itemView) {
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
