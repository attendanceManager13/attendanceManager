package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Subject2Adapter extends FirestoreRecyclerAdapter<Subject2, Subject2Adapter.Subject2Holder> {

    public Subject2Adapter(@NonNull FirestoreRecyclerOptions<Subject2> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Subject2Adapter.Subject2Holder holder, int position, @NonNull Subject2 model) {

        holder.name.setText(model.getName());
        holder.percentage.setText(String.valueOf(model.getPercentage())+"%");
    }

    @NonNull
    @Override
    public Subject2Adapter.Subject2Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject2,viewGroup,false);
        return new Subject2Adapter.Subject2Holder(v);
    }

    public String deleteItem(int position)
    {
        Subject2 holder = getItem(position);
        String name = holder.getName();

        getSnapshots().getSnapshot(position).getReference().delete();
        return name;
    }

    class Subject2Holder extends RecyclerView.ViewHolder{
        TextView name;
        //TextView priority;
        /*TextView attended_lectures;
        TextView total_lectures;*/
        TextView percentage;
        public Subject2Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.day_subject_name);
            //priority = itemView.findViewById(R.id.subject_priority);
            /*attended_lectures = itemView.findViewById(R.id.attended_lectures);
            total_lectures = itemView.findViewById(R.id.total_lectures);*/
            percentage = itemView.findViewById(R.id.day_total_percentage);
        }
    }
}
