package com.example.android.attendancemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecViewHolder> {
    private Context mCtx;

    private List<Gmodel> proList;


    public RecAdapter(Context mCtx, List<Gmodel> proList) {
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
        final Gmodel gmodel=proList.get(i);
        recViewHolder.textView1.setText(gmodel.getItem());
        recViewHolder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),DayActivity.class);
                    intent.putExtra("name",gmodel.getItem());
                    view.getContext().startActivity(intent);

            }
        });
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),DayActivity.class);
                    intent.putExtra("name",textView1.getText().toString());
                    view.getContext().startActivity(intent);
                }
            });

        }
    }


}