package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtils {

    public static String formatDateToString(Date date) {
        SimpleDateFormat dateFormat;

        if (Locale.getDefault().getLanguage().equals("en")) {
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        } else {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        }

        return dateFormat.format(date);
    }

    public static Date formatDateToDate(String stringData) throws ParseException {
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss");

        return dateFormat.parse(stringData);
    }

    public static String formatDateToHour(Date date) {
        SimpleDateFormat dateFormat;

        dateFormat = new SimpleDateFormat("HH:mm");

        return dateFormat.format(date);
    }
}
