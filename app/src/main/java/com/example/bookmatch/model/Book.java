package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Objects;

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


    @ColumnInfo(name = "is_reviewed")
    private boolean isReviewed;

    private String review;


    public Book(String id, String title, ArrayList<String> authors, String description,
                String publicationYear, String coverURI, boolean isSaved, boolean isReviewed, String review){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publicationYear = publicationYear;
        this.coverURI = coverURI;
        this.isSaved = isSaved;
        this.review = review;
        this.isReviewed = isReviewed;
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

    public String getReview() { return review; }

    public boolean isReviewed() {
        return isReviewed;
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

    public void setReview(String review){
        this.review = review;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
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
                ", review='" + review + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
