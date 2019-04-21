package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class EditSubjectActivity extends AppCompatActivity {
    private EditText subject_name;
    private Button save_button;
    private EditText attended_lectures;
    private EditText total_lectures;
    private CollectionReference cr;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);

        Bundle extras = getIntent().getExtras();
        final String subjectName = extras.getString("name");
        final String attended = extras.getString("attended");
        final String total = extras.getString("total");

        mAuth  = FirebaseAuth.getInstance();
        cr = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");

        subject_name = findViewById(R.id.edit_text_subject);
        save_button = findViewById(R.id.save_button);
        total_lectures = findViewById(R.id.edit_text_total_lectures);
        attended_lectures = findViewById(R.id.edit_text_attended_lectures);

        subject_name.setText(subjectName);
        attended_lectures.setText(attended);
        total_lectures.setText(total);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton(subjectName);
            }
        });
    }

    private void saveButton(final String subjectName) {
        String subject = subject_name.getText().toString().trim();
        String attended = attended_lectures.getText().toString().trim();
        String total = total_lectures.getText().toString().trim();

        if(subject.isEmpty() || attended.isEmpty() || total.isEmpty()) {
            Toast.makeText(EditSubjectActivity.this, "enter all fields", Toast.LENGTH_LONG).show();
            return;
        }
        if(Integer.parseInt(total)<Integer.parseInt(attended)){
            Toast.makeText(EditSubjectActivity.this, "attended lectures must not exceed total lectures", Toast.LENGTH_LONG).show();
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
        addSubject(cr,subject,attended,total,percentage,subjectName);
    }

    private void addSubject(final CollectionReference cr, final String subject, final String attended, final String total, final float percentage, final String subjectName) {
        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())

                {
                    int flag = 0;
                    String id = null;
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        if(snapshot.get("name").equals(subjectName)) 
                            id = snapshot.getId();
                        String str = snapshot.get("name").toString().toLowerCase();
                        if (str.equals(subject.toLowerCase()) && !str.equals(subjectName)) {
                            flag=1;
                        } 
                    }
                    if(flag==1)
                    {
                        Toast.makeText(EditSubjectActivity.this, "subject with same name already exists", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        cr.document(id).update("name", subject, "attended_lectures", Integer.valueOf(attended),
                                "total_lectures", Integer.valueOf(total), "percentage", percentage);
                        Toast.makeText(EditSubjectActivity.this, "changes made", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
    }
}
