package com.example.android.attendancemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private ListView myListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = findViewById(R.id.listView);
        adapter= new ArrayAdapter<String>(SubjectsActivity.this,android.R.layout.simple_list_item_1,listItems);
        myListView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListItem();
                Snackbar.make(view, "added", Snackbar.LENGTH_LONG)
                        .setAction("Undo", undoOnClickListener).show();
            }
        });

    }

    View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listItems.remove(listItems.size()-1);
            adapter.notifyDataSetChanged();
            Snackbar.make(view,"item removed",Snackbar.LENGTH_LONG).setAction("Action",null).show();

        }
    };


    private void addListItem(){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_subject_prompt,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final EditText input = promptsView.findViewById(R.id.subject_name);

        alertDialogBuilder.setCancelable(false).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = input.getText().toString();
                listItems.add(name);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
