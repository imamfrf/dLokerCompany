package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity {

    ImageView pictProfil;
    TextView tv_nama, tv_email, tv_alamat, tv_web;
    FloatingActionButton fab_edit;
    //firebase

    FirebaseAuth mAuth;
    FirebaseDatabase db;

    //FirebaseUser mUser;

    //DatabaseReference mRef;

    //firebase storage
    FirebaseStorage storage;
    StorageReference storageReference;

    Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profil Perusahaan");
//        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();

//        mUser = mAuth.getCurrentUser();
//
//        mRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        db = FirebaseDatabase.getInstance();

        //image
        pictProfil = (ImageView) findViewById(R.id.profile_pict);

        //textview
        tv_nama = (TextView) findViewById(R.id.profile_nama);
        tv_email = (TextView) findViewById(R.id.profile_email);
        tv_alamat = (TextView) findViewById(R.id.tv_alamat);
        tv_web = (TextView) findViewById(R.id.tv_web);

        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit_profile);

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, EditProfile.class);
                startActivity(intent);

                /*if(isOpen){
                    FragmentManager fm  = getFragmentManager();
                    fm.beginTransaction().remove(ep).commit();

                    isOpen = false;
                } else {*/
//                Intent intent = new Intent(getActivity(), EditProfile.class);
//                startActivity(intent);
                    /*FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.main_frame, ep).commit();*/
                    /*isOpen = true;
                }*/
            }
        });

        db.getReference("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nama, email, alamat, web, url;
                nama = dataSnapshot.child("Nama").getValue(String.class);
                email = dataSnapshot.child("Email").getValue(String.class);
                alamat = dataSnapshot.child("Alamat").getValue(String.class);
                web = dataSnapshot.child("Website").getValue(String.class);
                url = dataSnapshot.child("Pict").getValue().toString();
                Glide.with(Profile.this)
                        .load(url)
                        .into(pictProfil);

                Log.d("aduh", nama);
                tv_nama.setText(nama);
                tv_email.setText(email);
                tv_alamat.setText(alamat);
                tv_web.setText(web);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //getSupportActionBar().setTitle("Profil");

    }


//    @Override
//    public boolean onMenuItemSelected(int featureId, MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//
//        return false;
//    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}

