package com.example.bookmatch.model;

import static com.example.bookmatch.utils.Constants.API_RETRIEVE_BOOK_COVER_BY_ID_ENDPOINT;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Book implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long book_id;

    private String key;

    private String title;

    @ColumnInfo(name = "author_name")
    private ArrayList<String> authors;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "first_publication_year")
    private String publicationYear;

    @ColumnInfo(name = "cover_uri")
    private String coverURI;

    @ColumnInfo(name = "is_saved")
    private boolean isSaved;


    public Book(String key, String title, ArrayList<String> authors, String description,
                String publicationYear, String coverURI, boolean isSaved){
        this.key = key;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publicationYear = publicationYear;
        this.coverURI = coverURI;
        this.isSaved = isSaved;
    }

    protected Book(Parcel in) {
        book_id = in.readLong();
        title = in.readString();
        authors = in.createStringArrayList();
        description = in.readString();
        publicationYear = in.readString();
        coverURI = in.readString();
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

    public String getDescription() {
        return description;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public String getCoverURI() { return coverURI; }

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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setCoverURI(String coverURI) {
        this.coverURI = coverURI;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", description='" + description + '\'' +
                ", publicationYear='" + publicationYear + '\'' +
                ", coverURI='" + coverURI + '\'' +
                ", isSaved=" + isSaved +
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
        dest.writeString(description);
        dest.writeString(publicationYear);
        dest.writeString(coverURI);
    }
}
