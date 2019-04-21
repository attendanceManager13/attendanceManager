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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewDaySubjectActivity extends AppCompatActivity {
    private Spinner spinner;
    private Spinner spinner2;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private List subjectList;
    private List priorityList;
    private Button saveButton;
    private CollectionReference cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_day_subject);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.priority_spinner);
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        subjectList = new ArrayList();
        priorityList = new ArrayList();
        //priorityList.add(0);
        for(int i =0; i<=10; i++)
            priorityList.add(i);
        saveButton = findViewById(R.id.save_subject_button);
        Bundle extras = getIntent().getExtras();
        final String newString = extras.getString("dayName");
        String p="Choose Subject";
        subjectList.add(p);
        db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document: task.getResult()){
                        String subject = document.getString("name");
                        subjectList.add(subject);

                    }
                    ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<String>(NewDaySubjectActivity.this, R.layout.spinner_item, subjectList);
                    subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(subjectsAdapter);
                    ArrayAdapter<Integer> priorityAdapter = new ArrayAdapter<>(NewDaySubjectActivity.this,R.layout.spinner_item, priorityList);
                    priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(priorityAdapter);
                }
                else
                    finish();

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            String subject;

            @Override
            public void onClick(View view) {
                cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(newString);
                String name;
                final int number;
                if (spinner == null || spinner.getSelectedItem() == null) {
                    Toast.makeText(NewDaySubjectActivity.this, "select subject", Toast.LENGTH_LONG).show();
                    return;
                }
                else if(spinner2 == null || spinner2.getSelectedItem() == null)
                {
                    Toast.makeText(NewDaySubjectActivity.this, "select Lecture no.",Toast.LENGTH_LONG).show();
                    return;
                }
                name = spinner.getSelectedItem().toString();
                final String text = name;
                number = (int) spinner2.getSelectedItem();

                if (text.equals("Choose Subject") || number==0) {
                    Toast.makeText(view.getContext(), "Please select valid subject and lecture", Toast.LENGTH_SHORT).show();
                }
                else{
                    addSubject(number,cr,text);
                }
            }
        });

    }

    private void addSubject(final int number, final CollectionReference cr, final String text) {

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    int flag2=0;
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        if(Integer.parseInt(String.valueOf(snapshot.get("lecture")))==number)
                        {
                            flag2=1;
                            break;
                        }

                    }
                    if(flag2==1)
                        Toast.makeText(NewDaySubjectActivity.this,"Lecture number already present",Toast.LENGTH_LONG).show();
                    else
                    {
                        cr.add(new Subject2(text, "YES",number));

                        Toast.makeText(NewDaySubjectActivity.this, "subject added", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

            }
        });

    }
}
