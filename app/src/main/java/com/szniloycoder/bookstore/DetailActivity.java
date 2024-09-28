package com.szniloycoder.bookstore;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.szniloycoder.bookstore.Models.Books;
import com.szniloycoder.bookstore.databinding.ActivityDetailBinding;
import com.szniloycoder.bookstore.databinding.ActivityMainBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    // Firebase and Database
    FirebaseDatabase database;

    // Data Variables
    String Description;
    String Name;
    ArrayList<String> Genre;
    String Language;
    ArrayList<Books> list;
    String Poster;
    String Pdf ;
    float Rating ;
    Long Pages ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Initialize Firebase and UI components
        database = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        Genre = new ArrayList<>();

        // Get data from Intent
        retrieveIntentData();

        // Set UI Components
        setupUI();

        // Setup Event Listeners
        setupEventListeners();


    }

    private void setupEventListeners() {

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        binding.btnFav.setOnClickListener(view -> {

            binding.btnFav.setImageResource(R.drawable.favorite_full);
        });

        binding.btnReading.setOnClickListener(view -> {

            // external PDF viewer:
            if (Pdf != null && !Pdf.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(Pdf), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent chooser = Intent.createChooser(intent, "Open PDF");
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    // Handle if no PDF viewer app is installed
                    Toast.makeText(DetailActivity.this, "No application to view PDF", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DetailActivity.this, "PDF not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveIntentData() {

        Intent intent = getIntent();
        Name = intent.getStringExtra("name");
        Rating = intent.getFloatExtra("ratings",0);
        Description = intent.getStringExtra("description");
        Pages = intent.getLongExtra("pages",0);
        Genre = intent.getStringArrayListExtra("genre");
        Language = intent.getStringExtra("language");
        Poster = intent.getStringExtra("poster");
        Pdf = intent.getStringExtra("pdf");
    }

    @SuppressLint("SetTextI18n")
    private void setupUI() {

      //  binding.ratingTxt.setText((int) Rating);
        binding.descriptionTxt.setText(Description);
        binding.pageTxt.setText(Pages+ "\nPages");
        binding.languageTxt.setText(Language);
        Glide.with(this)
                .load(Poster)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(binding.bgPoster);

        Glide.with(this)
                .load(Poster)
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(binding.poster);
    }

}