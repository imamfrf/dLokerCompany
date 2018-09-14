package com.dloker.imam.dlokercompany;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class AddAcara extends AppCompatActivity {

    FirebaseDatabase db;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText et_acaraTtl, et_acaraLok, et_acaraTgl, et_acaraDesc, et_contactP;
    Button bt_upAcara;
    ImageButton bt_upPict;
    ImageView img;
    private ProgressBar progressBar;
    HashMap post = new HashMap();

    Uri filepath, cameraUri;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acara);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setActionBarTitle("Tambah Lowongan");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        progressBar = (ProgressBar)findViewById(R.id.pb_acara);


        et_acaraTtl = (EditText) findViewById(R.id.et_namaAcara);
        et_acaraLok = (EditText) findViewById(R.id.et_lokasiAcr);
        et_acaraTgl = (EditText) findViewById(R.id.et_waktuAcr);
        et_acaraDesc = (EditText) findViewById(R.id.et_detailAcr);
        et_contactP = (EditText) findViewById(R.id.et_contactP);
        bt_upAcara = (Button) findViewById(R.id.bt_upAcr);
        bt_upPict = (ImageButton) findViewById(R.id.bt_pictAcr);
        img = (ImageView) findViewById(R.id.img_pictAcr);
        //final String jobTtl, jobDesc, jobCate, jobLoc;

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        bt_upPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        bt_upAcara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String nama, lokasi, tgl, desc, telp, id;
                nama = et_acaraTtl.getText().toString();
                lokasi = et_acaraLok.getText().toString();
                tgl = et_acaraTgl.getText().toString();
                desc = et_acaraDesc.getText().toString();
                telp = et_contactP.getText().toString();
                id = db.getReference("Acara").push().getKey();

//                db.getReference("Acara").child(id).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        //image
//                        String url;
//                        url = dataSnapshot.child("Pict").getValue().toString();
//                        Glide.with(AddAcara.this)
//                                .load(url)
//                                .into(img);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

                uploadImg(id);


                post.put("namaAcara", nama);
                post.put("lokasi", lokasi);
                post.put("tanggal", tgl);
                post.put("desc", desc);
                post.put("contact", telp);
                post.put("idCompany", mAuth.getCurrentUser().getUid());

                if(et_acaraTtl.getText().toString().isEmpty()){
                    et_acaraTtl.setError("Nama acara tidak boleh kosong");
                    et_acaraTtl.requestFocus();
                    return;
                }

                if (et_acaraDesc.getText().toString().length() < 10){
                    et_acaraDesc.setError("Minimal 10 karakter");
                    return;
                }

                if(et_acaraLok.getText().toString().isEmpty()){
                    et_acaraLok.setError("Lokasi tidak boleh kosong");
                    et_acaraLok.requestFocus();
                    return;
                }

                if (et_acaraTgl.getText().toString().isEmpty()){
                    et_acaraTgl.setError("Waktu acara tidak boleh kosong");
                    return;
                }

                db.getReference("Acara").child(id).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Snackbar.make(view, "ACARA BERHASIL DISIMPAN", Snackbar.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent i=new Intent(AddAcara.this, MainActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Uri cameraUri;
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                cameraUri = getImageUri(AddAcara.this, bmp);
                filepath = cameraUri;
                //uploadImg();
                //pict.setImageURI(filepath);


            } else if (requestCode == SELECT_FILE) {
                filepath = data.getData();
                //uploadImg();
                //pict.setImageURI(filepath);

            }
            img.setImageURI(filepath);

        }

    }

    public void selectImage() {


        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddAcara.this);

        builder.setTitle("Profile Pict");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    if (ContextCompat.checkSelfPermission(AddAcara.this, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {android.Manifest.permission.CAMERA},
                                REQUEST_CODE_ASK_PERMISSIONS);

                        return;
                    }
                    if (ContextCompat.checkSelfPermission(AddAcara.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {
                    if (ContextCompat.checkSelfPermission(AddAcara.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void uploadImg(final String idAcr) {

        //pict.setImageURI(filepath);
        String imgLink;
        final StorageReference ref;


        if (filepath != null) {
            Log.d("path", filepath.toString());
            //String uid = mAuth.getCurrentUser().getUid();
            String Suri1;
            ref = storageReference.child("poster/" + idAcr);
            ref.putFile(filepath)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadImg = uri;
                                        String Suri = downloadImg.toString();
                                        Log.d("gbr", Suri);
//


                                        //post.put("Pict", Suri);
                                        db.getReference("Acara").child(idAcr).child("Pict").setValue(Suri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                                                }
//
                                            }
                                        });
                                    }
                                });
                            }

                        }
                    });

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
