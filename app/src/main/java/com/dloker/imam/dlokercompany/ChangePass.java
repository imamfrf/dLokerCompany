package com.dloker.imam.dlokercompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePass extends AppCompatActivity {
    EditText et_oldPass, et_newPass;
    Button bt_savePass;
    FirebaseAuth mAuth;
    //FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Ubah Kata Sandi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        et_oldPass = findViewById(R.id.et_oldPass);
        et_newPass = findViewById(R.id.et_newPass);

        bt_savePass = findViewById(R.id.bt_savePass);



        bt_savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String mail = mAuth.getCurrentUser().getEmail();

                if (et_oldPass.getText().toString().isEmpty()) {
                    et_oldPass.setError("Tidak boleh kosong");
                }

                if (et_newPass.getText().toString().length() < 6) {
                    if (et_newPass.getText().toString().isEmpty()) {
                        et_newPass.setError("Tidak boleh kosong");
                    } else {
                        et_newPass.setError("Minimal 6 karakter");
                    }
                } else {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mail, et_oldPass.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                    mAuth.getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getCurrentUser().updatePassword(et_newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("ganti", "Password updated");
                                                    Toast.makeText(ChangePass.this, "Kata Sandi Berhasil Diubah", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(ChangePass.this, MainActivity.class);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    } else {
                                        Log.d("ganti", "Error password not updated");
                                        Toast.makeText(ChangePass.this, "Kata Sandi Yang Anda Masukkan Salah", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
                finish();
                return true;
            }
        }

