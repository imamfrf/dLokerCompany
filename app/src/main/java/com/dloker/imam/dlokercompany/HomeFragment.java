package com.dloker.imam.dlokercompany;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseDatabase db;

    private FirebaseAuth mAuth;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    public List<List_Item> listItems;
    public HashMap<String, String> usr, lwg;
    View v;
    String CHANNEL_ID = "com.dLokerPartner.IncomingLamaran";
    public MainActivity activity;
    public static String imgSrc;

    public static String getImgSrc() {
        return imgSrc;
    }

    public static void setImgSrc(String imgSrc) {
        HomeFragment.imgSrc = imgSrc;
    }

    public HomeFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) v.findViewById(R.id.recV_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        retrieveUsers();
        retrieveLowongan();



        //String a;
        listItems = new ArrayList<List_Item>();
        final String myUid = mAuth.getCurrentUser().getUid();
        int initsize;
        db.getReference("Lamaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listItems.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.exists()){
                        if (snapshot.child("idCompany").exists()) {
                            if (snapshot.child("idCompany").getValue().equals(myUid) && snapshot.child("statusLmr").getValue().equals("wait")) {
                                Intent intent = new Intent(activity, MainActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("ntf", "1");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true)
                                        .setSmallIcon(R.drawable.dlogo)
                                        .setContentTitle("Lamaran Masuk")
                                        .setContentText("Lamaran masuk baru diterima")
                                        .setLights(Color.RED, 1000, 300)
                                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                        .setVibrate(new long[]{100, 200, 300, 400, 500})
                                        .setDefaults(Notification.DEFAULT_VIBRATE)
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    NotificationChannel channel = new NotificationChannel(
                                            CHANNEL_ID, "Lamaran Masuk", NotificationManager.IMPORTANCE_DEFAULT
                                    );
                                    channel.setDescription("Lamaran masuk baru diterima");
                                    channel.setShowBadge(true);
                                    channel.canShowBadge();
                                    channel.enableLights(true);
                                    channel.setLightColor(Color.RED);
                                    channel.enableVibration(true);
                                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
                                    notificationManager.createNotificationChannel(channel);
                                }

                                if(snapshot.child("ntfPart").getValue(String.class).equalsIgnoreCase("false")){
                                    notificationManager.notify(1, mBuilder.build());
                                    db.getReference("Lamaran").child(snapshot.child("idLamaran").getValue(String.class)).child("ntfPart")
                                            .setValue("true");
                                }




                                // uidP = snapshot.child("idPelamar").getValue().toString();
                                //idL = snapshot.getKey();
                                String idL = snapshot.getKey();
                                String namaP = usr.get(snapshot.child("UID").getValue());
                                String desc = lwg.get(snapshot.child("idLowongan").getValue());
                                String imgSrc = usr.get(snapshot.child("UID").getValue()+"pict");

//                                final HomeFragment h = new HomeFragment();
//
//                                db.getReference("Users").child(snapshot.child("UID").getValue().toString()).child("Pict").addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        h.setImgSrc(dataSnapshot.getValue(String.class));
//                                        //HomeFragment.imgSrc = dataSnapshot.getValue(String.class);
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                                Log.d("TAG", h.getImgSrc());

                                listItems.add(new List_Item(namaP, desc, idL, imgSrc));




                            }
                    }
                    }
                    else {
                        Toast.makeText(getActivity(), "Belum Ada Lamaran Masuk", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                adapter = new myAdapter(listItems, getActivity(), new myAdapter.OnItemClicked() {
                    @Override
                    public void onItemClick(int position) {
                        String idLa = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_idLamaran)).getText().toString();
                        Log.d("TAG", idLa);
                        Intent intent = new Intent(getActivity(), DetailPelamar.class);
                        intent.putExtra("idLamaran", idLa);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        final int initSize = listItems.size();
        String sz = Integer.toString(initSize);
        Log.d("haha", sz);
        Log.d("hehe", Integer.toString(listItems.size()));


        // Inflate the layout for this fragment
        return v;
    }

    public void retrieveUsers(){
        usr = new HashMap<String, String>();
        db = FirebaseDatabase.getInstance();
        db.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    usr.put(sn.getKey(), sn.child("Nama").getValue(String.class));
                    usr.put(sn.getKey()+"pict", sn.child("Pict").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void retrieveLowongan(){
        lwg = new HashMap<String, String>();
        db = FirebaseDatabase.getInstance();
        db.getReference("Lowongan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sn : dataSnapshot.getChildren()){
                    lwg.put(sn.getKey(), sn.child("Judul").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //((MainActivity)getActivity()).setActionBarTitle("Beranda");
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(getActivity());
        this.activity = (MainActivity) activity;
    }
}
