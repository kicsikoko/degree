package com.kicsiroot.passwordstodo;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;
    FirebaseUser currentUser;

    public PostAdapter(Context context, List<Post> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.saved_pwd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvUrl.setText(mData.get(position).getAddUrl());
        holder.tvEmail.setText(mData.get(position).getAddEmail());
        holder.tvUsername.setText(mData.get(position).getAddUsername());
        holder.tvPwd.setText(mData.get(position).getAddPassword());

        final String getUrl = mData.get(position).getAddUrl();
        final String getEmail = mData.get(position).getAddEmail();
        final String getUName = mData.get(position).getAddUsername();
        final String getPwd = mData.get(position).getAddPassword();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PasswordsDetail.class);
                mContext.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                intent.putExtra("url", getUrl);
                intent.putExtra("email", getEmail);
                intent.putExtra("username", getUName);
                intent.putExtra("pwd", getPwd);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder {

        TextView tvUrl;
        TextView tvEmail;
        TextView tvUsername;
        TextView tvPwd;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvUrl = itemView.findViewById(R.id.showUrl);
            tvEmail = itemView.findViewById(R.id.showEmail);
            tvUsername = itemView.findViewById(R.id.showUsername);
            tvPwd = itemView.findViewById(R.id.showPwd);
        }

    }
}
