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

import static android.support.constraint.Constraints.TAG;
interface RecyclerViewClickListener{
    void onClick(View view,int position);
}

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {
    private Context mCtx;
    private RecyclerViewClickListener mListener;
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
        return new RecViewHolder(view,mListener);
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

    class RecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView1;
        private RecyclerViewClickListener mListener;
        RecViewHolder(@NonNull View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            textView1=itemView.findViewById(R.id.t1);
            mListener=listener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            mListener.onClick(view,getAdapterPosition());
        }
    }
}