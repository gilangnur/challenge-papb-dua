package com.example.challengepapbdua;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardViewHolder extends RecyclerView.ViewHolder {

    private TextView tvStatus = null;
    private TextView tvYour = null;
    private TextView tvComputer = null;

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        if (itemView != null) {
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvYour = itemView.findViewById(R.id.tv_yourResult);
            tvComputer = itemView.findViewById(R.id.tv_computerResult);
        }

    }

    public TextView getTvStatus() {
        return tvStatus;
    }
    public TextView getTvYour() {
        return tvYour;
    }
    public TextView getTvComputer() { return tvComputer;}
}
