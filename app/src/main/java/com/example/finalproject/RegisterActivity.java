package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.finalproject.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.Range;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

public class RegisterActivity extends AppCompatActivity {
    EditText et_User, et_Phone, et_Email, et_Pass;
    Button register;
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    AwesomeValidation mAwesomeValidation;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        activity = this;
        initViews();
        initComponents();

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAwesomeValidation.validate()){
                    String name = et_User.getText().toString();
                    String number = et_Phone.getText().toString();
                    String email = et_Email.getText().toString().trim();
                    String password = et_Pass.getText().toString();

                }

                /*else {

                    if (et_User.length() == 0 || et_Phone.length() == 0 || et_Email.length() == 0 || et_Pass.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_SHORT).show();

                    }
                }*/


                String name = binding.etUser.getText().toString();
                String number = binding.etPhone.getText().toString();
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPass.getText().toString();
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                     progressDialog.cancel();

                     firebaseFirestore.collection("Users")
                             .document(FirebaseAuth.getInstance().getUid())
                             .set(new UserModel(name,number,email));
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                            }
                        });
            }
        });
        binding.tview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });



    }
    private void initComponents(){
        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        AwesomeValidation.disableAutoFocusOnFirstFailure();
        mAwesomeValidation.addValidation(activity, R.id.et_User,RegexTemplate.NOT_EMPTY, R.string.err_name);
        mAwesomeValidation.addValidation(activity, R.id.et_Phone, RegexTemplate.TELEPHONE, R.string.err_tel);
        mAwesomeValidation.addValidation(activity, R.id.et_Email, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(activity, R.id.et_Pass,RegexTemplate.NOT_EMPTY, R.string.err_password);

        //Progress Dialog//
         progressDialog = new ProgressDialog(activity);
         progressDialog.setTitle("Please Wait");
         progressDialog.setMessage("We Are Creating Your Account ");

        //Progress Dialog//

    }
    private  void initViews (){
        et_User = findViewById(R.id.et_User);
        et_Phone = findViewById(R.id.et_Phone);
        et_Email = findViewById(R.id.et_Email);
        et_Pass = findViewById(R.id.et_Pass);

    }
}