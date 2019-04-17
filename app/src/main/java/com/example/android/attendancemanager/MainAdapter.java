package com.example.android.attendancemanager;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainAdapter extends FirestoreRecyclerAdapter<MainModel,MainAdapter.MainViewHolder> {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference subjectData = FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("subjects").collection("subjects_data");;
    private DocumentReference timeTableData =FirebaseFirestore.getInstance().collection(mAuth.getCurrentUser().getUid()).document("time_table");

    private Context context ;



    public MainAdapter(FirestoreRecyclerOptions<MainModel> options,Context context){
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull final MainViewHolder mainViewHolder, final int position, @NonNull MainModel model) {
        mainViewHolder.textView1.setText(model.getName());
        mainViewHolder.textView2.setText(model.getStatus());
        mainViewHolder.textView3.setText(model.getProgtext());
        mainViewHolder.progressBar.setProgress(model.getProgress());
        mainViewHolder.b1.setText(model.getPlus());
        mainViewHolder.b2.setText(model.getMinus());
        mainViewHolder.b3.setText(model.getCancel());
        mainViewHolder.b4.setText(model.getUndo());
        final String subjectName = mainViewHolder.textView1.getText().toString().trim();


        String dateStringNow = DateFormat.format("dd-MM-yyyy", new Date((new Date()).getTime())).toString();
        final SharedPreferences sharedPreferences = context.getSharedPreferences(dateStringNow, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor1 = sharedPreferences.edit();

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        final String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        if(sharedPreferences.getInt(String.valueOf(position),0)!=0)
            buttonsDisabled(mainViewHolder);

        mainViewHolder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentSubject(subjectName);
                markDay(position,day);
                buttonsDisabled(mainViewHolder);

                setFlag(1,editor1,position);
            }
        });
        mainViewHolder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                absentSubject(subjectName);
                markDay(position,day);
                buttonsDisabled(mainViewHolder);

                setFlag(2,editor1, position);
            }
        });
        mainViewHolder.b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markDay(position,day);
                buttonsDisabled(mainViewHolder);
                setFlag(3,editor1, position);
            }
        });
        mainViewHolder.b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = sharedPreferences.getInt(String.valueOf(position),0);
                switch(flag){
                    case 1: undoPresent(subjectName);
                            unmarkDay(position,day,mainViewHolder,editor1);
                            break;
                    case 2: undoAbsent(subjectName);
                            unmarkDay(position,day, mainViewHolder, editor1);
                            break;
                    case 3: unmarkDay(position,day, mainViewHolder, editor1);
                            break;
                    default:
                }

            }
        });


    }

    private void unmarkDay(int position, final String day, MainViewHolder mainViewHolder, SharedPreferences.Editor editor1) {
        timeTableData.collection(day).whereEqualTo("lecture",position).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                        timeTableData.collection(day).document(snapshot.getId()).update("marked","NO");
                }
            }
        });
        buttonsEnabled(mainViewHolder);
        setFlag(0,editor1,position);
    }

    private void markDay(int position, final String day) {
        timeTableData.collection(day).whereEqualTo("lecture",position).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot snapshot: task.getResult())
                        timeTableData.collection(day).document(snapshot.getId()).update("marked","YES");
                }
            }
        });
    }

    private void buttonsEnabled(MainViewHolder mainViewHolder) {
        mainViewHolder.b2.setEnabled(true);
        mainViewHolder.b1.setEnabled(true);
        mainViewHolder.b3.setEnabled(true);
        mainViewHolder.b1.setBackgroundColor(Color.parseColor("#00aa00"));
        mainViewHolder.b2.setBackgroundColor(Color.RED);
        mainViewHolder.b3.setBackgroundColor(Color.parseColor("#d23456"));
    }


    private void setFlag(int flag, SharedPreferences.Editor editor1, int position)
    {
        editor1.putInt(String.valueOf(position),flag);
        editor1.apply();
    }

    private void undoAbsent(String subjectName) {
        subjectData.whereEqualTo("name",subjectName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document: task.getResult())
                    {
                        String[] need = getDataAfterUndoAbsent(document);
                        updateSubject(Integer.valueOf(need[0]),Integer.valueOf(need[1]),Float.valueOf(need[2]),document.getId());


                    }
                }
            }
        });


    }

    private String[] getDataAfterUndoAbsent(DocumentSnapshot document) {
        int attended = (int) (long) document.get("attended_lectures");
        int total = (int) (long) document.get("total_lectures");

        total-=1;
        float percentage;
        if(total <= 0) {
            percentage = 0;
            total = 0;
        }

        else {
            percentage = ((float) attended / (float) total) * 100;
            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
        }
        String[] need = {String.valueOf(attended),String.valueOf(total),String.valueOf(percentage)};
        return need;
    }

    private void undoPresent(String subjectName) {
        subjectData.whereEqualTo("name",subjectName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document: task.getResult())
                    {
                        String[] need = getDataAfterUndoPresent(document);
                        updateSubject(Integer.valueOf(need[0]),Integer.valueOf(need[1]),Float.valueOf(need[2]),document.getId());


                    }
                }
            }
        });

    }
    private String[] getDataAfterUndoPresent(DocumentSnapshot document) {
        int attended = (int) (long) document.get("attended_lectures");
        int total = (int) (long) document.get("total_lectures");
        attended-=1;
        total-=1;
        float percentage;
        if(total==0)
            percentage = 0;
        else {
            percentage = ((float) attended / (float) total) * 100;
            percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
        }
        String[] need = {String.valueOf(attended),String.valueOf(total),String.valueOf(percentage)};
        return need;
    }



    private void buttonsDisabled(MainViewHolder mainViewHolder) {
        mainViewHolder.b2.setEnabled(false);
        mainViewHolder.b1.setEnabled(false);
        mainViewHolder.b3.setEnabled(false);
        mainViewHolder.b1.setBackgroundColor(Color.GRAY);
        mainViewHolder.b2.setBackgroundColor(Color.GRAY);
        mainViewHolder.b3.setBackgroundColor(Color.GRAY);
    }

    private void absentSubject(final String name) {
        subjectData.whereEqualTo("name",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(DocumentSnapshot document: task.getResult())
                    {
                        String[] need = absent(document);
                        updateSubject(Integer.valueOf(need[0]),Integer.valueOf(need[1]),Float.valueOf(need[2]),document.getId());
                    }
                }

            }
        });
    }

    private String[] absent(DocumentSnapshot document)
    {
        int attended = (int) (long) document.get("attended_lectures");
        int total = (int) (long) document.get("total_lectures");
        total+=1;
        float percentage = ((float)attended/(float)total)*100;
        percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
        String[] need={String.valueOf(attended),String.valueOf(total),String.valueOf(percentage)};
        return need;

    }



    private void presentSubject(final String name) {
        subjectData.whereEqualTo("name",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String[] need = present(document);
                        updateSubject(Integer.valueOf(need[0]),Integer.valueOf(need[1]),Float.valueOf(need[2]), document.getId());
                    }
                }

            }
        });


    }

    private String[] present(DocumentSnapshot document) {
        int attended = (int) (long) document.get("attended_lectures");
        int total = (int) (long) document.get("total_lectures");

        attended += 1;
        total += 1;
        float percentage = ((float) attended / (float) total) * 100;
        percentage = Float.valueOf(new DecimalFormat("#.##").format(percentage));
        String[] need={String.valueOf(attended),String.valueOf(total),String.valueOf(percentage)};
        return need;
    }


    private void updateSubject(int attended, int total, float percentage, String id) {
        subjectData.document(id).update(
                "attended_lectures",attended,
                "total_lectures",total,
                "percentage",percentage
        );
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.subjectcards,viewGroup,false);
        return new MainViewHolder(view);
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        TextView textView1,textView2,textView3;
        Button b1,b2,b3,b4;
        ProgressBar progressBar;
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.t1);
            textView2=itemView.findViewById(R.id.t2);
            textView3=itemView.findViewById(R.id.t3);
            progressBar=itemView.findViewById(R.id.progressBar);
            b1=itemView.findViewById(R.id.b1);
            b2=itemView.findViewById(R.id.b2);
            b3=itemView.findViewById(R.id.b3);
            b4=itemView.findViewById(R.id.b4);
        }
    }
}
