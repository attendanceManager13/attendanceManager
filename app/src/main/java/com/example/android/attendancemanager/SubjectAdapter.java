package com.example.android.attendancemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SubjectAdapter extends FirestoreRecyclerAdapter<Subject, SubjectAdapter.SubjectHolder> {


    public SubjectAdapter(@NonNull FirestoreRecyclerOptions<Subject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectHolder holder, int position, @NonNull Subject model) {
        holder.name.setText(model.getName());
        //holder.priority.setText(String.valueOf(position+1));
        holder.attended_lectures.setText(String.valueOf(model.getAttended_lectures()));
        holder.total_lectures.setText(String.valueOf(model.getTotal_lectures()));
        holder.percentage.setText(String.valueOf(model.getPercentage())+"%");
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject,viewGroup,false);
        return new SubjectHolder(v);
    }

    public String deleteItem(int position)
    {
        Subject holder = getItem(position);
        String name = holder.getName();

        getSnapshots().getSnapshot(position).getReference().delete();
        return name;
    }

    class SubjectHolder extends RecyclerView.ViewHolder{
         TextView name;
         //TextView priority;
         TextView attended_lectures;
         TextView total_lectures;
         TextView percentage;
        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.subject_name);
            //priority = itemView.findViewById(R.id.subject_priority);
            attended_lectures = itemView.findViewById(R.id.attended_lectures);
            total_lectures = itemView.findViewById(R.id.total_lectures);
            percentage = itemView.findViewById(R.id.total_percentage);
                }
    }
    }



