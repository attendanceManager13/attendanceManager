package com.example.android.attendancemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

public class CurrentStatusActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference cr;
    /*private TextView t1;
    private TextView t2;
    private TextView t3;*/
    private TextView tot_lec;
    private TextView tot_att_lec;
    private TextView pre_att;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_status);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");
        tot_lec = findViewById(R.id.tot_lec);
        tot_att_lec = findViewById(R.id.tot_att_lec);
        pre_att = findViewById(R.id.pre_lec);

        cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    int totalLectures = 0;
                    int totalAttended =0;
                    for(DocumentSnapshot snapshot: task.getResult())
                    {
                        //totalAttended += Integer.parseInt(snapshot.get("attended_lectures").toString());
                        totalAttended += Integer.valueOf(snapshot.get("attended_lectures").toString());
                        totalLectures += Integer.valueOf(snapshot.get("total_lectures").toString());
                    }
                    tot_att_lec.setText(String.valueOf(totalAttended));
                    tot_lec.setText(String.valueOf(totalLectures));
                    Float percentage;
                    if(totalLectures ==0)
                        percentage =(float)0;
                    else {
                        percentage = ((float) totalAttended / (float) totalLectures) * 100;
                        percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
                    }
                    pre_att.setText(String.valueOf(percentage.toString()+"%"));
                }
            }
        });

    }
}
