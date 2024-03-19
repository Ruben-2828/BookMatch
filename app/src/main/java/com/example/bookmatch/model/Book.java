package com.example.bookmatch.model;

import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long book_id;

    @SerializedName("id")
    private String key;

    @SerializedName("title")
    private String title;
    @ColumnInfo(name = "author_name")
    @SerializedName("author_name")
    private ArrayList<String> authors;
    @ColumnInfo(name = "first_sentence")
    @SerializedName("first_sentence")
    private ArrayList<String> firstSentence;
    @ColumnInfo(name = "first_publication_year")
    @SerializedName("first_publish_year")
    private String publicationYear;

    private ArrayList<String> subject;
    @ColumnInfo(name = "cover_i")
    @SerializedName("cover_i")
    private String coverID;

    @ColumnInfo(name = "is_saved")
    private boolean isSaved;


    public Book(String key, String title, ArrayList<String> authors, ArrayList<String> firstSentence,
                String publicationYear, ArrayList<String> subject, String coverID, boolean isSaved){
        this.key = key;
        this.title = title;
        this.authors = authors;
        this.firstSentence = firstSentence;
        this.publicationYear = publicationYear;
        this.subject = subject;
        this.coverID = coverID;
        this.isSaved = isSaved;
    }

    protected Book(Parcel in) {
        book_id = in.readLong();
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

    public long getBook_id() { return book_id; }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getFirstSentence() {
        return firstSentence;
    }

    public ArrayList<String> getSubject() {
        return subject;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getCoverID() { return coverID; }

    public boolean isSaved() {
        return isSaved;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void setFirstSentence(ArrayList<String> firstSentence) {
        this.firstSentence = firstSentence;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setSubject(ArrayList<String> subject) {
        this.subject = subject;
    }

    public void setCoverID(String coverID) {
        this.coverID = coverID;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getCoverURI() {
        return String.format(API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT, this.coverID);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + book_id + '\'' +
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
        dest.writeLong(book_id);
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeStringList(firstSentence);
        dest.writeString(publicationYear);
        dest.writeString(coverID);
    }
}
