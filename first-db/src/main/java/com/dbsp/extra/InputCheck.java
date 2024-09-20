package com.dbsp.extra;

import static com.dbsp.extra.Colors.COLOR_RED_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_RESET;

public class InputCheck {
    public static boolean asinCheck(String asin) {
        if (asin == null){
            return false;
        }
        // Prüfen, ob die Länge genau 10 Zeichen beträgt
        if (asin.length() != 10) {
            System.out.println(COLOR_RED_BACKGROUND + " The asin has a length of 10 characters! " + COLOR_RESET);
            return false;
        }
        // Prüfen, ob der String nur Buchstaben und Zahlen enthält
        //return asin.matches("[A-Za-z0-9]+");
        if (asin.matches("[A-Za-z0-9]+")) {
            return true;
        } else {
            System.out.println(COLOR_RED_BACKGROUND + " The asin does not have special characters! " + COLOR_RESET);
            return false;
        }
    }
}
