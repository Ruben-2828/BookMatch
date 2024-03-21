package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Book implements Parcelable {

    @PrimaryKey()
    @NonNull
    private String id;

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


    public Book(String id, String title, ArrayList<String> authors, String description,
                String publicationYear, String coverURI, boolean isSaved){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publicationYear = publicationYear;
        this.coverURI = coverURI;
        this.isSaved = isSaved;
    }

    protected Book(Parcel in) {
        id = in.readString();
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


    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
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
        dest.writeString(id);
        dest.writeString(title);
        dest.writeStringList(authors);
        dest.writeString(description);
        dest.writeString(publicationYear);
        dest.writeString(coverURI);
    }
}
