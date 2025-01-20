package com.example.dam2_e2_t6_mpgm;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Users;

public class IkasleListAdapter extends RecyclerView.Adapter<IkasleListAdapter.ViewHolder>{
    private ArrayList<Users> ikasleList;
    private IrakasleActivity ia;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;

        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            image = itemView.findViewById(R.id.img_ikasle);
            name = itemView.findViewById(R.id.tv_ikasle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
