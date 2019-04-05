package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewDaySubjectActivity extends AppCompatActivity {
    private Spinner spinner;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private List subjectList;
    private Button saveButton;
    private CollectionReference cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day_subject);
        spinner = findViewById(R.id.spinner);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        subjectList = new ArrayList();
        saveButton = findViewById(R.id.save_subject_button);
        Bundle extras = getIntent().getExtras();
        final String newString = extras.getString("dayName");

        db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        String subject = document.getString("name");
                        subjectList.add(subject);

                    }
                    ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<String>(NewDaySubjectActivity.this, android.R.layout.simple_spinner_item, subjectList);
                    subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(subjectsAdapter);
                }
                else
                    finish();

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            String subject;
            int attended_lectures;
            int total_lectures;
            float percentage;
            @Override
            public void onClick(View view) {

                String name = null;
                if(spinner==null || spinner.getSelectedItem() == null)
                {
                    Toast.makeText(NewDaySubjectActivity.this, "select subject", Toast.LENGTH_LONG).show();
                    return;
                }
                name = spinner.getSelectedItem().toString();
                final String text = name;

                db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot document: task.getResult())
                            {
                                subject = document.getString("name");
                                if(subject.equals(text))
                                {
                                    attended_lectures = (int)(long)document.get("attended_lectures");
                                    total_lectures = (int)(long)document.get("total_lectures");
                                    percentage = (float)(double)document.get("percentage");
                                    break;


                                }
                            }
                            cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(newString);
                            cr.add(new Subject(subject,attended_lectures,total_lectures,percentage));
                            Toast.makeText(NewDaySubjectActivity.this, "subject added", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
            }
        });

    }
}
