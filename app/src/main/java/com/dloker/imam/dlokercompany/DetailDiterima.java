package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailDiterima extends AppCompatActivity {
    TextView tv_nama, tv_loker, tv_date, tv_gender, tv_birth, tv_addr, tv_phone, tv_prefjob, tv_disable;
    ImageView img_ava;
    Button bt_cv, bt_status;
    private FirebaseAuth.AuthStateListener mListener;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    //String nama, loker, date, gender, birth, addr, phone, prefJob, disable, idLamaran, idLowongan, idPelamar;
    String idLamaran, idPel, cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_diterima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setActionBarTitle("Detail Pelamar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage;
        final StorageReference storageRef;

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        tv_nama = (TextView) findViewById(R.id.tv_nama);
        tv_loker = (TextView) findViewById(R.id.tv_loker);
        //tv_date = (TextView) findViewById(R.id.tv_date);
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_birth = (TextView) findViewById(R.id.tv_birth);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_prefjob = (TextView) findViewById(R.id.tv_prefjob);
        tv_disable = (TextView) findViewById(R.id.tv_disable);

        bt_cv = (Button) findViewById(R.id.bt_cv);
        bt_status = findViewById(R.id.bt_status);

        img_ava = findViewById(R.id.img_avaPelamar);

        String status;
        status = getIntent().getStringExtra("status");
        idLamaran = getIntent().getStringExtra("idLamaran");
        db.getReference("Lamaran").child(idLamaran).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String idLowongan = dataSnapshot.child("idLowongan").getValue(String.class);
                String idPelamar = dataSnapshot.child("UID").getValue(String.class);
                String cv1 = dataSnapshot.child("CV").getValue(String.class);
                idPel = idPelamar;
                cv = cv1;
                //Log.d("TAG3", idPelamar);
                db.getReference("Lowongan").child(idLowongan).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String loker = dataSnapshot.child("Judul").getValue(String.class);
                        tv_loker.setText(loker);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                db.getReference("Users").child(idPelamar).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nama = dataSnapshot.child("Nama").getValue(String.class);
                        String gender = dataSnapshot.child("Gender").getValue(String.class);
                        String birth = dataSnapshot.child("TempatTanggalLahir").getValue(String.class);
                        String prefJob = dataSnapshot.child("BidangKerja").getValue(String.class);
                        String phone = dataSnapshot.child("NoTelp").getValue(String.class);
                        String addr = dataSnapshot.child("Alamat").getValue(String.class);
                        String disable = dataSnapshot.child("Disabilitas").getValue(String.class);
                        tv_nama.setText(nama);
                        //tv_loker.setText(loker);
                        tv_gender.setText(gender);
                        tv_disable.setText(disable);
                        tv_prefjob.setText(prefJob);
                        tv_birth.setText(birth);
                        tv_addr.setText(addr);
                        tv_phone.setText(phone);
                        //Picasso.get().load(dataSnapshot.child("Pict").getValue(String.class)).into(img_ava);
                        Glide.with(DetailDiterima.this)
                                .load(dataSnapshot.child("Pict").getValue()).into(img_ava);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    //Log.d(TAG, "sing in : "+user.getUid() );
                } else {
                    Toast.makeText(getApplicationContext(), "Keluar ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        };


        //Log.d("linkcv", cv+" "+idPel);

        db.getReference("Lamaran").child(idLamaran).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String UID = dataSnapshot.child("UID").getValue(String.class);

                db.getReference("Users").child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String link;
                        link = dataSnapshot.child("CV").getValue(String.class);
                        bt_cv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                final String link;
//                link = dataSnapshot.child("CV").getValue(String.class);
//                bt_cv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
//                    }
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bt_status.setText(status);


    }
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mListener != null){

            mAuth.removeAuthStateListener(mListener);
        }
    }
}
