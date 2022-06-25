package com.example.scanqrlite.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.scanqrlite.R;

import com.example.scanqrlite.Test;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.scan.ResultScan;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryCreateAdapter extends RecyclerView.Adapter<HistoryCreateAdapter.CreateViewHolder> {
    List<HistoryCreateItem> createItemList;
    Bitmap bitmap;
    Context context;

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
                intent.putExtra("create_txt", historyCreateItem.getContent());
                if(historyCreateItem.getTitle().equals("Wifi")) {
                    intent.putExtra("S", historyCreateItem.getContent());
                    intent.putExtra("P", historyCreateItem.getPassword());
                    intent.putExtra("T", historyCreateItem.getSecurity());
                }
                intent.putExtra("type", "QRcode");
                context.startActivity(intent);
            }
        });

    }

    private Bitmap CreateImage(String content) throws WriterException {
        int sizeWidth = 660;
        int sizeHeight = 264;

        Map hints = new HashMap();
        hints.put(EncodeHintType.MARGIN, 1);

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
        private LinearLayout layoutItem;
        private TextView txtTitle, txtContent, txtDate, txtPass, txtSecurity;
        private ImageView imgQr;

        public CreateViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.title_history_item);
            txtContent = itemView.findViewById(R.id.infor_history_item);
            txtDate = itemView.findViewById(R.id.date_history_item);
            imgQr = itemView.findViewById(R.id.qr_image_history_item);
            layoutItem = itemView.findViewById(R.id.history_recycleview_item);
        }
    }
}
