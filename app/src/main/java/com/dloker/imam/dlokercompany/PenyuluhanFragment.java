package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PenyuluhanFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private View v;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public HashMap<String, String>  acr;
    public List<List_Item> listItems;
    static String imgSrc;
    Intent intent;

    public PenyuluhanFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PenyuluhanFragment newInstance(String param1, String param2) {
        PenyuluhanFragment fragment = new PenyuluhanFragment();
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
        v = inflater.inflate(R.layout.fragment_penyuluhan, container, false);

        FloatingActionButton fab1 = (FloatingActionButton)v.findViewById(R.id.fabAcr);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddAcara.class);
                startActivity(i);
            }
        });

        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recV_acara);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        db.getReference("Acara").child(mAuth.getCurrentUser().getUid()).child("Pict")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        imgSrc = dataSnapshot.getValue(String.class);
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

        listItems = new ArrayList<List_Item>();
        db.getReference("Acara").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("idCompany").exists()) {
                        if (snapshot.child("idCompany").getValue().equals(mAuth.getCurrentUser().getUid())) {
                            String title = snapshot.child("namaAcara").getValue(String.class);
                            String loc = snapshot.child("lokasi").getValue(String.class);
                            String tgl = snapshot.child("tanggal").getValue(String.class);
                            String imgSrc = snapshot.child("Pict").getValue(String.class);
                            String telp = snapshot.child("contact").getValue(String.class);
                            String desc = snapshot.child("desc").getValue(String.class);
                            String idAcr = snapshot.getKey();
                            //String imgSrc = snapshot.child("Pict").getValue(String.class);
                            //final String imgSrc;
                            intent = new Intent(getActivity(), DetailAcara.class);
                            intent.putExtra("jdlAcr", title);
                            intent.putExtra("tgl", tgl);
                            intent.putExtra("lok", loc);
                            intent.putExtra("img", imgSrc);
                            intent.putExtra("desc", desc);
                            intent.putExtra("telp", telp);
                            intent.putExtra("id", idAcr);


                            Log.d("tes123", title + " " + loc);
                            listItems.add(new List_Item(title, tgl, idAcr, imgSrc));
                        } else {
                            Log.d("tes123", "gagal");

                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Belum ada penyluhan", Toast.LENGTH_LONG).show();
                    }

                }
                adapter = new myAdapter(listItems, getActivity(), new myAdapter.OnItemClicked() {
                    @Override
                    public void onItemClick(int position) {
                        String idAcr = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                                .itemView.findViewById(R.id.tv_idLamaran)).getText().toString();
                        //Log.d("TAG", idLa);
                        //Intent intent = new Intent(getActivity(), DetailAcara.class);


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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle("Agenda Penyuluhan Saya");
        ((MainActivity)getActivity()).getTabLayout().setVisibility(View.GONE);
        ((MainActivity)getActivity()).getToolbar().setVisibility(View.GONE);
        ((MainActivity)getActivity()).showActionBar();
        ((MainActivity)getActivity()).getmViewPager().setVisibility(View.GONE);
        ((MainActivity)getActivity()).fab.setVisibility(View.GONE);


    }
}
