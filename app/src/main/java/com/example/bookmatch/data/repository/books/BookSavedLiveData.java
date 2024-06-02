package com.example.bookmatch.data.repository.books;

import static com.example.bookmatch.utils.Constants.BOOKS_JSON_DIRECTORY;

import android.content.Context;

import com.example.bookmatch.utils.JSONParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.example.bookmatch.model.Book;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookSavedLiveData implements IBookSavedLiveData {
    private JSONParser jsonParser;
    private Gson gson;
    public BookSavedLiveData(Context context){
        this.jsonParser = new JSONParser(context);
        this.gson = new Gson();
    }
    @Override
    public ArrayList<Book> getSavedBooks() {
        String json = jsonParser.readLocalJsonFile(BOOKS_JSON_DIRECTORY);
        Type bookListType = new TypeToken<List<Book>>() {}.getType();
        return gson.fromJson(json, bookListType);
    }

    @Override
    public boolean saveBooks(ArrayList<Book> books) {
        String booksJson = gson.toJson(books);

        try{
            jsonParser.saveJsonToFile(booksJson, BOOKS_JSON_DIRECTORY);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
