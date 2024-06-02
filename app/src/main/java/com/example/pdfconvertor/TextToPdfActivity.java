package com.example.pdfconvertor;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextToPdfActivity extends AppCompatActivity {

    private EditText textToPdf;
    Button save_btn_text_to_pdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_pdf);

        textToPdf = findViewById(R.id.edit_write_text_here);
        save_btn_text_to_pdf = findViewById(R.id.save_btn_text_to_pdf);

        save_btn_text_to_pdf.setOnClickListener(v -> {
            String text = textToPdf.getText().toString();
            if (!text.isEmpty()) {
                PDFUtil.createPDF(TextToPdfActivity.this, text);
            } else {
                Toast.makeText(this, "Text is empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class PDFUtil {
    public static void createPDF(Context context, String text) {
        // Get the directory for storing PDFs
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "PDFs");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }

        String fileName = "text_to_pdf_" + System.currentTimeMillis() + ".pdf";
        File pdfFile = new File(pdfFolder, fileName);

        try {
            // Create a new PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add the text content to the PDF
            document.add(new Paragraph(text));

            document.close();

            String message = "PDF saved: " + pdfFile.getAbsolutePath();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.d("PDFUtil", message);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show();
        }
    }
}