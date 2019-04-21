package com.example.android.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class HistoryAdapter extends FirestoreRecyclerAdapter<History, HistoryAdapter.HistoryHolder> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference cr = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("history_data");
    private CollectionReference cr2 = db.collection(mAuth.getCurrentUser().getUid()).document("history").collection("previous_date");



    private Context context ;
    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<History> options, Context context) {
        super(options);

        this.context = context;

    }
    //@Override
    protected void onBindViewHolder(@NonNull HistoryHolder holder, int position, @NonNull final History model) {

            holder.date.setText(model.getDate());

            //holder.cancel.setText("cancel");
            holder.done.setText("done");
            holder.date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(),PreviousDateActivity.class);
                    intent.putExtra("date",model.getDate());
                    view.getContext().startActivity(intent);
                }
            });
            holder.done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cr.whereEqualTo("date",model.getDate()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot: task.getResult())
                                {
                                    cr.document(snapshot.getId()).delete();
                                }
                            }
                        }
                    });
                    cr2.whereEqualTo("date",model.getDate()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot: task.getResult())
                                    cr2.document(snapshot.getId()).delete();
                            }
                        }
                    });

                    SharedPreferences sharedPreferences1 = context.getSharedPreferences("previous"+model.getDate(),Context.MODE_PRIVATE);
                    sharedPreferences1.edit().clear().apply();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("history",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(model.getDate(),1);
                    editor.apply();
                }
            });

    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_card,viewGroup,false);
        return new HistoryAdapter.HistoryHolder(v);
    }

    public String deleteItem(int position)
    {
        History holder = getItem(position);
        String date = holder.getDate();

        getSnapshots().getSnapshot(position).getReference().delete();
        return date;
    }



    class HistoryHolder extends RecyclerView.ViewHolder{
        TextView date;
        //Button cancel;
        Button done;
        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            done = itemView.findViewById(R.id.done_button);
            //cancel = itemView.findViewById(R.id.cancel_button);

        }


    }
}