package com.example.tom.gruppe5;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.ContactsContract;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.widget.Button;
import android.widget.Toast;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import android.support.v4.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by Tom on 25.01.2017.
 */

public class SprachMemos extends AppCompatActivity {

    Button btnStartrec, btnStoprec, btnStartPlay, btnStopPlay ;
    Spinner spmemos;
    EditText mFilename;
    Spinner spinnerVoices;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer;
    ArrayList<File> fileList;
    ArrayList<String> memoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartrec = (Button) findViewById(R.id.btnrec);
        btnStoprec = (Button) findViewById(R.id.btnrecstop);
        btnStartPlay = (Button) findViewById(R.id.btnplay);
        btnStopPlay = (Button) findViewById(R.id.btnstopplay);
        mFilename = (EditText) findViewById(R.id.mFilename);
        spmemos = (Spinner) findViewById(R.id.spmemos);

        btnStoprec.setEnabled(false);
        btnStartPlay.setEnabled(false);
        btnStopPlay.setEnabled(false);

        ArrayListSetUp();

        InitializeActivity();


        btnStartrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    mFilename + ".3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {

                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    btnStartrec.setEnabled(false);
                    btnStoprec.setEnabled(true);

                } else {
                    requestPermission();
                }

            }
        });

        btnStoprec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStoprec.setEnabled(false);
                btnStartPlay.setEnabled(true);
                btnStartrec.setEnabled(true);
                btnStopPlay.setEnabled(false);


            }
        });

        btnStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                btnStoprec.setEnabled(false);
                btnStartrec.setEnabled(false);
                btnStopPlay.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();

            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStoprec.setEnabled(false);
                btnStartrec.setEnabled(true);
                btnStopPlay.setEnabled(false);
                btnStartPlay.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });
    }

    private void InitializeActivity() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, (List<String>) spmemos);
        spmemos.setAdapter(arrayAdapter);

        spmemos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }




    private void ArrayListSetUp() {
        fileList = new ArrayList<>();
        memoList = new ArrayList<>();

        new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Memos").mkdirs();
        File ordner = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Memos");

        fileList.addAll(Arrays.asList(ordner.listFiles()));
        Collections.sort(fileList);
        Collections.reverse(fileList);

        int fileCounter = 0;

    }


/*    private void DisplayMemoFiles(String Files){
        int a;
        Contacts contakts;
        SpinnerAdapter adapter = spinnerVoices.getAdapter();

        for (a = 0; adapter.getCount(); a++){

        }

    }*/


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(SprachMemos.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(SprachMemos.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SprachMemos.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}










