package com.ulbululstudios.sssm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Firebase Authentication variables
    private FirebaseAuth mAuth;

    // BottomSheet Variables
    private BottomSheetBehavior bottomSheetBehavior;
    private MaterialTextView mtrltvArrowIcon;
    private TextInputEditText etLoginEmail, etLoginPassword ;
    private TextView lblTroubleText, tvForgotPassword;
    private Button btnLogin;
    private ProgressBar pbLogin;

    // LoginActivity variables
    private MaterialButton btnSendEmail;
    private TextInputEditText etSendEmail;
    private TextView tvContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        setTitle("Student State Management");

        mAuth = FirebaseAuth.getInstance();

        etSendEmail = findViewById(R.id.et_send_email);
        btnSendEmail = findViewById(R.id.btn_send_email);
        btnSendEmail.setOnClickListener(this);
        tvContactUs = findViewById(R.id.tv_contact_us);

        SetBottomSheetView();
    }

    private void SetBottomSheetView() {
        FrameLayout fl = findViewById(R.id.fl_login_bottom_sheet);
        final LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout flc = (ConstraintLayout) inflater.inflate(R.layout.design_login_bottom_sheet,null);

        fl.addView(flc);

        mtrltvArrowIcon = flc.findViewById(R.id.tv_arrow_icon);
        lblTroubleText = flc.findViewById(R.id.lbl_trouble_text);

        etLoginEmail = flc.findViewById(R.id.et_login_email);
        etLoginPassword = flc.findViewById(R.id.et_login_password);
        btnLogin = flc.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        tvForgotPassword = flc.findViewById(R.id.tv_forgot_password);
        tvForgotPassword.setOnClickListener(this);
        pbLogin = flc.findViewById(R.id.pb_login);

        String tmp = lblTroubleText.getText().toString();
        bottomSheetBehavior = BottomSheetBehavior.from(fl);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    mtrltvArrowIcon.setRotationX(0);
                    lblTroubleText.setText(tmp);
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setPeekHeight(150);
                    mtrltvArrowIcon.setRotationX(180);
                    lblTroubleText.setText("Trouble Solved! Swipe Up to Login");
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_password:
                Toast.makeText(this, "forgot", Toast.LENGTH_SHORT).show();
                bottomSheetBehavior.setPeekHeight(150);
                break;
            case R.id.btn_login:
                LoginUser();
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_send_email:
                Toast.makeText(this, "send email", Toast.LENGTH_SHORT).show();
                bottomSheetBehavior.setPeekHeight(125);
                break;
            default:
                break;
        }
    }

    private void LoginUser() {
        String email = etLoginEmail.getText().toString().trim().toLowerCase();
        String password = etLoginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etLoginEmail.setError("Email Required!");
            etLoginEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etLoginEmail.setError("Invalid Email");
            etLoginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etLoginPassword.setError("Password Required!");
            etLoginPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etLoginPassword.setError("Min 6 characters required!");
            etLoginPassword.requestFocus();
            return;
        }

        pbLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (!user.isEmailVerified()) {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Please verify your email first!", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Failed to login! Check your credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}