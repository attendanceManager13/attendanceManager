package com.example.android.attendancemanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomDialog1 extends Dialog implements android.view.View.OnClickListener {
    public Context c;

    private EditText name  ;
    private EditText attended;
    private EditText total;
    private Button ok;
    private Button cancel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference cr;

    public CustomDialog1(Context a) {
        super(a);
        this.c = a;
    }

    /*@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view).setTitle("Want to change something?").setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name  = findViewById(R.id.dt1);
        attended = findViewById(R.id.dt2);
        total = findViewById(R.id.dt3);
        ok = findViewById(R.id.btn_ok);
        cancel = findViewById(R.id.btn_cancel);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");


        String subject = name.getText().toString();
        int att = Integer.parseInt(attended.getText().toString());
        int tot = Integer.parseInt(total.getText().toString());

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:


        }
    }
}
