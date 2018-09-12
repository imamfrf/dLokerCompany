package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase db;
    private TextView tv_compName, tv_compEmail;
    private ImageView img_compLogo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListener;
    private HomeFragment homef;
    private LowonganFragment lwgf;
    private PenyuluhanFragment pnyf;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    static int count;
    private Settings stg;
    public FloatingActionButton fab;

    MainPagerAdapter mMainPagerAdapter;
    ViewPager mViewPager;
    String CHANNEL_ID = "com.dLokerPartner.IncomingLamaran";

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void showActionBar(){
        getSupportActionBar().show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //getSupportActionBar().setTitle("Lamaran Masuk");
        //setActionBarTitle("Lamaran Masuk");


        toolbar = (Toolbar) findViewById(R.id.toolbar1);




        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Terkini"));
        tabLayout.addTab(tabLayout.newTab().setText("Diterima"));
        tabLayout.addTab(tabLayout.newTab().setText("Ditolak"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

        homef = new HomeFragment();
        lwgf = new LowonganFragment();
        stg = new Settings();
        pnyf = new PenyuluhanFragment();
        //setFragment(homef);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        //toolbar.setElevation(0);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddLowongan.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        db.getReference("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                tv_compName = headerView.findViewById(R.id.tv_namaComp);
                tv_compName.setText(dataSnapshot.child("Nama").getValue(String.class));
                tv_compEmail = headerView.findViewById(R.id.tv_emailComp);
                tv_compEmail.setText(dataSnapshot.child("Email").getValue(String.class));
                img_compLogo = headerView.findViewById(R.id.img_logoComp);
                //Picasso.get().load(dataSnapshot.child("Pict").getValue(String.class)).into(img_compLogo);
                Glide.with(getApplicationContext()).load(dataSnapshot.child("Pict").getValue(String.class))
                        .into(img_compLogo);

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

        count = 0;

       // createNotificationChannel();

//        db.getReference("Lamaran").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                //if (dataSnapshot.)
//                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.dlogo)
//                        .setContentTitle("My notification")
//                        .setContentText("Much longer text that cannot fit one line...")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("Much longer text that cannot fit one line..."))
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(1, mBuilder.build());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "Lamaran Masuk";
//            String description = "Notifikasi ketika lamaran baru diterima";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    public void setFragment(Fragment fragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.cont_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (count == 0){
            //super.onBackPressed();
            Toast.makeText(MainActivity.this, "Tekan back sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
            count++;
            //getFragmentManager().popBackStack();
        }
        else {
            finish();
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
           // setFragment(homef);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(lwgf);
            ft.remove(stg);
            ft.remove(pnyf);
            ft.commit();
            toolbar.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            setActionBarTitle("Lamaran Masuk");
        }
        else if (id == R.id.nav_myJob){
            setFragment((lwgf));
            //tabLayout.setVisibility(View.GONE);
        }
        else if (id == R.id.nav_settings){
            setFragment(stg);
        }
        else if (id == R.id.nav_keluar){
            mAuth.signOut();
        }
        else if (id == R.id.nav_myEvent){
            setFragment(pnyf);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Lamaran Masuk");
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
