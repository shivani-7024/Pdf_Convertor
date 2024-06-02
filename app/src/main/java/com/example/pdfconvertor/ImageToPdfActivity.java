package com.example.pdfconvertor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageToPdfActivity extends AppCompatActivity {
    Button back_btn_image_to_pdf, select_image_btn_image_to_pdf, save_btn_image_to_pdf;
    ImageView image_view_image_to_pdf;
    private static final int REQUEST_IMAGE_PICK = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_pdf);

        back_btn_image_to_pdf = findViewById(R.id.back_btn_image_to_pdf);
        select_image_btn_image_to_pdf = findViewById(R.id.select_btn_image_to_pdf);
        save_btn_image_to_pdf = findViewById(R.id.save_btn_image_to_pdf);
        image_view_image_to_pdf = findViewById(R.id.imageview_image_to_pdf);

        select_image_btn_image_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        back_btn_image_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_view_image_to_pdf.setImageResource(R.drawable.baseline_image_search_24);
                select_image_btn_image_to_pdf.setVisibility(View.VISIBLE);
                save_btn_image_to_pdf.setVisibility(View.GONE);
                back_btn_image_to_pdf.setVisibility(View.GONE);
            }
        });
        save_btn_image_to_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSavePdf();
            }
        });
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            image_view_image_to_pdf.setImageURI(selectedImageUri);
            back_btn_image_to_pdf.setVisibility(View.VISIBLE);
            save_btn_image_to_pdf.setVisibility(View.VISIBLE);
            select_image_btn_image_to_pdf.setVisibility(View.GONE);
        }
    }

    private void generateAndSavePdf() {
        BitmapDrawable drawable = (BitmapDrawable) image_view_image_to_pdf.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            if (bitmap != null) {
                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                canvas.drawPaint(paint);
                canvas.drawBitmap(bitmap, 0, 0, null);
                pdfDocument.finishPage(page);
                File downloadsDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFs");
                String fileName = "image_to_pdf_" + System.currentTimeMillis() + ".pdf";
                File pdfFile = new File(downloadsDir, fileName);
                try {
                    pdfDocument.writeTo(new FileOutputStream(pdfFile));
                    Toast.makeText(this, "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error saving PDF", Toast.LENGTH_SHORT).show();
                }
                pdfDocument.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}