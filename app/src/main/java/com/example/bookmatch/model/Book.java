package com.example.bookmatch.model;

import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT;
import static com.example.bookmatch.utils.Constants.BASE_API_URL;

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
    @SerializedName("key")
    private long id;
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
    @ColumnInfo(name = "cover_i")
    @SerializedName("cover_i")
    private String coverID;

    @ColumnInfo(name = "is_saved")
    private boolean isSaved;


    public Book(String title, ArrayList<String> authors, ArrayList<String> firstSentence, String publicationYear, String coverID, boolean isSaved){
        this.title = title;
        this.authors = authors;
        this.firstSentence = firstSentence;
        this.publicationYear = publicationYear;
        this.coverID = coverID;
        this.isSaved = isSaved;
    }

    protected Book(Parcel in) {
        id = in.readLong();
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

    public long getId() { return id; }

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

    public boolean isSaved() {
        return isSaved;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setCoverID(String coverID) {
        this.coverID = coverID;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public String getCoverURI() {
        return String.format(BASE_API_URL + API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT, this.coverID);
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
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeStringList(firstSentence);
        dest.writeString(publicationYear);
        dest.writeString(coverID);
    }
}
