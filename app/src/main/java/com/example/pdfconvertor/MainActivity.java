package com.example.pdfconvertor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button textToPdfButton, imageToPdfButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToPdfButton = findViewById(R.id.btn_text_to_pdf);
        imageToPdfButton = findViewById(R.id.btn_image_to_pdf);

        textToPdfButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, TextToPdfActivity.class);
            startActivity(intent);
        });

        imageToPdfButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageToPdfActivity.class);
            startActivity(intent);
        });
    }
}