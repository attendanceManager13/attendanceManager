package com.example.android.attendancemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Criteria extends AppCompatActivity {
    private SeekBar seek;
    private TextView t1;
    private Button save;
    private CollectionReference cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criteria);
        seek=findViewById(R.id.seek);
        t1=findViewById(R.id.ct2);
        save = findViewById(R.id.cb1);
        final HashMap<String,String> map = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("seekBarValue", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int value = sharedPreferences.getInt("value",0);
        t1.setText(value);
        cr= FirebaseFirestore.getInstance().collection(FirebaseAuth.getInstance().getCurrentUser().getUid()).document("target").collection("attendace_criteria");
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t1.setText(String.format("%s%%", String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.put("criteria",t1.getText().toString());
                cr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            if(task.getResult().isEmpty())
                                cr.add(map);
                            else
                            {
                                for(DocumentSnapshot snapshot: task.getResult())
                                    cr.document(snapshot.getId()).update("criteria",t1.getText().toString());
                            }
                        }
                    }
                });

                finish();
            }

        });
    }
}
