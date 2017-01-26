package com.example.tom.gruppe5;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Tom on 06.01.2017.
 */

public class TextEditor extends AppCompatActivity {

    private Button  btnTextCreate,
                    btnZurück,
                    btnEdit;

    private ListView listView;

    private

    ArrayList<File> fileList;                     //Liste, welche die Dateipfade der Dateien im Notizordner beinhaltet
    ArrayList<String> textList;                     //Liste, die die Textdateien beinhaltet

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_editor);
        InitializeActivity();
    }

    private void InitializeActivity() {

        /*Ansprache der Layout-Elemente über den Quellcode möglich*/

        btnTextCreate = (Button)findViewById(R.id.btnTextCreate);
        btnZurück = (Button)findViewById(R.id.btnZurück);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        listView = (ListView)findViewById(R.id.listView);

        ArrayListSetUp(); /*Eigene Methode in der das Auslesen der Textdateien aus dem Ordner beschrieben wird*/

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TextEditor.this, android.R.layout.simple_list_item_1, textList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        /*Wechsel von Texteditor zu CreateTextFile*/

        btnTextCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A = new Intent(TextEditor.this, CreateTextFile.class);
                startActivity(A);
            }
        });


        btnZurück.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TextEditor.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /*Edit-Funktion

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A = new Intent(TextEditor.this, CreateTextFile.class);
                A.putExtra();
                startActivity(A);

            }
        });
        */
    }

    /*Anzeige in Liste*/

    private void ArrayListSetUp() {

        fileList = new ArrayList<>();
        textList = new ArrayList<>();

        // TODO: Ordner musste erst erstellt werden, das gleiche musst du auch machen wenn du etwas abspeichern willst
        /*Anlegen einer File(die später als Ordner benannt wird), auf die alle Files gespeichert werden*/
        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Notizdateien").mkdirs();

        /*Befüllen der Dateienliste, die alle Dateien des Ordners beinhalten soll*/
        /*File wird benannt und "greifbar" gemacht, um diese leichter mit anderen Files zu befüllen*/
        File ordner = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Notizdateien");
        fileList.addAll(Arrays.asList(ordner.listFiles()));             //Listet alle Files in der Liste


        Collections.sort(fileList);         //Sortieren nach alphabetischer Reihenfolge und Zahlen aufsteigend

        Collections.reverse(fileList);      //Umkehrung der Sortierung


        /*Dateien der Position nach in die Liste einfügen*/

        int fileCounter = 0;

                while(fileCounter < fileList.size()){
                    textList.add(getTextFromFile(fileList.get(fileCounter)));

                    fileCounter++;
                }
    }

    /*Funktion mit der, der zuvor in der File (im Ordner liegend) gespeicherte Text "ausgelesen" wird*/

    private String getTextFromFile(File file) {

        StringBuilder stringBuilder = new StringBuilder(); //Setzt die Strings der einzeln ausgelesenen Zeilen zusammen

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            /*Mit BufferedReader kann lediglich eine Zeile im Text ausgelesen werden*/
                /*Also: String wird erstellt, auf den die aktuelle Zeile gespeichert wird, welcher vom BufferedReader zuvor ausgelesen wurde*/
                /*While.Loop wird je nach Länge der Textzeilen einer Textnotiz ausgeführt*/
            String actualLine;

            while((actualLine = bufferedReader.readLine()) != null){        //Zeile wird ausgelesen und actualLine übergeben
                stringBuilder.append(actualLine);                           //Inhalt der actualLine wird dem StringBuilder hinzugefügt
                stringBuilder.append("\n");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString().trim();                         //trim dient dazu alle überflüssigen zeilen zu entfernen
    }



}
