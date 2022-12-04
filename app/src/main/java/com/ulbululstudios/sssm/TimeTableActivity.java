package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ulbululstudios.sssm.Modals.TableAdapter;
import com.ulbululstudios.sssm.Modals.TimeTable;

public class TimeTableActivity extends AppCompatActivity {

    //region Declaration / Initialization
    // ---------------------------------------------------------------------------------------------
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference sectionRef = db.collection("Sections");

    private RecyclerView rvTimeTable;
    private TableAdapter tableAdapter;

    // ---------------------------------------------------------------------------------------------
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        sectionRef.document(getIntent().getStringExtra("snapshotID"))
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        setTitle(documentSnapshot.getString("department") + ": " + documentSnapshot.getString("section"));
                    } else {
                        Toast.makeText(TimeTableActivity.this, "Document doesn't exists!", Toast.LENGTH_SHORT).show();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TimeTableActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    Log.d("TimeTableActivity: ", e.toString());
                }
            });

        initVariables();



        /*rvTimeTable.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TimeTable> options =
                new FirebaseRecyclerOptions.Builder<TimeTable>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"), TimeTable.class)
                        .build();

        tableAdapter = new TableAdapter(options);
        rvTimeTable.setAdapter(tableAdapter);*/
    }

    private void initVariables() {
        rvTimeTable = findViewById(R.id.rv_time_table);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tableAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        tableAdapter.stopListening();
    }
}