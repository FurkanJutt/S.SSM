package com.ulbululstudios.sssm.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ulbululstudios.sssm.MainActivity;
import com.ulbululstudios.sssm.Modals.Department;
import com.ulbululstudios.sssm.Modals.Section;
import com.ulbululstudios.sssm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AddSectionBottomSheet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_add_section_bottom_sheet);
    }

}