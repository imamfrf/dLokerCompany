package com.dloker.imam.dlokercompany;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private List<List_Item> listItems;
    private Context context;
    private OnItemClicked mListener;

    public myAdapter(List<List_Item> listItems, Context context, OnItemClicked listener) {
        this.listItems = listItems;
        this.context = context;
        this.mListener = listener;

    }

    @Override
    public myAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myAdapter.ViewHolder holder, final int position) {
        //binding value dari list item ke holder
        List_Item listItem = listItems.get(position);
        holder.tvNama.setText(listItem.getNamaPelamar());
        holder.tvDesc.setText(listItem.getDescPelamar());
        holder.tvidL.setText(listItem.getIdLamaran());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(position);
            }
        });
        //Picasso.get().load(listItem.getImgSrc()).into(holder.avatar);
        Glide.with(context)
                .load(listItem.getImgSrc())
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNama, tvDesc, tvidL;
        public ImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tv_namaPelamar);
            tvDesc = itemView.findViewById(R.id.tv_descPelamar);
            tvidL = itemView.findViewById(R.id.tv_idLamaran);
            avatar = itemView.findViewById(R.id.img_avatar);

        }
    }
    public interface OnItemClicked {
        void onItemClick(int position);
    }
}



