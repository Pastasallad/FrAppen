package com.example.tfk17mhn.frappen;

/**
 * Klass som håller reda på timmar och minuter
 * @author Marcus Hermansson, tfk17mhn
 */
public class Time {

    private int hour;
    private int min;

    public int getHour() {return hour;}
    public void setHour(int hour) {this.hour = validateHour(hour);}
    public int getMin() {return min;}
    public void setMin(int min) {this.min = validateMinute(min);}

    public Time(int hour, int min) {
        this.hour = validateHour(hour);
        this.min = validateMinute(min);
    }

    /**
     * Adderar ett annat time-objekt med det egna.
     * @param time - Annat time-objekt
     * @return Time
     */
    public Time add(Time time) {
        int newMin = min + time.getMin();
        int newHour = hour + time.getHour();
        if (newMin > 59) // Lägg till 1 h
            newHour++;
        return new Time(validateHour(newHour), validateMinute(newMin));
    }

    /**
     * Subtraherar denna tiden med ett annat time-objekt
     * @param time - annat Time-objekt
     * @return Time
     */
    public Time remove(Time time) {
        int newMin = min - time.getMin();
        int newHour = hour - time.getHour();
        if (newMin < 0) // Ta bort 1 h
            newHour--;
        return new Time(validateHour(newHour), validateMinute(newMin));
    }

    public String toString() {
        return String.format("%02d:%02d", hour, min);
    }

    /**
     * Ser till att timmar utanför 0-23 rättas till.
     * @param hour - Timmar
     * @return Korrekt timangivelse
     */
    private int validateHour(int hour) {
        if (hour > 23)
            return hour -= 24;
        else if (hour < 0)
            return hour += 24;
        else
            return hour;
    }

    /**
     * Minuter utanför 0-59 rättas till.
     * @param min - Minuter
     * @return Korrekt minutangivelse
     */
    private int validateMinute(int min) {
        if (min > 59)
            return min -= 60;
        else if (min < 0)
            return min += 60;
        else
            return min;
    }
}
