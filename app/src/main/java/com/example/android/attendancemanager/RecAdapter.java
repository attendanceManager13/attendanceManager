package com.example.android.attendancemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {
    private Context mCtx;
    private List<Gmodel> proList;

    RecAdapter(Context mCtx, List<Gmodel> proList) {
        this.mCtx = mCtx;
        this.proList = proList;
    }
    @NonNull
    @Override
    public RecViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view=inflater.inflate(R.layout.activity_makecards,null);
        return new RecViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecViewHolder recViewHolder, int i) {
        Gmodel gmodel=proList.get(i);
        recViewHolder.textView1.setText(gmodel.getItem());
    }

    @Override
    public int getItemCount() {
        return proList.size();
    }

    class RecViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        RecViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.t1);
        }
    }
}