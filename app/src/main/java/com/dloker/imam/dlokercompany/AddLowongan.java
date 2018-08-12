package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddLowongan extends AppCompatActivity{
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    EditText et_jobTtl, et_jobDesc;
    Button bt_upJob;
    HashMap post = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lowongan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setActionBarTitle("Tambah Lowongan");


        et_jobTtl = (EditText) findViewById(R.id.et_judul);
        et_jobDesc = (EditText) findViewById(R.id.et_detailJob);
        bt_upJob = (Button) findViewById(R.id.bt_upJob);

        //final String jobTtl, jobDesc, jobCate, jobLoc;

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //post = new HashMap();

        db.getReference("Users").child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imgSrc, nama, loks, mail;
                        nama = dataSnapshot.child("Nama").getValue(String.class);
                        loks = dataSnapshot.child("Alamat").getValue(String.class);
                        imgSrc = dataSnapshot.child("Pict").getValue(String.class);
                        mail = dataSnapshot.child("Email").getValue(String.class);
                        Log.d("haha", nama);
                        post.put("Pict", imgSrc);
                        post.put("Alamat", loks);
                        post.put("Nama", nama);
                        post.put("Email", mail);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        Spinner spKate = (Spinner) findViewById(R.id.sp_kategori);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spKate.setAdapter(adapter);
        spKate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String kate = adapterView.getItemAtPosition(i).toString();
                post.put("Kategori", kate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spinner spLoc = (Spinner) findViewById(R.id.sp_lokasi);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.kota_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLoc.setAdapter(adapter2);
        spLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String lok = adapterView.getItemAtPosition(i).toString();
                post.put("Lokasi", lok);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        bt_upJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String jobTtl, jobDesc, idL;
                jobTtl = et_jobTtl.getText().toString();
                //jobCateg = jobCate;
                jobDesc = et_jobDesc.getText().toString();
                idL = db.getReference("Lowongan").push().getKey();


                DatabaseReference ref = db.getReference("Lowongan").child(idL);


                post.put("Judul", jobTtl);
                post.put("Desc", jobDesc);
               // post.put("Kategori", jobCateg);
                post.put("idCompany", mAuth.getCurrentUser().getUid());

                if(et_jobTtl.getText().toString().isEmpty()){
                    et_jobTtl.setError("Judul tidak boleh kosong");
                    et_jobTtl.requestFocus();
                    return;
                }

                if (et_jobDesc.getText().toString().length() < 10){
                    et_jobDesc.setError("Minimal 10 karakter");
                    return;
                }

                ref.setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(view, "LOWONGAN BERHASIL DITAMBAHKAN", Snackbar.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent i=new Intent(AddLowongan.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }, 1000);

                        }

                        else{
                            Snackbar.make(view, "FAILED", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
