package com.example.android.attendancemanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText confirm_pass;
    private Button register;
    private FirebaseAuth mAuth;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.reg_email_id);
        password = findViewById(R.id.reg_password);
        register = findViewById(R.id.register_btn);
        confirm_pass = findViewById(R.id.confirm_pass);
        login = findViewById(R.id.login_btn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId = email.getText().toString();
                String pass = password.getText().toString();
                String confirm = confirm_pass.getText().toString();
                if(!TextUtils.isEmpty(emailId) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm)) {
                    if (pass.equals(confirm)) {

                        mAuth.createUserWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                                Toast.makeText(RegisterActivity.this,"registered Succesfully. Please verify your email address",Toast.LENGTH_LONG).show();

                                            else {

                                                Toast.makeText(RegisterActivity.this,
                                                        task.getException().getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else

                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();
                                }
                            }

                        });
                    } else
                        Toast.makeText(RegisterActivity.this, "entered passwords do not match", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(RegisterActivity.this,"please complete above fields",Toast.LENGTH_LONG).show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }



    private void sendToMain() {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();

    }
}
