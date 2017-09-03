package com.example.tfk17mhn.frappen;

import android.util.Log;

/**
 * Klass som slår upp Sth i de olika bromsprocenttabellerna i klassen Bromsprocenttabell
 * med ingångsvärdena för bromsprocent och tåglängd.
 * @author Marcus Hermansson, tfk17mhn
 */
public class Sth {

    // Deklaration
    private final String TAG = "Pastasallad";
    private int bromsprocent, lengthColumn;
    private int[] sth = new int[Bromsprocenttabell.NR_OF_TABLES];

    // Getter
    public int[] getSth() {
        return sth;
    }

    // Konstruktor
    public Sth(int bromsprocent, int length) {
        this.bromsprocent = bromsprocent;
        lengthColumn = getColumnIndex(length);

        // Slår upp samtliga tabeller och placerar resultatet i en heltalsarray.
        sth[0] = calculateSth(Bromsprocenttabell.A17_LIMITS, Bromsprocenttabell.A17);
        sth[1] = calculateSth(Bromsprocenttabell.A10_LIMITS, Bromsprocenttabell.A10);
        sth[2] = calculateSth(Bromsprocenttabell.B_LIMITS, Bromsprocenttabell.B);
        sth[3] = calculateSth(Bromsprocenttabell.C_LIMITS, Bromsprocenttabell.C);
        sth[4] = calculateSth(Bromsprocenttabell.CITYBANAN_LIMITS, Bromsprocenttabell.CITYBANAN);
    }

    /**
     * Hämtar Sth ur bromsprocenttabellerna.
     * @param limits Gränsvärden för bromsprocenttabellen
     * @param tabell Bromsprocenttabell
     * @return Värdet ur bromsprocenttabellen på angivet index.
     */
    private int calculateSth(int[] limits, int[][] tabell) {
        return tabell[getRowIndex(limits)][lengthColumn];
    }

    /**
     * Går igenom en array med gränsvärden tills den hittar rätt rad.
     * @param limits Gränsvärden från bromsprocenttabellen.
     * @return Index för den rad som skall användas i tabellen.
     */
    private int getRowIndex(int[] limits) {
        for (int i = limits.length-1 ; i >= 0 ; i--) {
            if ( bromsprocent >= limits[i]) {
                return i;
            }
        }
        // Ska inte kunna inträffa.
        return -1;
    }

    /**
     * Avgör vilken kolumn tåglängden hör till.
     * @param length Tåglängd.
     * @return Index för rätt kolumn.
     */
    private int getColumnIndex(int length) {
        for (int i = 0; i < Bromsprocenttabell.LENGTH_COLUMNS.length; i++) {
            if (length <= Bromsprocenttabell.LENGTH_COLUMNS[i])
                return i;
        }
        // Ska inte kunna inträffa
        return -1;
    }
}
