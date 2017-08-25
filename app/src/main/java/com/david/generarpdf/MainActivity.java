package com.david.generarpdf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();
    private final static String NOMBRE_FICHERO = "prueba.pdf";
    private final static String NOMBRE_DIRECTORIO = "MiPDF";
    private final static String TEXTO = "Estoy probando como se hace un txt y parece que funciona. " +
            "Con los PDF no funciona y no se si es por el lugar de almacenamiento del fichero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button boton = (Button) findViewById(R.id.boton);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarPDF();
                //generarTxtExt();
                //generarTxtInt();
                pruebas();
            }
        });
    }

    public void pruebas(){

        Intent i = new Intent(this, VisualizarPdf.class);
        startActivity(i);

    }

    public void generarPDF() {

        PdfDocument document = new PdfDocument();

        // repaint the user's text into the page
        View content = findViewById(R.id.activity_main);

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

    public void generarTxtInt(){

        try
        {
            OutputStreamWriter fout=
                    new OutputStreamWriter(
                            openFileOutput("prueba_int.txt", Context.MODE_PRIVATE));

            fout.write(TEXTO);
            fout.close();
            leerTxtInt();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a memoria interna");
        }

    }

    public void leerTxtInt(){

        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("prueba_int.txt")));

            String texto = fin.readLine();
            fin.close();
            printLong(this, texto);
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }

    }

    public void generarTxtExt(){

        boolean sdDisponible = false;
        boolean sdAccesoEscritura = false;

        //Comprobamos el estado de la memoria externa (tarjeta SD)
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            sdDisponible = true;
            sdAccesoEscritura = true;
            printShort(this, "SD montada");
            Log.d(TAG, "SD montada");
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            sdDisponible = true;
            sdAccesoEscritura = false;
            printShort(this, "SD solo lectura");
            Log.d(TAG, "SD solo lectura");
        }
        else
        {
            sdDisponible = false;
            sdAccesoEscritura = false;
            printShort(this, "No se encuentra SD");
            Log.d(TAG, "No se encuentra SD");
        }
        try
        {
            File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File f = new File(ruta_sd.getAbsolutePath(), "prueba_sd.txt");

            OutputStreamWriter fout =
                    new OutputStreamWriter(
                            new FileOutputStream(f));

            fout.write(TEXTO);
            fout.close();
            printShort(this, "TXT generado correctamente");
            leerTxtExt();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al escribir fichero a tarjeta SD");
        }

    }

    public void leerTxtExt(){

        try
        {
            File ruta_sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File f = new File(ruta_sd.getAbsolutePath(), "prueba_sd.txt");

            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            String texto = fin.readLine();
            printLong(this, texto);
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }

    }

    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio "Download"
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            //ruta = new File(Environment.DIRECTORY_DOWNLOADS);
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta == null) {
                if (!ruta.mkdir()) {
                    if (!ruta.exists()) {
                        return ruta;
                    }
                }
            }
        } else {
        }
        return ruta;
    }

    public static void printShort(Context context, String text){

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();

    }

    public static void printLong(Context context, String text){

        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();

    }


}