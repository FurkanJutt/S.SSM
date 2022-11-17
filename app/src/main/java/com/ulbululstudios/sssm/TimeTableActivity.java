package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.ulbululstudios.sssm.Modals.TableAdapter;
import com.ulbululstudios.sssm.Modals.TimeTable;

public class TimeTableActivity extends AppCompatActivity {

    private RecyclerView rvTimeTable;
    private TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        initVariables();

        rvTimeTable.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<TimeTable> options =
                new FirebaseRecyclerOptions.Builder<TimeTable>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("teachers"), new SnapshotParser<TimeTable>() {
                            @NonNull
                            @Override
                            public TimeTable parseSnapshot(@NonNull DataSnapshot snapshot) {
                                snapshot.child("t1").child("name");
                                return null;
                            }
                        })
                        .build();

        tableAdapter = new TableAdapter(options);
        rvTimeTable.setAdapter(tableAdapter);
    }

    private void initVariables() {
        rvTimeTable = findViewById(R.id.rv_time_table);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tableAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tableAdapter.stopListening();
    }
}