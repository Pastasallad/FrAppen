package com.example.tfk17mhn.frappen;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * @author Marcus Hermansson, tfk17mhn
 */
public class RestActivity extends AppCompatActivity {

    // Deklaration av klassvariabler
    TextView endTime, earliestStartAway, earliestStartHome, actualStartDay2,
            actualEndDay2, earliestStartDay3, earliestStartFp;
    final String TAG = "Pastasallad";
    final String BLANK = "- - : - -";
    // Sträng-nycklar för att spara inmatade tider
    final String END_TIME = "endTime";
    final String ST_AWAY = "startAway";
    final String ST_HOME = "startHome";
    final String ST_DAY2 = "startDay2";
    final String EN_DAY2 = "endDay2";
    final String ST_DAY3 = "startDay3";
    final String ST_FP = "startFp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        // Referera till alla TextView:s
        setUpViews();

        // Återställ sparade värden
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        endTime.setText(prefs.getString(END_TIME, BLANK));
        earliestStartAway.setText(prefs.getString(ST_AWAY, BLANK));
        earliestStartHome.setText(prefs.getString(ST_HOME, BLANK));
        actualStartDay2.setText(prefs.getString(ST_DAY2, BLANK));
        actualEndDay2.setText(prefs.getString(EN_DAY2, BLANK));
        earliestStartDay3.setText(prefs.getString(ST_DAY3, BLANK));
        earliestStartFp.setText(prefs.getString(ST_FP, BLANK));

        // Skapar lyssnare och TimePickers för att välja tid.
        setUpListeners();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(END_TIME, endTime.getText().toString());
        editor.putString(ST_AWAY, earliestStartAway.getText().toString());
        editor.putString(ST_HOME, earliestStartHome.getText().toString());
        editor.putString(ST_DAY2, actualStartDay2.getText().toString());
        editor.putString(EN_DAY2, actualEndDay2.getText().toString());
        editor.putString(ST_DAY3, earliestStartDay3.getText().toString());
        editor.putString(ST_FP, earliestStartFp.getText().toString());
        editor.apply();
    }

    /**
     * Refererar till samtliga TextViews
     */
    private void setUpViews() {
        endTime = (TextView) findViewById(R.id.textViewEndTime);
        earliestStartAway = (TextView) findViewById(R.id.textViewAway);
        earliestStartHome = (TextView) findViewById(R.id.textViewHome);
        actualStartDay2 = (TextView) findViewById(R.id.textViewStart2);
        actualEndDay2 = (TextView) findViewById(R.id.textViewEnd2);
        earliestStartDay3 = (TextView) findViewById(R.id.textViewStart3);
        earliestStartFp = (TextView) findViewById(R.id.textViewFp);
    }

    /**
     * Sätter upp lyssnare och dess funktioner
     */
    private void setUpListeners() {
        // Sluttid
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Skapar ett Time-objekt men vald tid
                        Time selectedTime = new Time(selectedHour, selectedMinute);
                        // Skapar ett Rest-objekt som vid initieringen räknar ut tidigast
                        // möjliga starttider
                        Rest myRest = new Rest(selectedTime);
                        // Rensa TextViews
                        clearAllTextViews();
                        // Uppdaterar TextView-objekten med nya tider.
                        updateTimeView(endTime, selectedTime);
                        updateTimeView(earliestStartAway, myRest.getEarliestStartTimeAway());
                        updateTimeView(earliestStartHome, myRest.getEarliestStartTimeHome());
                        updateTimeView(earliestStartFp, myRest.getEarliestStartTimeFp());
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });

        // Starttid borta
        earliestStartAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time selectedTime = new Time(selectedHour, selectedMinute);
                        Rest myRest = new Rest(selectedTime, true); // true = borta
                        // Rensa TextViews
                        clearAllTextViews();
                        // Uppdatera TextViews
                        updateTimeView(earliestStartAway, selectedTime);
                        updateTimeView(endTime, myRest.getEndTime());
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });

        // Starttid hemma
        earliestStartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time selectedTime = new Time(selectedHour, selectedMinute);
                        Rest myRest = new Rest(selectedTime, false); // false = hemma
                        // Rensa TextViews
                        clearAllTextViews();
                        // Uppdatera TextViews
                        updateTimeView(earliestStartHome, selectedTime);
                        updateTimeView(endTime, myRest.getEndTime());
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });

        // Starttid efter Fp-dag
        earliestStartFp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Time selectedTime = new Time(selectedHour, selectedMinute);
                        // Rensa TextViews
                        clearAllTextViews();
                        // Uppdatera TextViews
                        updateTimeView(earliestStartFp, selectedTime);
                        // Minst 36 timmar ledigt (24+12) hårdkodat nedan.
                        // Saknas lämplig funktion i Rest-klassen.
                        updateTimeView(endTime, selectedTime.remove(new Time(12, 0)));
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });

        // Starttid dag 2
        actualStartDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updateTimeView(actualStartDay2, new Time(selectedHour, selectedMinute));
                        calculateDay3();
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });

        // Sluttid dag 2
        actualEndDay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog myTimePicker = new TimePickerDialog(RestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updateTimeView(actualEndDay2, new Time(selectedHour, selectedMinute));
                        calculateDay3();
                    }
                }, 0, 0, true);
                myTimePicker.show();
            }
        });
    }

    /**
     * Beräknar tidigast möjliga starttid för dag 3 OM alla parametrar som krävs finns inmatade
     * och skriver i så fall ut denna.
     */
    private void calculateDay3() {
        try {
            // Kolla att alla värden är ifyllda
            if (checkParam(endTime) && checkParam(actualStartDay2) && checkParam(actualEndDay2)) {
                Time firstDayEnd = getTimeFromTextView(endTime);
                Rest firstRest = new Rest(firstDayEnd);
                Time actualStartTimeDay2 = getTimeFromTextView(actualStartDay2);
                Time actualEndTimeDay2 = getTimeFromTextView(actualEndDay2);
                Rest secondRest = new Rest(actualEndTimeDay2);
                // Om dygnsvilan blir kortare än 11 h ska detta kompenseras med motsvarande
                // tid nästa dygnsvila
                Time compensationTime = firstRest.getEarliestStartTimeHome()
                        .remove(actualStartTimeDay2);
                Time earliestStartTimeDay3 = secondRest.getEarliestStartTimeHome()
                        .add(compensationTime);
                // Skriv ut i TextView:n
                updateTimeView(earliestStartDay3, earliestStartTimeDay3);
            }
        } catch (NumberFormatException ex) {
            Log.e(TAG, "Felaktigt format", ex);
        }
    }

    /**
     * Kontrollerar om textView-fältet är ifyllt.
     * @param textView
     * @return true om det innehåller en tid
     */
    private boolean checkParam(TextView textView) {
        if (textView.getText().equals(BLANK) || textView.getText().toString().isEmpty())
            return false;
        else
            return true;
    }

    /**
     * Hämtar inmatad tid från en TextView och konverterar till ett Time-objekt
     * @param textView
     * @return Time
     */
    private Time getTimeFromTextView(TextView textView) {
            int hour = Integer.parseInt(textView.getText().subSequence(0,2).toString());
            int min = Integer.parseInt(textView.getText().subSequence(3, 5).toString());
            return new Time(hour, min);
    }

    /**
     * Sätter samtliga TextViews till BLANK ("- - : - -")
     */
    private void clearAllTextViews() {
        endTime.setText(BLANK);
        earliestStartAway.setText(BLANK);
        earliestStartHome.setText(BLANK);
        actualStartDay2.setText(BLANK);
        actualEndDay2.setText(BLANK);
        earliestStartDay3.setText(BLANK);
        earliestStartFp.setText(BLANK);
    }

    /**
     * Anropas av rensa-knappen i appen
     * @param view
     */
    public void clearButton(View view) {
        clearAllTextViews();
    }

    /**
     * Skriver ut en instans av Time till en TextView.
     * @param textView
     * @param time
     */
    private void updateTimeView(TextView textView, Time time) {
        textView.setText(time.toString());
    }
}
