package com.example.android.attendancemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private FirebaseFirestore db ;
    private FirebaseUser currentUser;
    RecyclerView recyclerView;
    final Context context=this;
    private MainAdapter adapter;

    private CollectionReference cr;
    private CollectionReference cr1;
    private CollectionReference cr2;
    private DocumentReference timeTableData;
    private String subject;
    private int lecture;
    private TextView todayday;
    private TextView todaydate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //check authentication of current user
        currentUser = mAuth.getCurrentUser();
        if(currentUser==null || !currentUser.isEmailVerified())
            sentToLogin();

        cr1 = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("history_data");
        cr2 = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("previous_date");
        timeTableData =FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table");

        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);

        todaydate = findViewById(R.id.date_id);
        todayday = findViewById(R.id.day_id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        todaydate.setText(dateFormat.format(new Date()));
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String stringDate = sdf.format(new Date());
        todayday.setText(stringDate);

        //get yesterday's DAYNAME
        final String previousDay = previousDay(date);
        //get yesterday's DATE
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date oldDate = cal.getTime();
        final String previousDate = dateFormat.format(oldDate);
        //get two day's before DAYNAME
        cal.add(Calendar.DATE,-2);
        Date olderDate = cal.getTime();
        String twoDayBeforeDate = dateFormat.format(olderDate);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        switch (menuItem.getItemId()){
                            case R.id.nav_subjects:
                                startActivity(new Intent(MainActivity.this,AddSubjectsActivity.class));
                                break;
                            case R.id.nav_time_table:
                                startActivity(new Intent(MainActivity.this,TimeTableActivity.class));
                                break;
                            case R.id.nav_criteria:
                                startActivity(new Intent(MainActivity.this,Criteria.class));
                                break;
                            case R.id.nav_history:
                                startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                                break;
                            case R.id.nav_current_staus:
                                startActivity(new Intent(MainActivity.this,CurrentStatusActivity.class));

                        }
                        return true;
                    }
                });

        setupRecyclerView();
        SharedPreferences sp = getSharedPreferences("dayOfAttendance",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(dateFormat.format(new Date()),1);
        editor.apply();
        if(sp.getInt(previousDate,0)==1) {
            SharedPreferences sharedPreferences = getSharedPreferences("history", Context.MODE_PRIVATE);
            //sharedPreferences.edit().clear().apply();
            if (sharedPreferences.getInt(previousDate, 0) != 1)
                checkHistory(previousDate, previousDay);
            if (sharedPreferences.getInt(twoDayBeforeDate, 0) == 1)
                sharedPreferences.edit().remove(twoDayBeforeDate).apply();
        }

    }

    private void checkHistory(final String previousDate, final String previousDay) {
        cr1.whereEqualTo("date",previousDate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().isEmpty()) {
                        updateHistory(previousDay, previousDate);
                        unmarkDay(previousDay,previousDate);
                    }
                }
            }
        });
    }

    private void unmarkDay(final String previousDay, String previousDate) {
        timeTableData.collection(previousDay).whereEqualTo("marked","YES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                        timeTableData.collection(previousDay).document(snapshot.getId()).update("marked","NO");
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("today"+previousDate,Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();

    }
    private void updateHistory( final String previousDay, final String previousDate) {

        timeTableData.collection(previousDay).whereEqualTo("marked", "NO")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(!task.getResult().isEmpty()) {
                        cr2.add(new History(previousDate));
                        for (DocumentSnapshot document : task.getResult()) {
                            subject = document.getString("name");
                            lecture = Integer.parseInt(String.valueOf(document.get("lecture")));
                            cr1.add(new History(subject, lecture, previousDate));
                        }
                    }
                }
            }
        });
    }

    private String previousDay(Date dateFormat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateFormat);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(cal.getTime());}

    private void setupRecyclerView() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String stringDate = sdf.format(new Date());
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(stringDate);

        Query query = cr.orderBy("lecture", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MainModel> options = new FirestoreRecyclerOptions.Builder<MainModel>().setQuery(query, MainModel.class).build();

        adapter = new MainAdapter(options,this);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        sentToLogin();
        adapter.startListening();
    }

    private void sentToLogin() {
        if(currentUser==null ) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(!currentUser.isEmailVerified())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("email not verified")
                    .setCancelable(false)
                    .setPositiveButton("Verified", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout_btn:
                    logout();
                    return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
                default:
                    return false;
        }
    }
    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
