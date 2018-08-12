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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LowonganFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private View v;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public HashMap<String, String>  lwg;
    public List<List_Item> listItems;
    static String imgSrc;


    public LowonganFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LowonganFragment newInstance(String param1, String param2) {
        LowonganFragment fragment = new LowonganFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_lowongan, container, false);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recV_lowongan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<List_Item>();
        db.getReference("Lowongan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems.clear();
                db.getReference("Users").child(mAuth.getCurrentUser().getUid()).child("Pict")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                imgSrc = dataSnapshot.getValue(String.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("idCompany").exists()) {
                        if (snapshot.child("idCompany").getValue().equals(mAuth.getCurrentUser().getUid())) {
                            String title = snapshot.child("Judul").getValue(String.class);
                            String loc = snapshot.child("Lokasi").getValue(String.class);
                            String idLwg = snapshot.getKey();
                            //String imgSrc = snapshot.child("Pict").getValue(String.class);
                            //final String imgSrc;

                            Log.d("tes123", title + " " + loc);
                            listItems.add(new List_Item(title, loc, idLwg, imgSrc));
                        } else {
                            Log.d("tes123", "gagal");

                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Belum ada lowongan", Toast.LENGTH_LONG).show();
                    }

                }
                adapter = new myAdapter(listItems, getActivity(), new myAdapter.OnItemClicked() {
                    @Override
                    public void onItemClick(int position) {
                        String idLwg = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_idLamaran)).getText().toString();
                        //Log.d("TAG", idLa);
                        Intent intent = new Intent(getActivity(), Lowongan.class);
                        intent.putExtra("idLwg", idLwg);
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle("Lowongan Saya");
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getToolbar().setVisibility(View.GONE);
        ((MainActivity)getActivity()).showActionBar();
        ((MainActivity)getActivity()).getmViewPager().setVisibility(View.GONE);


    }


}
