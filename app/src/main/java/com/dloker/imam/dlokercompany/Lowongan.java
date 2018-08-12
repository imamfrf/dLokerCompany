package com.dloker.imam.dlokercompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Lowongan extends AppCompatActivity {
    TextView tv_judul, tv_company, tv_kota, tv_desc;
    ImageView img_logo;
    Button bt_lihatPl, bt_close;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    String idLwg, jdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lowongan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle("Detail Lowongan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_judul = (TextView) findViewById(R.id.tv_jobTtl);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_kota = (TextView) findViewById(R.id.tv_city);
        tv_desc = (TextView) findViewById(R.id.tv_jobdesc);

        img_logo = findViewById(R.id.img_lwglogo);


        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //String  jdl = "";

        idLwg = getIntent().getStringExtra("idLwg");
        Log.d("aduh", idLwg);

        db.getReference("Lowongan").child(idLwg).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_judul.setText(dataSnapshot.child("Judul").getValue(String.class));
                jdl = tv_judul.getText().toString();
                tv_kota.setText(dataSnapshot.child("Lokasi").getValue(String.class));
                tv_desc.setText(dataSnapshot.child("Desc").getValue(String.class));
                Picasso.get().load(dataSnapshot.child("Pict").getValue(String.class)).into(img_logo);

                db.getReference("Users").child(mAuth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                     tv_company.setText(dataSnapshot.child("Nama").getValue(String.class));
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

        //bt_lihatPl = (Button) findViewById(R.id.bt_seePl);
        bt_close = (Button) findViewById(R.id.bt_closeLwg);

//        bt_lihatPl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Lowongan.this, DaftarPelamar.class);
//                intent.putExtra("idLwg", idLwg);
//                intent.putExtra("judul", jdl);
//                startActivity(intent);
//            }
//        });

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Lowongan.this);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(Lowongan.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
//                    builder = new AlertDialog.Builder(Lowongan.this);
//                }
                builder.setTitle("Tutup Lowongan")
                        .setMessage("Apakah Anda yakin akan menghapus lowongan ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                deletePost("Lowongan", idLwg);
                                Snackbar.make(view, "LOWONGAN BERHASIL DIHAPUS", Snackbar.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i=new Intent(Lowongan.this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    }, 1000);


                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }
        });
    }

    public void deletePost(String root, String child){
        db = FirebaseDatabase.getInstance();
        db.getReference(root).child(child).removeValue();
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
