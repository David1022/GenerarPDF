package com.david.generarpdf;

import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VisualizarPdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pdf);
        Button button = (Button) findViewById(R.id.boton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.printShort(VisualizarPdf.this, "prueba");
                pruebas();
            }
        });

    }

    public void pruebas(){

        PdfDocument document = new PdfDocument();

        // repaint the user's text into the page
        View content = findViewById(R.id.linearB);

        // crate a page description
        int pageNumber = 1;
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(content.getWidth(),
                content.getHeight(), pageNumber).create();

        // create a new page from the PageInfo
        PdfDocument.Page page = document.startPage(pageInfo);

        content.draw(page.getCanvas());

        // do final processing of the page
        document.finishPage(page);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
        String pdfName = "pdfdemo"
                + sdf.format(Calendar.getInstance().getTime()) + ".pdf";

        File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File outputFile = new File(ruta_sd.getAbsolutePath(), pdfName);

        //File outputFile = new File(getRuta(), pdfName);

        try {
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            document.writeTo(out);
            document.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
