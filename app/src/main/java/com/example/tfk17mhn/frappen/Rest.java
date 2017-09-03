package com.example.tfk17mhn.frappen;

/**
 * @author Marcus Hermansson, tfk17mhn
 */
public class Rest {

    // Deklaration
    final Time MIN_REST_HOME = new Time(11, 0);
    final Time MIN_REST_AWAY = new Time(8, 0);
    final Time MIN_REST_FP = new Time(36, 0);
    private Time endTime, earliestStartTimeAway, earliestStartTimeHome, earliestStartTimeFp;

    public Time getEndTime() { return endTime; }
    public Time getEarliestStartTimeAway() { return earliestStartTimeAway; }
    public Time getEarliestStartTimeHome() { return earliestStartTimeHome; }
    public Time getEarliestStartTimeFp() { return earliestStartTimeFp; }

    // Konstruktor, räknar ut när man har haft sin minsta rast
    public Rest(Time endTime) {
        this.endTime = endTime;
        calculateRest();
    }

    // Om man vill gå åt andra hållet och räkna ut när man senast måste sluta för att kunna börja
    // inmatad tid.
    public Rest(Time startTime, boolean away) {
        if (away) {
            earliestStartTimeAway = startTime;
            calculateLatestEndTime(true);
        } else {
            earliestStartTimeHome = startTime;
            calculateLatestEndTime(false);
        }
    }

    private void calculateRest() {
        earliestStartTimeAway = endTime.add(MIN_REST_AWAY);
        earliestStartTimeHome = endTime.add(MIN_REST_HOME);
        earliestStartTimeFp = endTime.add(MIN_REST_FP);
    }

    private void calculateLatestEndTime(boolean away) {
        if (away) {
            endTime = earliestStartTimeAway.remove(MIN_REST_AWAY);
        } else {
            endTime = earliestStartTimeHome.remove(MIN_REST_HOME);
        }
    }
}
