package com.example.android.attendancemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DayActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection("days");
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subjects);
        FloatingActionButton fab = findViewById(R.id.button_add_subject);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewSubjectActivity.class);
                intent.putExtra("activityName", "DayActivity");
                startActivity(intent);
            }
        });
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = cr.orderBy("priority",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Subject> options = new FirestoreRecyclerOptions.Builder<Subject>().setQuery(query,Subject.class).build();

        adapter = new SubjectAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.add_subjects_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
