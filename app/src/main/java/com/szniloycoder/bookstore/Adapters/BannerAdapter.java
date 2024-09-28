package com.szniloycoder.bookstore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.szniloycoder.bookstore.Models.Banners;
import com.szniloycoder.bookstore.R;
import com.szniloycoder.bookstore.databinding.BannerViewholderBinding;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    Context context;
    ArrayList<Banners> bannerItems;
    ViewPager2 viewPager2;


    // Constructor to initialize adapter with items and ViewPager2
    public BannerAdapter(ArrayList<Banners> featuredItems, ViewPager2 viewPager2) {
        this.bannerItems = featuredItems;
        this.viewPager2 = viewPager2;
    }

    // Runnable to load more items when nearing the end of the list
    private final Runnable loadMoreItems = new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            bannerItems.addAll(bannerItems); // Adds duplicate items (optional: logic can be improved)
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.banner_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Set image for the current position
        holder.setImage(bannerItems.get(position));

        // If near the end of the list, load more items
        if (position == bannerItems.size() - 2) {
            viewPager2.post(loadMoreItems);
        }

    }

    @Override
    public int getItemCount() {
        return bannerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BannerViewholderBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = BannerViewholderBinding.bind(itemView);
        }

        // Method to set the image with Glide and transformations
        public void setImage(Banners featuredItem) {
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new CenterCrop(), new RoundedCorners(60)); // Apply transformations

            Glide.with(context)
                    .load(featuredItem.getImage())
                    .apply(requestOptions)
                    .into(binding.imageSlide); // Load image into ImageView
        }
    }

}
