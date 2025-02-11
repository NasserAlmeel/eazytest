package com.nasser.eazytest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> userList;

    public UserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.userName.setText(user.getName().getFullName());

        // Load image with Glide and handle errors
        Glide.with(context)
                .load(user.getPicture().getLarge())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder_image)  // While loading
                .error(R.drawable.error_image)  // If fails
                .into(holder.profileImageView);

        // Handle click event to open details screen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra("name", user.getName().getFullName());
            intent.putExtra("image", user.getPicture().getLarge());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("location", user.getLocation().getFullAddress());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}