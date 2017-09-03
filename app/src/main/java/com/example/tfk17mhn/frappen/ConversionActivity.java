package com.example.tfk17mhn.frappen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Marcus Hermansson, tfk17mhn
 */
public class ConversionActivity extends AppCompatActivity {

    // Deklaration
    private final String TAG = "Pastasallad";
    private EditText editTextBroms, editTextRet;
    private boolean fromBromsprocent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        setUpViews();
        setUpListeners();
    }

    /**
     * Refererar till layoutens EditText-views
     */
    private void setUpViews() {
        editTextBroms = (EditText) findViewById(R.id.editTextBromsprocent);
        editTextRet = (EditText) findViewById(R.id.editTextRet);
    }

    /**
     * Skapar lyssnare för när fokusen hamnar på och ändringar görs för respektive EditText
     */
    private void setUpListeners() {
        editTextBroms.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) // Fältet är valt, konvertera från bromsprocent
                    fromBromsprocent = true;
            }
        });

        editTextRet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) // Fältet är valt, konvertera från retardationstal
                    fromBromsprocent = false;
            }
        });
        editTextBroms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (fromBromsprocent)
                    convertFromBroms();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editTextRet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!fromBromsprocent)
                    convertFromRet();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Konverterar inmatad bromsprocent till retardationstal enligt data i klassen Retardationstal.
     */
    private void convertFromBroms() {
            // Läs in integer
            int bromsprocent = getInput(editTextBroms);

                // Kontrollera om värdet är inom tabellens värden
                if (bromsprocent >= Retardationstal.MIN_VALUE && bromsprocent <= Retardationstal.MAX_VALUE) {
                    // Skriv ut retardationstalet från tabellen
                    editTextRet.setText("" + Retardationstal.RETARDATIONSTABELL[bromsprocent]);
                } else {
                    // Felaktigt värde, rensa bort ev gammalt retardationstal
                    editTextRet.setText("");
                }
    }

    private void convertFromRet() {
            // Läs in integer
            int retardation = getInput(editTextRet);

            // Kontrollera om värdet är inom tabellens värden
            if (retardation >= Retardationstal.RETARDATIONSTABELL[Retardationstal.MIN_VALUE] &&
                    retardation <= Retardationstal.RETARDATIONSTABELL[Retardationstal.MAX_VALUE]) {
                // Loopar igenom tabellen till rätt värde. Börjar nedifrån så den lägsta möjliga
                // bromsprocenten (==index) erhålls.
                Loop:
                for (int i = Retardationstal.MIN_VALUE; i <= Retardationstal.MAX_VALUE; i++) {
                    if (retardation == Retardationstal.RETARDATIONSTABELL[i]) {
                        // Rätt värde har hittats skriv ut index (tillika bromsprocent)
                        editTextBroms.setText("" + i);
                        // Behöver ej leta längre. Avbryt loopen
                        break Loop;
                    }
                }
            } else {
                // Felaktigt värde inmatat, rensa bort ev gammal bromsprocent
                editTextBroms.setText("");
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
        } catch (NumberFormatException e) {
            Log.e(TAG, "ERROR!" + e);
            return -1;
        }
    }
}
