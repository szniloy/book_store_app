package com.szniloycoder.bookstore;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.szniloycoder.bookstore.Adapters.BannerAdapter;
import com.szniloycoder.bookstore.Adapters.BooksAdapter;
import com.szniloycoder.bookstore.Models.Banners;
import com.szniloycoder.bookstore.Models.Books;
import com.szniloycoder.bookstore.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<Books> popularBookList;  // Separate list for popular books
    ArrayList<Books> upcomingBookList; // Separate list for upcoming books
    BooksAdapter popularAdapter;       // Adapter for popular books
    BooksAdapter upcomingAdapter;
    Handler sliderHandle = new Handler();

    // Slider runnable to auto-scroll featured items
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.bannerView.setCurrentItem(binding.bannerView.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(getResources().getColor(R.color.activityColor));

        // Initialize Firebase and variables
        database = FirebaseDatabase.getInstance();
        popularBookList = new ArrayList<>();
        upcomingBookList = new ArrayList<>();

        // Set up user data and features
        initFeatured();
        initPopularBooks();
        initUpcomingBooks();
    }

    private void initFeatured() {

        DatabaseReference myRef = database.getReference("Banners");
        binding.progressBarFeatured.setVisibility(View.VISIBLE);

        ArrayList<Banners> items = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        items.add(snapshot1.getValue(Banners.class));
                    }
                    banners(items);
                    binding.progressBarFeatured.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void banners(ArrayList<Banners> items) {
        BannerAdapter featuredAdapter = new BannerAdapter(items, binding.bannerView);
        binding.bannerView.setAdapter(featuredAdapter);
        binding.bannerView.setClipToPadding(false);
        binding.bannerView.setClipChildren(false);
        binding.bannerView.setOffscreenPageLimit(3);
        binding.bannerView.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        binding.bannerView.setPageTransformer(transformer);

        // Auto-scroll logic
        binding.bannerView.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandle.removeCallbacks(sliderRunnable);
                sliderHandle.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private void initPopularBooks() {

        binding.progressPopularBooks.setVisibility(View.VISIBLE);

        database.getReference().child("Books").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    popularBookList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        popularBookList.add(dataSnapshot.getValue(Books.class));
                    }
                    if (!popularBookList.isEmpty()) {
                        popularAdapter = new BooksAdapter(MainActivity.this, popularBookList);
                        binding.reyPopularBooks.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.reyPopularBooks.setAdapter(popularAdapter);
                        popularAdapter.notifyDataSetChanged();
                    }
                    binding.progressPopularBooks.setVisibility(View.GONE);
                } else {
                    binding.progressPopularBooks.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "No books available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUpcomingBooks() {
        binding.progressBarUpcomingBooks.setVisibility(View.VISIBLE);

        database.getReference().child("Upcoming").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    upcomingBookList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        upcomingBookList.add(dataSnapshot.getValue(Books.class));
                    }
                    if (!upcomingBookList.isEmpty()) {
                        upcomingAdapter = new BooksAdapter(MainActivity.this, upcomingBookList);
                        binding.reyUpcomingBooks.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.reyUpcomingBooks.setAdapter(upcomingAdapter);
                        upcomingAdapter.notifyDataSetChanged();
                    }
                    binding.progressBarUpcomingBooks.setVisibility(View.GONE);
                } else {
                    binding.progressBarUpcomingBooks.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "No books available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onPause() {
        super.onPause();
        sliderHandle.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandle.postDelayed(sliderRunnable, 2000);
    }
}