package br.edu.utfpr.alunos.rodrigodea.projetoinicial_real.persistence;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) { return value == null ? null : new Date(value); }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
