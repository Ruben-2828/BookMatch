package com.example.bookmatch.utils;

import androidx.room.TypeConverter;
import java.util.ArrayList;
import java.util.Arrays;

public class Converters {
    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {

        StringBuilder sb = new StringBuilder();
        if(list == null) {
            return sb.toString();
        }
        for (String s : list) {
            sb.append(s);
            sb.append(",");
        }
        return sb.toString();
    }

    @TypeConverter
    public static ArrayList<String> toArrayList(String string) {
        String[] array = string.split(",");
        return new ArrayList<>(Arrays.asList(array));
    }
}
