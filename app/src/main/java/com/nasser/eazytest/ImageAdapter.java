package com.nasser.eazytest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Image> imageList;

    // Constructor
    public ImageAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = (imageList != null) ? imageList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imageList.get(position);

        // Use Glide to load the image with error handling and placeholder
        Glide.with(context)
                .load(image.getUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder) // Replace with a drawable resource
                        .error(R.drawable.error_placeholder)) // Replace with an error drawable resource
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    // Update the list dynamically
    public void updateImageList(List<Image> newImages) {
        if (newImages != null) {
            imageList.clear();
            imageList.addAll(newImages);
            notifyDataSetChanged();
        }
    }

    // Append to the existing list
    public void addImages(List<Image> newImages) {
        if (newImages != null) {
            int startPosition = imageList.size();
            imageList.addAll(newImages);
            notifyItemRangeInserted(startPosition, newImages.size());
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
