package com.dloker.imam.dlokercompany;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class DetailAcara extends AppCompatActivity {

    TextView tv_namaAcr, tv_tglAcr, tv_lokAcr, tv_telp, tv_desc;
    ImageView img_poster;
    Button bt_close;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    String idAcr, imgSrc, jdl, tgl, lok, desc, telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_acara);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setActionBarTitle("Detail Acara");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tv_namaAcr = (TextView)findViewById(R.id.tv_DAcrTtl);
        tv_tglAcr = (TextView)findViewById(R.id.tv_DAcrTgl);
        tv_lokAcr = (TextView)findViewById(R.id.tv_DAcrLok);
        tv_telp = (TextView)findViewById(R.id.tv_DAcrTlp);
        tv_desc = (TextView)findViewById(R.id.tv_Acrdesc);

        bt_close = (Button)findViewById(R.id.bt_closeAcr);

        img_poster = (ImageView)findViewById(R.id.img_dtlAcr);

        jdl = getIntent().getStringExtra("jdlAcr");
        imgSrc = getIntent().getStringExtra("img");
        idAcr = getIntent().getStringExtra("id");
        tgl = getIntent().getStringExtra("tgl");
        lok = getIntent().getStringExtra("lok");
        desc = getIntent().getStringExtra("desc");
        telp = getIntent().getStringExtra("telp");

        Glide.with(getApplicationContext())
                .load(imgSrc).into(img_poster);

        tv_namaAcr.setText(jdl);
        tv_tglAcr.setText(tgl);
        tv_lokAcr.setText(lok);
        tv_telp.setText(telp);
        tv_desc.setText(desc);

        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailAcara.this);
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
                                deletePost("Lowongan", idAcr);
                                Snackbar.make(view, "LOWONGAN BERHASIL DIHAPUS", Snackbar.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i=new Intent(DetailAcara.this, MainActivity.class);
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

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void deletePost(String root, String child){
        db = FirebaseDatabase.getInstance();
        db.getReference(root).child(child).removeValue();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
