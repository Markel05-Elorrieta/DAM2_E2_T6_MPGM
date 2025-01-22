package com.example.dam2_e2_t6_mpgm;

import android.view.LayoutInflater;
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

    public IkasleListAdapter(ArrayList<Users> ikasleList, IrakasleActivity ia) {
        this.ikasleList = ikasleList;
        this.ia = ia;
    }

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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_ikasle, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users ikasle = ikasleList.get(position);
        holder.image.setImageResource(R.drawable.ic_launcher_foreground);
        holder.name.setText(ikasle.getNombre() + " " + ikasle.getApellidos());
    }

    public void setIkasleList(ArrayList<Users> ikasleList) {
        this.ikasleList = ikasleList;
    }

    @Override
    public int getItemCount() {
        return ikasleList.size();
    }

}
