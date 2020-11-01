package com.artyushin.hw102;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private ListView list;
    private SwipeRefreshLayout refreshLayout;
    private List<Map<String, String>> content;
    private SimpleAdapter adapter;
    private String settingField;

    private SharedPreferences prefs;
    private final static String PREF_NAME = "LIST_PREF";
    private final static String PREF_KEY = "LIST_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        settingField = getString(R.string.large_text);
        prefs.edit().putString(PREF_KEY, settingField).apply();

        refreshLayout = findViewById(R.id.swipe_refresh);

        list = findViewById(R.id.list);
        content = new ArrayList<>();

        refreshContent();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ListViewActivity.this, "ID  " + position, Toast.LENGTH_SHORT).show();
                content.remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Toast.makeText(ListViewActivity.this, "Обновление",Toast.LENGTH_LONG).show();
                refreshContent();
                adapter.notifyDataSetChanged(); //Данные изменились - изм. адаптер
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshContent() {
        content = prepareContent();
        String[] from = new String[]{"Header", "Length"};
        int[] to = new int[]{R.id.textHeader, R.id.textContent};
        adapter = new SimpleAdapter(this, content, R.layout.list_item, from, to);
        list.setAdapter(adapter);
    }

    @NonNull
    private ArrayList<Map<String, String>> prepareContent() {
        ArrayList<Map<String, String>> data;
        data = new ArrayList<>();
        settingField = prefs.getString(PREF_KEY, "");
        String[] arrayContent = settingField.split("\n\n");

        for (int i = 0; i < arrayContent.length; i++) {
            Map<String, String> RowMap = new HashMap<>();
            RowMap.put("Header", arrayContent[i]);
            RowMap.put("Length", String.valueOf(arrayContent[i].length()));
            data.add(i, RowMap);
        }

        return data;
    }
}