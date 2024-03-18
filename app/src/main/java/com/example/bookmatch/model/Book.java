package com.example.bookmatch.model;

import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT;
import static com.example.bookmatch.utils.Constants.API_BASE_URL;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Book implements Parcelable {

    @SerializedName("key")
    private final String id;
    private final String title;
    @SerializedName("author_name")
    private final ArrayList<String> authors;
    @SerializedName("first_sentence")
    private final ArrayList<String> firstSentence;
    @SerializedName("first_publish_year")
    private final String publicationYear;
    @SerializedName("cover_i")
    private final String coverID;

    public Book(String id, String title, ArrayList<String> authors, ArrayList<String> firstSentence, String publicationYear, String coverID) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.firstSentence = firstSentence;
        this.publicationYear = publicationYear;
        this.coverID = coverID;
    }

    protected Book(Parcel in) {
        id = in.readString();
        title = in.readString();
        authors = in.createStringArrayList();
        firstSentence = in.createStringArrayList();
        publicationYear = in.readString();
        coverID = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getFirstSentence() {
        return firstSentence;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getCoverID() { return coverID; }

    public String getCoverURI() {
        return String.format(API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT, this.coverID);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + authors + '\'' +
                ", plot='" + firstSentence + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", coverID='" + coverID + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeStringList(firstSentence);
        dest.writeString(publicationYear);
        dest.writeString(coverID);
    }
}
