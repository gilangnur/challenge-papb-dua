package com.example.challengepapbdua;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<WinnerModel> winnerList;

    public CardViewAdapter(ArrayList<WinnerModel> winnerList) {
        this.winnerList = winnerList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        View winnerListItem = layoutInflater.inflate(R.layout.item_history, parent, false);
        CardViewHolder result = new CardViewHolder(winnerListItem);
        return result;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (winnerList != null) {
            WinnerModel winnerModel = winnerList.get(position);
            if (winnerModel != null) {
                holder.getTvStatus().setText(winnerModel.getStatus());
                holder.getTvYour().setText(winnerModel.getYourResult());
                holder.getTvComputer().setText(winnerModel.getComputerResult());
            }
        }
    }

    @Override
    public int getItemCount() {
        int result = 0;
        if (winnerList != null) {
            result = winnerList.size();
        }
        return result;
    }
}
