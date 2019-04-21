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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DayActivity extends AppCompatActivity {



    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference cr;
    private CollectionReference cr2;
    private Subject2Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        final String newString = extras.getString("name");
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(newString);
        cr2 = db.collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        FloatingActionButton fab = findViewById(R.id.button_add_subject_day_activity);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), NewDaySubjectActivity.class);
                intent.putExtra("dayName",newString);
                //intent.putExtra("activityName", "AddSubjectsActivity");
                startActivity(intent);

            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {

        Query query = cr.orderBy("lecture",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Subject2> options = new FirestoreRecyclerOptions.Builder<Subject2>().setQuery(query,Subject2.class).build();

        adapter = new Subject2Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.add_day_subjects_recyclerView);
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
