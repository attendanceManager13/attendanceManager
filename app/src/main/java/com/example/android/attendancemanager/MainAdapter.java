package com.example.android.attendancemanager;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import java.util.Date;
import java.util.List;


public class MainAdapter extends FirestoreRecyclerAdapter<MainModel,MainAdapter.MainViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference subjectData = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");;
    private DocumentReference timeTableData =FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table");
    private String[] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private Context context ;
    private SharedPreferences sharedPreferences;

    public MainAdapter(FirestoreRecyclerOptions<MainModel> options,Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, final int position, @NonNull MainModel model) {
        mainViewHolder.textView1.setText(model.getName());
        mainViewHolder.textView2.setText(model.getStatus());
        mainViewHolder.textView3.setText(model.getProgtext());
        mainViewHolder.progressBar.setProgress(model.getProgress());
        mainViewHolder.b1.setText(model.getPlus());
        mainViewHolder.b2.setText(model.getMinus());
        final String subjectName = mainViewHolder.textView1.getText().toString().trim();

        sharedPreferences = context.getSharedPreferences("<your-app-id>",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        //buttonEnabled(mainViewHolder);
        String savedDateTime = sharedPreferences.getString(String.valueOf(position),"");

        if(!"".equals(savedDateTime))
        {
            String dateStringNow = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
            if(savedDateTime.equals(dateStringNow))
                buttonsDisabled(mainViewHolder);
        }

        mainViewHolder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentSubject(subjectName);
                presentDay(subjectName);

                String dateString = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();

                editor.putString(String.valueOf(position), dateString);
                editor.commit();
                //buttonsDisabled(mainViewHolder);

            }
        });
        mainViewHolder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                absentSubject(subjectName);
                absentDay(subjectName);
                String dateString = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(position), dateString);
                editor.commit();
                //buttonsDisabled(mainViewHolder);

            }
        });
    }

    private void buttonsDisabled(MainViewHolder mainViewHolder) {
        mainViewHolder.b2.setEnabled(false);
        mainViewHolder.b1.setEnabled(false);
        mainViewHolder.b2.setFocusable(false);
        mainViewHolder.b1.setFocusable(false);
        mainViewHolder.b1.setBackgroundColor(Color.GRAY);
        mainViewHolder.b2.setBackgroundColor(Color.GRAY);
    }
    private void buttonEnabled(MainViewHolder mainViewHolder)
    {
        mainViewHolder.b2.setEnabled(true);
        mainViewHolder.b1.setEnabled(true);
        mainViewHolder.b2.setFocusable(true);
        mainViewHolder.b1.setFocusable(true);
        mainViewHolder.b1.setBackgroundColor(Color.GREEN);
        mainViewHolder.b2.setBackgroundColor(Color.RED);
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
