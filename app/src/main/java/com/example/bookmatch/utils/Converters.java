package com.example.bookmatch.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.CollectionContainer;

import java.io.ByteArrayOutputStream;
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

    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @TypeConverter
    public static Bitmap toBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
