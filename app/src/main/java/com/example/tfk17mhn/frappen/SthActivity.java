package com.example.tfk17mhn.frappen;

import android.icu.text.MessageFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Marcus Hermansson, tfk17mhn
 */
public class SthActivity extends AppCompatActivity {

    private final String TAG = "Pastasallad";
    private EditText editTextBroms, editTextLength;
    private TextView textViewResult;
    private CheckBox checkBoxEp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sth);
        setUpViews(); // Skapar referenser till view-objekten från xml-layouten.
    }

    private void setUpViews() {
        editTextBroms = (EditText) findViewById(R.id.editTextBroms);
        editTextLength = (EditText) findViewById(R.id.editTextLength);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        checkBoxEp = (CheckBox) findViewById(R.id.checkBoxEP);
        // För att få resultatet scrollbart vid behov
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
    }

    public void calculate(View view) {
        try {
            int length, broms;
            String output;
            if (checkBoxEp.isChecked()) {
                // Med EP-broms får man alltid räkna med tåglängd upp till 100 meter
                length = 100;
            } else { // Saknas EP-broms läser vi in inmatad tåglängd.
                length = getInput(editTextLength);
            }
            broms = getInput(editTextBroms);

            // Kontrollerar att inmatade värden är OK
            if (broms >= 0 && length >= 0 && length <= Bromsprocenttabell.MAX_TRAIN_LENGTH) {
                // Slår upp en ny Sth med aktuell bromsprocent och tåglängd.
                Sth uppslag = new Sth(broms, length);
                int[] sth = uppslag.getSth();
                // Sträng-Array för att skriva ut hastigheterna. Negativa tal är specialfall
                // som hanteras i switch-satsen.
                String[] sthText = new String[sth.length];
                for (int i = 0; i < sth.length; i++) {
                    switch (sth[i]) {
                        case -1:
                            sthText[i] = "Ej tillåtet";
                            break;
                        case -2:
                            sthText[i] = "Fastställs av operativ arbetsledning";
                            break;
                        case -3:
                            sthText[i] = "Förekommer ej";
                            break;
                        default: // Inget specialfall
                            sthText[i] = sth[i] + " km/h";
                            break;
                    }
                }
                // Fyller på strängen som ska presenteras med alla Sth:er
                output = String.format("Tabell     STH" +
                                "\nA17-bana:  %s" +
                                "\nA10-bana:  %s" +
                                "\nB-bana:    %s" +
                                "\nC-bana:    %s" +
                                "\nCitybanan: %s",
                        sthText[0], sthText[1], sthText[2], sthText[3], sthText[4]);
            } else {
                output = "FELAKTIGT VÄRDE ANGETT";
            }

            // Presentera resultatet
            textViewResult.setText(output);
        } catch (Exception e) {
            Log.e(TAG, "ERROR2!" + e);
        }
    }

    /**
     * Läser in texten från en EditText och returnerar en integer.
     * @param editText Den EditText som skall läsas in
     * @return Ett heltal, -1 om konverteringen misslyckades.
     */
    private int getInput(EditText editText) {
        try {
            String input = editText.getText().toString();
            if (!input.isEmpty())
                return Integer.parseInt(input);
            else
                return -1;
        } catch (Exception e) {
            Log.e(TAG, "ERROR!" + e);
            return -1;
        }
    }
}
