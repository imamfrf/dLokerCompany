package com.dloker.imam.dlokercompany;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase db;
    private TextView tv_compName, tv_compEmail;
    private ImageView img_compLogo;
    private FirebaseAuth mAuth;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public List<List_Item> listItems;
    public String uidP, namaP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recV_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                tv_compName = headerView.findViewById(R.id.tv_namaComp);
                tv_compName.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(MainActivity.class.getSimpleName(), "Failed to read value.", error.toException());
            }
        });

        db.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                tv_compEmail = headerView.findViewById(R.id.tv_emailComp);
                tv_compEmail.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(MainActivity.class.getSimpleName(), "Failed to read value.", error.toException());
            }
        });
        listItems = new ArrayList<List_Item>();
        final String myUid = mAuth.getCurrentUser().getUid();
        db.getReference("Lamaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("idCompany").getValue().equals(myUid)){
                      //uidP = snapshot.child("idPelamar").getValue().toString();
                        db.getReference("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                listItems.clear();
                                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()){
                                    String uidP = snapshot.child("idPelamar").getValue(String.class);
                                    if (snapshot1.getKey().equals(uidP)){
                                        String namaPl = snapshot1.child("Nama").getValue(String.class);
                                        //namaP = snapshot1.child("Nama").getValue(String.class);
                                        listItems.add(new List_Item(namaPl, "tes"));
                                        //nama.remove(0);
                                    }
                                }
                                adapter = new myAdapter(listItems, MainActivity.this);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //listItems = new ArrayList<List_Item>();
        //listItems.add(new List_Item("tes", "tes"));
//            adapter = new myAdapter(listItems, MainActivity.this);
//            recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
