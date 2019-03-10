package com.example.android.attendancemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button login;
    private Button register;
    private TextView forgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.reg_password);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);
        forgotPass = findViewById(R.id.forgot);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = email.getText().toString();
                String pass = password.getText().toString();

                if(!TextUtils.isEmpty(emailId)&& !TextUtils.isEmpty(pass)){
                    mAuth.signInWithEmailAndPassword(emailId,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                                sendToMain();

                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error:"+message,Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }
                else
                    Toast.makeText(LoginActivity.this,"Error:Please enter above fields",Toast.LENGTH_LONG).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                finish();
            }
        });



    }



    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
