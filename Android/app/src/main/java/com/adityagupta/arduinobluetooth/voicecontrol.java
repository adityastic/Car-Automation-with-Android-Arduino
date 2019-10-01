package com.adityagupta.arduinobluetooth;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class voicecontrol extends Activity {

    private TextView txtSpeechInput,txtSpeechInput1,blue;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        txtSpeechInput1 = (TextView) findViewById(R.id.txtSpeechInput1);

        blue=(TextView)findViewById(R.id.blue);
        // hide the action bar
//        getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> matches = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(matches.get(0));

                    if(matches.get(0).equals ("move forward") || matches.get(0).equals( "forward") ||matches.get(0).equals("followed") || matches.get(0).equals("4") ||matches.get(0).equals("fore head") || matches.get(0).equals("fido") ||matches.get(0).equals( "follow" )|| matches.get(0).equals("find")|| matches.get(0).equals("fido")|| matches.get(0).equals("file")|| matches.get(0).equals("fire")||matches.get(0).equals("spider")||matches.get(0).equals("5")||matches.get(0).equals("front")||matches.get(0).equals("you forward")||matches.get(0).equals("my father")||matches.get(0).equals("you 50")||matches.get(0).equals("more fo")||matches.get(0).equals("more 4")||matches.get(0).equals("more for")||matches.get(0).equals("farrell")||matches.get(0).equals("from")||matches.get(0).equals("fri")||matches.get(0).equals("more 4")||matches.get(0).equals("ford"))
                    {
                       txtSpeechInput1.setText("^");
                       int i=0;
                     while(txtSpeechInput1.getText().equals("^")&&i<=150) {

                        BluetoothUtils.getB().send("a");
                        Log.e("check1","a");
                         i++;
                         Log.e("check value of i",String.valueOf(i));
                        if(i==150)
                        {
                            onCreate(Bundle.EMPTY);
                        }
                     }
                    }
                    else if(matches.get(0).equals("backward")){
                       int i=0;

                        txtSpeechInput1.setText("|");
                        while(txtSpeechInput1.getText().equals("|")&&i<=150) {
                            i++;

                            BluetoothUtils.getB().send("b");
                            Log.e("check1","b");
                            if(i==150)
                            {
                                onCreate(Bundle.EMPTY);
                            }

                        }
                    }

                    else if(matches.get(0).equals("left")||matches.get(0).equals("")||matches.get(0).equals("bear")||matches.get(0).equals("by")||matches.get(0).equals("va")||matches.get(0).equals("ma")||matches.get(0).equals("i")){
                        int i=0;
                        txtSpeechInput1.setText("<--");
                        while(txtSpeechInput1.getText().equals("<--")&&i<=150) {
                            i++;
                            BluetoothUtils.getB().send("d");
                            Log.e("check1","d");
                            if(i==75)
                            {
                                onCreate(Bundle.EMPTY);
                            }

                        }
                    }

                    else if(matches.get(0).equals("right")||matches.get(0).equals("b")||matches.get(0).equals("bear")||matches.get(0).equals("by")||matches.get(0).equals("va")||matches.get(0).equals("ma")||matches.get(0).equals("i")){

                        int i=0;
                        txtSpeechInput1.setText("-->");
                        while(txtSpeechInput1.getText().equals("-->")&&i<=150) {
                            i++;
                            BluetoothUtils.getB().send("c");
                            Log.e("check1","c");
                            if(i==75)
                            {
                                onCreate(Bundle.EMPTY);
                            }

                        }
                    }

                }
                break;

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}