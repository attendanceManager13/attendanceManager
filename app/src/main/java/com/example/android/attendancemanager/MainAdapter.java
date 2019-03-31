package com.example.android.attendancemanager;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;



public class MainAdapter extends FirestoreRecyclerAdapter<MainModel,MainAdapter.MainViewHolder> {

    public MainAdapter(FirestoreRecyclerOptions<MainModel> options){
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull MainViewHolder mainViewHolder, int position, @NonNull MainModel model) {

        mainViewHolder.textView1.setText(model.getName());
        mainViewHolder.textView2.setText(model.getStatus());
        mainViewHolder.textView3.setText(model.getProgtext());
        mainViewHolder.progressBar.setProgress(model.getProgress());
        mainViewHolder.b1.setText(model.getPlus());
        mainViewHolder.b2.setText(model.getMinus());
    }
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.subjectcards,viewGroup,false);
        return new MainViewHolder(view);
    }




    /*@Override
    public int getItemCount() {
        return proList.size();
    }*/

    class MainViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2,textView3;
        Button b1,b2;
        ProgressBar progressBar;
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.t1);
            textView2=itemView.findViewById(R.id.t2);
            textView3=itemView.findViewById(R.id.t3);
            progressBar=itemView.findViewById(R.id.progressBar);
            b1=itemView.findViewById(R.id.b1);
            b2=itemView.findViewById(R.id.b2);
        }
    }
}
