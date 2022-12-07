package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.ulbululstudios.sssm.Modals.Section;
import com.ulbululstudios.sssm.Modals.SectionAdapter;
import com.ulbululstudios.sssm.Modals.TableAdapter;
import com.ulbululstudios.sssm.Modals.TimeTable;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeTableActivity extends AppCompatActivity {

    //region Declaration / Initialization
    // ---------------------------------------------------------------------------------------------
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference sectionRef = db.collection("Sections");
    private final CollectionReference timetableRef = db.collection("TimeTables");
    private final CollectionReference staticSubjectRef = db.collection("static_subjects");
    private final CollectionReference staticTeacherRef = db.collection("static_teachers");

    private RecyclerView rvTimeTable;
    private TableAdapter tableAdapter;

    private String department, section;

    // Views Declaration
    private ExtendedFloatingActionButton fabAddNewSubject;
    private ImageView ivCloseSubjectTab;

    // Dropdown menu
    private ArrayAdapter<String> itemAdapter;
    private AutoCompleteTextView subjectSelect, teacherSelect, timeFromSelect, timeToSelect, sectionSelect;
    private ArrayList<String> subjectList, teacherList, timeList;
    private MaterialButton btnAddSubject;
    private TimePickerDialog timePicker;


    // ---------------------------------------------------------------------------------------------
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        initVariables();

        //region Startup Processing
        sectionRef.document(getIntent().getStringExtra("snapshotID"))
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        department = documentSnapshot.getString("department");
                        section = documentSnapshot.getString("section");
                        setTitle(department + ": " + section);
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
        //endregion

        SetupTableRecyclerView();
        addSubjectListener();

        // TODO: remove
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
        fabAddNewSubject = findViewById(R.id.fab_add_new_subject);
    }

    private void SetupTableRecyclerView() {
        Query query = timetableRef.orderBy("subject");

        FirestoreRecyclerOptions<TimeTable> options = new FirestoreRecyclerOptions.Builder<TimeTable>()
                .setQuery(query, TimeTable.class)
                .build();

        tableAdapter = new TableAdapter(options);

        rvTimeTable.setHasFixedSize(true);
        rvTimeTable.setLayoutManager(new LinearLayoutManager(this));
        rvTimeTable.setAdapter(tableAdapter);
    }

    private void addSubjectListener() {
        fabAddNewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region display DialogPlus
                final DialogPlus dialogPlus = DialogPlus.newDialog(TimeTableActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.design_add_subject_bottom_sheet))
                        .setExpanded(true, 920)
                        .create();

                dialogPlus.show();

                View v = dialogPlus.getHolderView();

                ivCloseSubjectTab = v.findViewById(R.id.iv_close_subject_tab);
                ivCloseSubjectTab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
                //endregion

                //region select SUBJECT (dropdown)
                subjectSelect = v.findViewById(R.id.dropdown_subject_select);
                subjectList = new ArrayList<>();

                itemAdapter = new ArrayAdapter<String>(TimeTableActivity.this, R.layout.design_dropdown_item, subjectList);
                staticSubjectRef.orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                subjectList.add(documentSnapshot.getString("name"));
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                subjectSelect.setAdapter(itemAdapter);

                // TODO: remove onItemClick()
                subjectSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                //endregion

                //region select TEACHER (dropdown)
                teacherSelect = v.findViewById(R.id.dropdown_teacher_select);
                teacherList = new ArrayList<>();

                itemAdapter = new ArrayAdapter<String>(TimeTableActivity.this, R.layout.design_dropdown_item, teacherList);
                staticTeacherRef.orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                teacherList.add(documentSnapshot.getString("name"));
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                teacherSelect.setAdapter(itemAdapter);

                // TODO: remove onItemClick()
                teacherSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                //endregion

                //region select TIMEFROM (dropdown)
                timeFromSelect = v.findViewById(R.id.dropdown_time_from_select);
                timeFromSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cldr = Calendar.getInstance();
                        int hour = cldr.get(Calendar.HOUR_OF_DAY);
                        int minutes = cldr.get(Calendar.MINUTE);
                        timePicker = new TimePickerDialog(TimeTableActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hrs, int mins) {
                                timeFromSelect.setText(hrs + " : " + mins);
                            }
                        }, hour, minutes, false);
                        timePicker.show();
                    }
                });
                //endregion

                //region select TIMETO (dropdown)
                timeToSelect = v.findViewById(R.id.dropdown_time_to_select);
                timeToSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cldr = Calendar.getInstance();
                        int hour = cldr.get(Calendar.HOUR_OF_DAY);
                        int minutes = cldr.get(Calendar.MINUTE);
                        timePicker = new TimePickerDialog(TimeTableActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hrs, int mins) {
                                timeToSelect.setText(hrs + " : " + mins);
                            }
                        }, hour, minutes, false);
                        timePicker.show();
                    }
                });
                //endregion

                //region add SUBJECT to database (button)
                btnAddSubject = v.findViewById(R.id.btn_add_subject);
                btnAddSubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sub = subjectSelect.getText().toString();
                        String tch = teacherSelect.getText().toString();
                        String timefrom = timeFromSelect.getText().toString();
                        String timeto = timeToSelect.getText().toString();

                        if (sub.isEmpty() || tch.isEmpty() || timefrom.isEmpty() || timeto.isEmpty()) {
                            Toast.makeText(TimeTableActivity.this, "Cannot add empty fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String timeset = timefrom+" - "+timeto;
                        String room = "room";

                        // Check if data already exists in database
                        //Query query = sectionRef.whereEqualTo("department", dep).whereEqualTo("section", sem_sec);
                        timetableRef.document(department).collection(section).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                boolean isExisting = false;
                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                    if (ds.getString("subject").equals(sub) && ds.getString("teacher").equals(tch) && ds.getString("time").equals(timeset)) {
                                        Toast.makeText(TimeTableActivity.this, "Subject Already Exists!", Toast.LENGTH_SHORT).show();
                                        isExisting = true;
                                    }
                                }
                                if (!isExisting) {
                                    timetableRef.document(department).collection(section).add(new TimeTable(timeset, sub, room, tch));
                                    Toast.makeText(TimeTableActivity.this, "Subject Added!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TimeTableActivity.this, "Subject Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //endregion
            }
        });
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