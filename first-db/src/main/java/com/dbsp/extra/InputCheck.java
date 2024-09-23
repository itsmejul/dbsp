package com.dbsp.extra;

import static com.dbsp.extra.Colors.COLOR_RED_BACKGROUND;
import static com.dbsp.extra.Colors.COLOR_RESET;

public class InputCheck {
    public static boolean asinCheck(String asin) {
        if (asin == null) {
            return false;
        }
        // Prüfen, ob die Länge genau 10 Zeichen beträgt
        if (asin.length() != 10) {
            System.out.println(COLOR_RED_BACKGROUND + " The asin has a length of 10 characters! " + COLOR_RESET);
            return false;
        }
        // Prüfen, ob der String nur Buchstaben und Zahlen enthält
        // return asin.matches("[A-Za-z0-9]+");
        if (asin.matches("[A-Za-z0-9]+")) {
            return true;
        } else {
            System.out.println(COLOR_RED_BACKGROUND + " The asin does not have special characters! " + COLOR_RESET);
            return false;
        }
    }

    public static boolean patternCheck(String pattern) {
        if (pattern == null) {
            return true;
        }
        if (pattern == "") {
            return false;
        }
        // Regex angepasst, um auch '_', '-', '[', ']', '(', ')' zu erlauben
        // \\s für whitespace
        if (pattern.matches("[A-Za-z0-9_\\%\\-\\[\\]\\(\\)\\s]+")) {
            return true;
        } else {
            System.out.println(COLOR_RED_BACKGROUND + " The pattern does not have special characters! " + COLOR_RESET);
            return false;
        }
    }

    // geile Review Methoden!
    public static boolean ratingCheck(String readRating) {
        if (readRating == null) {
            System.out
                    .println(COLOR_RED_BACKGROUND + " The rating cannot be empty! " + COLOR_RESET);
            return false;
        }
        try {
            Integer rating = Integer.parseInt(readRating);
            if (rating >= 1 && rating <= 5) {
                return true;
            } else {
                System.out
                        .println(COLOR_RED_BACKGROUND + " The rating must be a number between 1 and 5! " + COLOR_RESET);
                return false;
            }

        } catch (NumberFormatException e) {
            System.out
                    .println(COLOR_RED_BACKGROUND + " The rating must be an integer value! " + COLOR_RESET);
            return false; // Parsing failed
        }
        // Überprüfen, ob das Rating im Bereich von 1 bis 5 liegt

    }/*
      * public static boolean helpfulCheck(String readHelpful) {
      * if (readRating == null) {
      * System.out
      * .println(COLOR_RED_BACKGROUND + " The rating cannot be empty! " +
      * COLOR_RESET);
      * return true;
      * }
      * try {
      * Integer rating = Integer.parseInt(readRating);
      * if (rating >= 1 && rating <= 5) {
      * return true;
      * } else {
      * System.out
      * .println(COLOR_RED_BACKGROUND +
      * " The rating must be a number between 1 and 5! " + COLOR_RESET);
      * return false;
      * }
      * 
      * } catch (NumberFormatException e) {
      * System.out
      * .println(COLOR_RED_BACKGROUND + " The rating must be an integer value! " +
      * COLOR_RESET);
      * return false; // Parsing failed
      * }
      * // Überprüfen, ob das Rating im Bereich von 1 bis 5 liegt
      * 
      * }
      */

    public static boolean trollsCheck(Double averageRating) {
        if (averageRating == null) {
            return false;
        }
        if (averageRating >= 1 && averageRating <= 5) {
            return true;
        } else {
            System.out.println(COLOR_RED_BACKGROUND + " The rating must be a number between 1 and 5! " + COLOR_RESET);
            return false;
        }
    }

    public static boolean summaryCheck(String summary) {
        if (summary == null) {
            return false;
        }
        // Regex angepasst, um auch '_', '-', '[', ']', '(', ')' zu erlauben
        if (summary.matches("[A-Za-z0-9_.,;!?\\-\\[\\]\\(\\)]+")) {
            return true;
        } else {
            System.out.println(COLOR_RED_BACKGROUND + " The summary contains invalid characters! " + COLOR_RESET);
            return false;
        }
    }
}
