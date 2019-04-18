package com.example.android.attendancemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference cr;
    private CollectionReference cr2;
    private DocumentReference timeTableData;
    private String subject;
    private int lecture;

    private HistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //get yesterday's DAYNAME
        final String previousDay = previousDay(date);
        //get yesterday's DATE
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date oldDate = cal.getTime();
        final String previousDate = dateFormat.format(oldDate);
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("history_data");*/
        cr2 = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("previous_date");
        //timeTableData =FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table");


        /*cr.whereEqualTo("date",previousDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().isEmpty()) {
                        updateHistory(cr, previousDay, previousDate,cr2);
                        unmarkDay(timeTableData,previousDay,previousDate);
                    }
                }
            }
        });*/

        setUpRecyclerView();


    }

    /*private void unmarkDay(final DocumentReference timeTableData, final String previousDay, String previousDate) {
        timeTableData.collection(previousDay).whereEqualTo("marked","YES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                        timeTableData.collection(previousDay).document(snapshot.getId()).update("marked","NO");
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("today"+previousDate,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }*/

    private void setUpRecyclerView() {

        Query query = cr2.orderBy("date");

        FirestoreRecyclerOptions<History> options = new FirestoreRecyclerOptions.Builder<History>().setQuery(query, History.class).build();

        adapter = new HistoryAdapter(options,HistoryActivity.this);

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    /*private void updateHistory(final CollectionReference cr, final String previousDay, final String previousDate, final CollectionReference cr2) {

            db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(previousDay).whereEqualTo("marked","NO")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        cr2.add(new History(previousDate));
                        for(DocumentSnapshot document: task.getResult())
                        {
                            subject = document.getString("name");
                            lecture = Integer.parseInt(String.valueOf(document.get("lecture")));
                            cr.add(new History(subject,lecture,previousDate));
                        }
                    }
                }
            });


        }
        private String previousDay(Date dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(cal.getTime());
    }*/
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
