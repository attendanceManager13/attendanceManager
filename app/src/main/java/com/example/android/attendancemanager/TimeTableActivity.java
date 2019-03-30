package com.example.android.attendancemanager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TimeTableActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    final Context context=this;
    RecAdapter adapter;
    List<Gmodel> proList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        recyclerView =findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        proList.add(new Gmodel(1,"Monday"));
        proList.add(new Gmodel(2,"Tuesday"));
        proList.add(new Gmodel(3,"Wednesday"));
        proList.add(new Gmodel(4,"Thursday"));
        proList.add(new Gmodel(5,"Friday"));
        adapter=new RecAdapter(this,proList);
        recyclerView.setAdapter(adapter);

    }
}
