package com.example.android.attendancemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Locale.US;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    RecyclerView recyclerView;
    final Context context=this;
    MainAdapter adapter;
    List<MainModel> proList=new ArrayList<>();
    List<DocumentSnapshot> subjects = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView =findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String stringDate = sdf.format(new Date());
        FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(stringDate).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            subjects = task.getResult().getDocuments();
                            for (int i = 0; i < subjects.size(); i++) {
                                proList.add(new MainModel((Long) subjects.get(i).get("priority"), subjects.get(i).get("name").toString(), "Status: On Track", 45, "+", "-", "45"));

                            }
                        }
                        else
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("no record for today");
                            alertDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MainActivity.this,TimeTableActivity.class));

                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                });
        adapter=new MainAdapter(this,proList);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
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
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sentToLogin();
    }

    private void sentToLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
        sentToLogin();
    }
}
