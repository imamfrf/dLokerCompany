package com.dloker.imam.dlokercompany;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiterimaFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
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


    public DiterimaFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DiterimaFragment newInstance(String param1, String param2) {
        DiterimaFragment fragment = new DiterimaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_diterima, container, false);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) v.findViewById(R.id.recV_accepted);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        retrieveUsers();
        retrieveLowongan();

        listItems = new ArrayList<List_Item>();
        final String myUid = mAuth.getCurrentUser().getUid();
        db.getReference("Lamaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                listItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("idCompany").exists()) {
                        if (snapshot.child("idCompany").getValue().equals(myUid) && snapshot.child("statusLmr").getValue().equals("accepted")) {
                            // uidP = snapshot.child("idPelamar").getValue().toString();
                            //idL = snapshot.getKey();
                            String idL = snapshot.getKey();
                            String namaP = usr.get(snapshot.child("UID").getValue());
                            String desc = lwg.get(snapshot.child("idLowongan").getValue());
                           // String imgSrc = snapshot.child("PelamarPict").getValue(String.class);
                            String imgSrc = usr.get(snapshot.child("UID").getValue()+"pict");



                            listItems.add(new List_Item(namaP, desc, idL, imgSrc));
                        } else {
                            //Log.d("hadeh", snapshot.child("status").getValue(String.class));
                            //Toast.makeText(snapshot.child("status").getValue(String.class), Toast.LENGTH_LONG).show();
                        }
                    }


                }
                adapter = new myAdapter(listItems, getActivity(), new myAdapter.OnItemClicked() {
                    @Override
                    public void onItemClick(int position) {
                        String idLa = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_idLamaran)).getText().toString();
                        Log.d("TAG", idLa);
                        Intent intent = new Intent(getActivity(), DetailDiterima.class);
                        intent.putExtra("idLamaran", idLa);
                        intent.putExtra("status", "Telah Diterima");
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

      //  ((MainActivity)getActivity()).setActionBarTitle("Lamaran Masuk");
    }
}
