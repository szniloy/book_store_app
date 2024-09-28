package com.szniloycoder.bookstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.FirebaseDatabase;
import com.szniloycoder.bookstore.DetailActivity;
import com.szniloycoder.bookstore.Models.Books;
import com.szniloycoder.bookstore.R;
import com.szniloycoder.bookstore.databinding.BooksViewBinding;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.viewHolder> {

    Context context;
    ArrayList<Books> list;
    FirebaseDatabase database;

    // Constructor
    public BooksAdapter(Context context, ArrayList<Books> list) {
        this.context = context;
        this.list = list;
        this.database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.books_view, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Books model = list.get(position);


        // Load food image with Glide and apply transformations
        Glide.with(context)
                .load(model.getPoster())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.binding.img);

        // Set click listener to open DetailActivity with food details
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", model.getTitle());
            intent.putExtra("ratings", model.getRatings());
            intent.putExtra("description", model.getDescription());
            intent.putExtra("language", model.getLanguage());
            intent.putExtra("poster", model.getPoster());
            intent.putExtra("pages", model.getPages());
            intent.putExtra("pdf", model.getPdf());
            intent.putExtra("genre", model.getGenre());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        BooksViewBinding binding;

        public viewHolder(View itemView) {
            super(itemView);
            this.binding = BooksViewBinding.bind(itemView);
        }
    }
}
