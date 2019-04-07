package com.example.android.attendancemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    List<MainModel> proList=new ArrayList<>();
    List<DocumentSnapshot> subjects = new ArrayList<>();
    CollectionReference cr;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);


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
                        }
                        return true;
                    }
                });
        //sentToLogin();
        currentUser = mAuth.getCurrentUser();
        if(currentUser==null || !currentUser.isEmailVerified())
            sentToLogin();
        else
            setupRecyclerView();
    }

    private void setupRecyclerView() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String stringDate = sdf.format(new Date());
        cr = db.collection(mAuth.getCurrentUser().getUid()).document("time_table").collection(stringDate);


        Query query = cr.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MainModel> options = new FirestoreRecyclerOptions.Builder<MainModel>().setQuery(query, MainModel.class).build();

        adapter = new MainAdapter(options,this);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) Objects.requireNonNull(recyclerView.getItemAnimator())).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        /*catch (Exception e)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("alert");
            dialog.setMessage("no record for today exists,got to time table activity for entering records");
            dialog.setPositiveButton("OK",null);
            dialog.show();

        }*/
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

        //finish();
    }
}
