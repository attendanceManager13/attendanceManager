package com.example.android.attendancemanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class PreviousDateActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db ;
    RecyclerView recyclerView;
    final Context context=this;
    private PreviousDateAdapter adapter;
    CollectionReference cr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_date);
        Bundle extras = getIntent().getExtras();
        final String newString = extras.getString("date");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setupRecyclerView(newString);
    }

    private void setupRecyclerView(String newString) {
        cr= db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("history_data");
        Query query = cr.whereEqualTo("date",newString).orderBy("lecture", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<History> options = new FirestoreRecyclerOptions.Builder<History>().setQuery(query, History.class).build();

        adapter = new PreviousDateAdapter(options,context);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //sentToLogin();
        adapter.startListening();
    }
}
