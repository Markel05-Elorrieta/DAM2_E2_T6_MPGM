package com.example.dam2_e2_t6_mpgm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Users;

public class ProfileInfoAdapter extends RecyclerView.Adapter<ProfileInfoAdapter.ViewHolder> {
    private ArrayList<String> infoList;
    private ProfileActivity pa;

    public ProfileInfoAdapter(ArrayList<String> infoList, ProfileActivity pa) {
        this.infoList = infoList;
        this.pa = pa;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.tv_infoUser);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_profileinfo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String sInfo = infoList.get(position);
        holder.info.setText(sInfo);
    }

    public void setInfoList(ArrayList<String> setInfoList) {
        this.infoList = infoList;
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }
}