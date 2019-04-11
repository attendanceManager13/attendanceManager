package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference cr;
    private String subject;
    private int attended_lectures;
    private int total_lectures;
    private float percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
        String previousDate = dateFormat.format(oldDate);

        cr = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection(previousDate);

        cr.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty())
                    updateHistory(cr,previousDay);
            }
        });

    }

    private void updateHistory(final CollectionReference cr, String previousDay) {

            db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(previousDay).whereEqualTo("marked","NO")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot document: task.getResult())
                        {
                            subject = document.getString("name");
                            attended_lectures = (int) (long) document.get("attended_lectures");
                            total_lectures = (int) (long) document.get("total_lectures");
                            percentage = (float) (double) document.get("percentage");

                            cr.add(new Subject2(subject,attended_lectures,total_lectures,percentage,"NO"));
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
    }
}
