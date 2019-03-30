package com.example.android.attendancemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class NewSubjectActivity extends AppCompatActivity {
    private EditText subject_name;
    private NumberPicker priority;
    private Button save_button;
    private CollectionReference cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);



        subject_name = findViewById(R.id.edit_text_subject);
        priority = findViewById(R.id.priority_number_picker);
        save_button = findViewById(R.id.save_button);

        priority.setMinValue(1);
        priority.setMaxValue(10);
        
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton();
            }
        });
    }

    private void saveButton() {
        String subject = subject_name.getText().toString().trim();
        int subject_priority = priority.getValue();

        if(subject.isEmpty()) {
            Toast.makeText(NewSubjectActivity.this, "enter subject name", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        String newString;
        Bundle extras;
        extras = getIntent().getExtras();
        newString = extras.getString("activityName");
        if(newString.equals("AddSubjectsActivity"))
            cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");
        else if(newString.equals("DayActivity"))
            cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table").collection("days");



        cr.add(new Subject(subject,subject_priority));
        Toast.makeText(NewSubjectActivity.this, "subject added", Toast.LENGTH_LONG).show();
        finish();
    }
}
