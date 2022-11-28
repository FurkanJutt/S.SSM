package com.ulbululstudios.sssm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ulbululstudios.sssm.R;

public class LoginBottomSheet extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.design_login_bottom_sheet, container, false);

        // Cut these to show bottom sheet in any activity
        /*LoginBottomSheet loginBottomSheet = new LoginBottomSheet();
        loginBottomSheet.show(getSupportFragmentManager(), "ModalLoginBottomSheet");*/

        return view;
    }


}
