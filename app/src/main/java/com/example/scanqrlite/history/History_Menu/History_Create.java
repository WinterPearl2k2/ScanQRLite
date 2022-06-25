package com.example.scanqrlite.history.History_Menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.adapter.HistoryCreateAdapter;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;

import java.util.ArrayList;
import java.util.List;

public class History_Create extends Fragment {
    RecyclerView recyclerView;
    HistoryCreateAdapter adapter;
    List<HistoryCreateItem> createItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_create, container, false);
        ORM(view);
        adapter = new HistoryCreateAdapter();

        createItemList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        loadData();

        return view;
    }

    private void ORM(View view) {
        recyclerView = view.findViewById(R.id.recycle_history_create);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        createItemList = CreateDatabase.getInstance(getContext()).createItemDAO().getListItem();
        adapter.setData(createItemList);
    }
}