package com.example.bookmatch.utils;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

public class JSONParser {
    private final Context context;

    public JSONParser(Context context){
        this.context = context;
    }

    public String readLocalJsonFile(String fileName) {
        String json = null;
        try {
            File path = context.getFilesDir();
            File readFrom = new File(path, fileName);
            FileInputStream stream = new FileInputStream(readFrom);
            byte[] content = new byte[(int)readFrom.length()];
            stream.read(content);
            json = new String(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public void saveJsonToFile(String jsonString, String filename) throws IOException {
        File path = context.getFilesDir();
        FileOutputStream writer = new FileOutputStream(new File(path, filename));
        writer.write(jsonString.getBytes());
        writer.close();
    }
}
