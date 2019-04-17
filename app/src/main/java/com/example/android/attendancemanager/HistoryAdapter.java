package com.example.android.attendancemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class HistoryAdapter extends FirestoreRecyclerAdapter<History, HistoryAdapter.HistoryHolder> {


    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<History> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull History model) {
        holder.date.setText(model.getDate());
        holder.cancel.setText("cancel");
        holder.done.setText("done");
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_card,viewGroup,false);
        return new HistoryAdapter.HistoryHolder(v);
    }

    public String deleteItem(int position)
    {
        History holder = getItem(position);
        String date = holder.getDate();

        getSnapshots().getSnapshot(position).getReference().delete();
        return date;
    }

    class HistoryHolder extends RecyclerView.ViewHolder{
        TextView date;
        Button cancel;
        Button done;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            done = itemView.findViewById(R.id.done_button);
            cancel = itemView.findViewById(R.id.cancel_button);
        }


    }
}