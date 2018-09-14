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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    EditText et_nama, et_email, et_alamat, et_web;
    Button bt_save;
    private CircleImageView pict;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    //private ProfileFragment pf;
    private ProgressBar progressBar;
    private TextView judul_edit;
    private ImageButton btn_pict;

    //firebase store
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase db;

    //uri

    Uri filepath, cameraUri;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1 ;
    private static int REQUEST_CAMERA = 1;
    private static int SELECT_FILE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

      getSupportActionBar().setTitle("Edit Profil");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //progressbar
        progressBar = (ProgressBar) findViewById(R.id.pb_edit);

        //edit
        et_nama = (EditText) findViewById(R.id.edit_profile_nama);
        et_email = (EditText) findViewById(R.id.edit_profile_email);
        et_alamat = (EditText) findViewById(R.id.edit_profile_alamat);
        et_web = (EditText) findViewById(R.id.edit_profile_web);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();

        mRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());
        //mRef2 = db.getReference("Lowongan")

        //btn
        bt_save = (Button) findViewById(R.id.btn_edit_profile);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dataNama, dataEmail, dataAlamat, dataWeb;

                dataNama = dataSnapshot.child("Nama").getValue().toString();
                dataEmail = dataSnapshot.child("Email").getValue().toString();
                dataAlamat = dataSnapshot.child("Alamat").getValue().toString();
                dataWeb = dataSnapshot.child("Website").getValue().toString();

                //image
                String url;
                url = dataSnapshot.child("Pict").getValue().toString();
                Glide.with(getApplicationContext())
                        .load(url)
                        .into(pict);

                et_nama.setText(dataNama);
                et_email.setText(dataEmail);
                et_alamat.setText(dataAlamat);
                et_web.setText(dataWeb);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e("error : ", databaseError.getMessage());

            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String Snama, Semail, Salamat, Sweb;
                uploadImg();

                //edit value
                Snama = et_nama.getText().toString().trim();
                Semail = et_email.getText().toString().trim();
                Salamat = et_alamat.getText().toString().trim();
                Sweb = et_web.getText().toString().trim();


                //image
                //nama
                if (Snama.isEmpty()) {
                    et_nama.setError("Nama Tidak Boleh Kosong");
                    et_nama.requestFocus();
                    return;
                }

                //email
                if (Semail.isEmpty()) {
                    et_email.setError("Email Tidak Boleh Kosong");
                    et_email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(Semail).matches()) {
                    et_email.setError("Email Tidak Benar");
                    et_email.requestFocus();
                    return;
                }

                //alamat
                if (Salamat.isEmpty()) {
                    et_alamat.setError("Alamat Tidak Boleh Kosong");
                    et_alamat.requestFocus();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                mRef.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        dataSnapshot.getRef().child("Nama").setValue(Snama);
                        dataSnapshot.getRef().child("Email").setValue(Semail);
                        dataSnapshot.getRef().child("Alamat").setValue(Salamat);
                        dataSnapshot.getRef().child("Website").setValue(Sweb);
                        //coba
                        //dataSnapshot.getRef().child("Pict").setValue(downloadImg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Error : ", databaseError.getMessage());
                    }
                });
               // mUser.updateEmail(Semail);

                mUser.updateEmail(Semail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {


                            /*FragmentManager fm = getFragmentManager();
                            fm.beginTransaction().replace(R.id.main_frame, pf).commit();*/
                            Snackbar.make(v, "Edit Profil Berhasil", Snackbar.LENGTH_LONG).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent i=new Intent(EditProfile.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, 1000);
                            //Toast.makeText(EditProfile.this, "Berhasil", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //bottom sheet dialog
        judul_edit = (TextView) findViewById(R.id.judul_edit);

        //image
        pict = (CircleImageView) findViewById(R.id.edit_profile_pict);


        btn_pict = (ImageButton) findViewById(R.id.btn_edit_pict);
        btn_pict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        //getSupportActionBar().setTitle("Profil");


    }

    public void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);

        builder.setTitle("Profile Pict");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {

                    if(checkAndRequestPermissions()) {
                        // carry on the normal flow, as the case of  permissions  granted.
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }




                } else if (items[i].equals("Gallery")) {
                    if (ContextCompat.checkSelfPermission(EditProfile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
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

    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == REQUEST_CAMERA) {
                Uri cameraUri;
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                cameraUri = getImageUri(EditProfile.this, bmp);
                filepath = cameraUri;
                //uploadImg();
                //pict.setImageURI(filepath);


            } else if (requestCode == SELECT_FILE) {
                filepath = data.getData();
                //uploadImg();
                //pict.setImageURI(filepath);

            }
            pict.setImageURI(filepath);

        }

    }

    public void uploadImg() {

        //pict.setImageURI(filepath);


        if (filepath != null) {
            Log.d("path", filepath.toString());
            String uid = mUser.getUid();
            final StorageReference ref = storageReference.child("images/" + uid);
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
                                        mRef.child("Pict").setValue(Suri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Berhasil upload image", Toast.LENGTH_SHORT).show();
                                                }
//                                                else {
//                                                    Log.d("WOY", Su);
//                                                }
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


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    }
