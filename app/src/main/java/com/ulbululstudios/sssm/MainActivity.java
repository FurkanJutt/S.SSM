package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.ulbululstudios.sssm.Modals.Section;
import com.ulbululstudios.sssm.Modals.SectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //region Declaration / Initialization
    // ---------------------------------------------------------------------------------------------
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference sectionRef = db.collection("Sections");
    private final CollectionReference timetableRef = db.collection("TimeTables");
    private final CollectionReference staticDepartmentRef = db.collection("static_department");
    private final CollectionReference staticSemesterRef = db.collection("static_semester");
    private final CollectionReference staticSectionRef = db.collection("static_section");
    private final CollectionReference staticBatchRef = db.collection("static_batch");

    private SectionAdapter sectionAdapter;
    private RecyclerView recyclerView;

    // Views Declaration
    private ExtendedFloatingActionButton fabAddNewSection;
    private ImageView ivCloseSectionTab;
    private MaterialButton btnGenerateTimeTable;

    // Dropdown menu
    private ArrayAdapter<String> itemAdapter;
    private AutoCompleteTextView departmentSelect, semesterSelect, sectionSelect;
    private ArrayList<String> departmentList, semesterList, sectionList;
    private MaterialButton btnAddSection;

    // ---------------------------------------------------------------------------------------------
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Section Grid");
        
        initVariables();
        SetupMainRecyclerView();
        addSectionListener();
        GenerateTimeTable();
    }

    private void initVariables() {
        recyclerView = findViewById(R.id.rv_section_grid);
        fabAddNewSection = findViewById(R.id.fab_add_new_section);
        btnGenerateTimeTable = findViewById(R.id.btn_generate_timetable);
    }

    private void SetupMainRecyclerView() {
        Query query = sectionRef.orderBy("section");

        FirestoreRecyclerOptions<Section> options = new FirestoreRecyclerOptions.Builder<Section>()
                .setQuery(query, Section.class)
                .build();

        sectionAdapter = new SectionAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(sectionAdapter);

        //region swipe to delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Entry?")
                        .setMessage("Are you sure you want to permanently delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sectionAdapter.DeleteItem(viewHolder.getAbsoluteAdapterPosition());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setIcon(R.drawable.ic_round_error_outline_24)
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
        //endregion

        sectionAdapter.setOnItemClickListener(new SectionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                Section section = documentSnapshot.toObject(Section.class);
                String id = documentSnapshot.getId();
                String d = documentSnapshot.getString("department");
                String s = documentSnapshot.getString("section");
                Intent sectionIntent = new Intent(MainActivity.this, TimeTableActivity.class);
                sectionIntent.putExtra("snapshotID", id);
                sectionIntent.putExtra("snapshotDep", d);
                sectionIntent.putExtra("snapshotSec", s);
                startActivity(sectionIntent);

                Toast.makeText(MainActivity.this, "ID: " + id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSectionListener() {
        fabAddNewSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //region display DialogPlus
                final DialogPlus dialogPlus = DialogPlus.newDialog(MainActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.design_add_section_bottom_sheet))
                        .setExpanded(true, 730)
                        .create();

                dialogPlus.show();

                View v = dialogPlus.getHolderView();

                ivCloseSectionTab = v.findViewById(R.id.iv_close_section_tab);
                ivCloseSectionTab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });
                //endregion

                //region select DEPARTMENT (dropdown)
                departmentSelect = v.findViewById(R.id.dropdown_department_select);
                departmentList = new ArrayList<>();

                itemAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.design_dropdown_item, departmentList);
                staticDepartmentRef.orderBy("department").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                departmentList.add(documentSnapshot.getString("department"));
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                departmentSelect.setAdapter(itemAdapter);

                // TODO: remove onItemClick()
                departmentSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                //endregion

                //region select SEMESTER (dropdown)
                semesterSelect = v.findViewById(R.id.dropdown_semester_select);
                semesterList = new ArrayList<>();

                itemAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.design_dropdown_item, semesterList);
                staticSemesterRef.orderBy("semester").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                semesterList.add(documentSnapshot.getString("semester"));
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                semesterSelect.setAdapter(itemAdapter);

                // TODO: remove onItemClick()
                semesterSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                //endregion

                //region select SECTION (dropdown)
                sectionSelect = v.findViewById(R.id.dropdown_section_select);
                sectionList = new ArrayList<>();

                itemAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.design_dropdown_item, sectionList);
                staticSectionRef.orderBy("section").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                sectionList.add(documentSnapshot.getString("section"));
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                    }
                });
                sectionSelect.setAdapter(itemAdapter);

                // TODO: remove onItemClick()
                sectionSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
                    }
                });
                //endregion

                //region add SECTION to database (button)
                btnAddSection = v.findViewById(R.id.btn_add_section);
                btnAddSection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dep = departmentSelect.getText().toString();
                        String sem = semesterSelect.getText().toString();
                        String sec = sectionSelect.getText().toString();

                        if (dep.isEmpty() || sem.isEmpty() || sec.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Cannot add empty fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String sem_sec = sem+sec;

                        // Check if data already exists in database
                        //Query query = sectionRef.whereEqualTo("department", dep).whereEqualTo("section", sem_sec);
                        sectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                boolean isExisting = false;
                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                        if (ds.getString("department").equals(dep) && ds.getString("section").equals(sem_sec)) {
                                            Toast.makeText(MainActivity.this, "Section Already Exists!", Toast.LENGTH_SHORT).show();
                                            isExisting = true;
                                        }
                                }
                                if (!isExisting) {
                                    sectionRef.add(new Section(dep, sem_sec));
                                }
                            }
                        });
                    }
                });
                //endregion
            }
        });
    }

    private void GenerateTimeTable() {
        btnGenerateTimeTable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*timetableRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (QueryDocumentSnapshot qs : value) {
                            List<DocumentSnapshot> ds = value.getDocuments();
                            Toast.makeText(MainActivity.this, ds.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                ///
                // https://firebase.google.com/docs/firestore/query-data/get-data
                ///
                timetableRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    String str;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot qs : task.getResult()) {
                                Log.i("docName: ", qs.getId());
                                Log.d("docName: ", qs.getId());
                                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sectionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sectionAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new AlertDialog.Builder(MainActivity.this)
            .setTitle("Logout?")
            .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            })
            .setNegativeButton("Cancel", null)
            .setIcon(R.drawable.ic_round_error_outline_24)
            .show();
    }
}