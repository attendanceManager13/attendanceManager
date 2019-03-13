package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SubjectAdapter extends FirestoreRecyclerAdapter<Subject, SubjectAdapter.SubjectHolder> {


    public SubjectAdapter(@NonNull FirestoreRecyclerOptions<Subject> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SubjectHolder holder, int position, @NonNull Subject model) {
        holder.name.setText(model.getName());
        holder.priority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject,viewGroup,false);
        return new SubjectHolder(v);
    }

    public void deleteItem(int position)
    {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class SubjectHolder extends RecyclerView.ViewHolder{
         TextView name;
         TextView priority;
        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.subject_name);
            priority = itemView.findViewById(R.id.subject_priority);
        }
    }
}
