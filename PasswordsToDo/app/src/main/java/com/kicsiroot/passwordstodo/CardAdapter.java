package com.kicsiroot.passwordstodo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MViewHolder> {

    Context mContext;
    List<CardPost> mData;


    public CardAdapter(Context mContext, List<CardPost> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.saved_cards, parent, false);
        return new CardAdapter.MViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.tvNumber.setText(mData.get(position).getcNumber());
        holder.tvName.setText(mData.get(position).getcName());
        holder.tvDate.setText(mData.get(position).getcDate());
        holder.tvCVC.setText(mData.get(position).getcCVC());


        final String getNum = mData.get(position).getcNumber();
        final String getName = mData.get(position).getcName();
        final String getDate = mData.get(position).getcDate();
        final String getCvc = mData.get(position).getcCVC();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CardsDetail.class);
                mContext.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                intent.putExtra("number", getNum);
                intent.putExtra("name", getName);
                intent.putExtra("date", getDate);
                intent.putExtra("cvc", getCvc);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {

        TextView tvNumber;
        TextView tvName;
        TextView tvDate;
        TextView tvCVC;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumber = itemView.findViewById(R.id.showNumber);
            tvName = itemView.findViewById(R.id.showName);
            tvDate = itemView.findViewById(R.id.showDate);
            tvCVC = itemView.findViewById(R.id.showCVC);
        }
    }
}
