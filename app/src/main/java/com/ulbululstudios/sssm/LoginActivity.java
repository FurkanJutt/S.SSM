package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ulbululstudios.sssm.Fragments.LoginBottomSheet;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        FrameLayout fl = findViewById(R.id.fl_login_bottom_sheet);
        final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout flc = (ConstraintLayout) inflater.inflate(R.layout.login_bottom_sheet,null);

        fl.addView(flc);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(fl);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setPeekHeight(150);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        /*LoginBottomSheet loginBottomSheet = new LoginBottomSheet();
        loginBottomSheet.show(getSupportFragmentManager(), "ModalLoginBottomSheet");*/
    }
}