package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class NewSubjectActivity extends AppCompatActivity {
    private EditText subject_name;
    private Button save_button;
    private EditText attended_lectures;
    private EditText total_lectures;
    private CollectionReference cr;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subject);
        mAuth  = FirebaseAuth.getInstance();
        subject_name = findViewById(R.id.edit_text_subject);
        save_button = findViewById(R.id.save_button);
        total_lectures = findViewById(R.id.edit_text_total_lectures);
        attended_lectures = findViewById(R.id.edit_text_attended_lectures);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton();
            }
        });
    }

    private void saveButton() {
        String subject = subject_name.getText().toString().trim();
        String attended = attended_lectures.getText().toString().trim();
        String total = total_lectures.getText().toString().trim();

        if(subject.isEmpty() || attended.isEmpty() || total.isEmpty()) {
            Toast.makeText(NewSubjectActivity.this, "enter all fields", Toast.LENGTH_LONG).show();
            return;
        }
        if(Integer.parseInt(total)<Integer.parseInt(attended)){
            Toast.makeText(NewSubjectActivity.this, "attended lectures must not exceed total lectures", Toast.LENGTH_LONG).show();
            return;
        }
        cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");

        float percentage;
        if(Integer.valueOf(total)==0)
            percentage = 0;
        else {
            percentage = (Float.parseFloat(attended) / Float.parseFloat(total)) * 100;
            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
        }
        addSubject(cr,subject,attended,total,percentage);
    }

    private void addSubject(final CollectionReference cr, final String subject, final String attended, final String total, final float percentage) {
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())

                {
                    int flag = 0;
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        if((snapshot.get("name").toString().toLowerCase()).equals(subject.toLowerCase())) {
                            flag = 1;
                            break;
                        }

                    }
                    if(flag==1)
                        Toast.makeText(NewSubjectActivity.this, "subject already present", Toast.LENGTH_LONG).show();
                    else{
                        cr.add(new Subject(subject,Integer.valueOf(attended),Integer.valueOf(total),percentage));
                        Toast.makeText(NewSubjectActivity.this, "subject added", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
}
