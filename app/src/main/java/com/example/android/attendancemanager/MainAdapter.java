package com.example.android.attendancemanager;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;


public class MainAdapter extends FirestoreRecyclerAdapter<MainModel,MainAdapter.MainViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference subjectData = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");;
    private DocumentReference timeTableData =FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table");
    private String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    public MainAdapter(FirestoreRecyclerOptions<MainModel> options){
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, int position, @NonNull MainModel model) {
        mainViewHolder.textView1.setText(model.getName());
        mainViewHolder.textView2.setText(model.getStatus());
        mainViewHolder.textView3.setText(model.getProgtext());
        mainViewHolder.progressBar.setProgress(model.getProgress());
        mainViewHolder.b1.setText(model.getPlus());
        mainViewHolder.b2.setText(model.getMinus());
        final String subjectName = mainViewHolder.textView1.getText().toString().trim();
        mainViewHolder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentSubject(subjectName);
                presentDay(subjectName);
                mainViewHolder.b2.setEnabled(false);
                mainViewHolder.b2.setFocusable(false);
                mainViewHolder.b1.setFocusable(false);
                mainViewHolder.b1.setEnabled(false);
            }
        });
        mainViewHolder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                absentSubject(subjectName);
                absentDay(subjectName);
                mainViewHolder.b2.setEnabled(false);
                mainViewHolder.b1.setEnabled(false);
                mainViewHolder.b2.setFocusable(false);
                mainViewHolder.b1.setFocusable(false);
            }
        });
    }

    private void absentDay(String subjectName) {
        for(final String day:days)
        {
            timeTableData.collection(day).whereEqualTo("name",subjectName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot document: task.getResult())
                        {
                            int attended = (int) (long) document.get("attended_lectures");
                            int total = (int) (long) document.get("total_lectures");

                            //attended += 1;
                            total += 1;
                            float percentage = ((float) attended / (float) total) * 100;
                            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
                            updateDay(attended,total,percentage,document.getId(),day);
                        }
                    }
                }
            });
        }

    }

    private void presentDay(String subjectName) {
        for(final String day:days)
        {
            timeTableData.collection(day).whereEqualTo("name",subjectName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot document: task.getResult())
                        {
                            int attended = (int) (long) document.get("attended_lectures");
                            int total = (int) (long) document.get("total_lectures");

                            attended += 1;
                            total += 1;
                            float percentage = ((float) attended / (float) total) * 100;
                            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
                            updateDay(attended,total,percentage,document.getId(),day);
                        }
                    }
                }
            });
        }
    }

    private void presentSubject(final String name) {
        subjectData.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {

                        if (document.getString("name").equals(name)) {
                            int attended = (int) (long) document.get("attended_lectures");
                            int total = (int) (long) document.get("total_lectures");

                            attended += 1;
                            total += 1;
                            float percentage = ((float) attended / (float) total) * 100;
                            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
                            updateSubject(attended, total, percentage, document.getId());
                        }
                    }
                }

            }
        });




    }



    private void absentSubject(final String name) {
        subjectData.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document: task.getResult())
                    {

                        if(document.getString("name").equals(name))
                        {
                            int attended = (int) (long) document.get("attended_lectures");
                            int total = (int) (long) document.get("total_lectures");
                            total+=1;
                            float percentage = ((float)attended/(float)total)*100;
                            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
                            updateSubject(attended,total,percentage,document.getId());
                        }
                    }
                }

            }
        });
    }
    private void updateSubject(int attended, int total, float percentage, String id) {
        subjectData.document(id).update(
                "attended_lectures",attended,
                "total_lectures",total,
                "percentage",percentage
        );
        notifyDataSetChanged();

    }
    private void updateDay(int attended, int total, float percentage, String id, String day) {

        timeTableData.collection(day).document(id).update("attended_lectures",attended,
                "total_lectures",total,
                "percentage",percentage);
        notifyDataSetChanged();
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
