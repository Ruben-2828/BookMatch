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

    private Float rating;


    public Book(@NonNull String id, String title, ArrayList<String> authors, String description,
                String publicationYear, String coverURI, boolean isSaved, boolean isReviewed,
                String review, Float rating){
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.publicationYear = publicationYear;
        this.coverURI = coverURI;
        this.isSaved = isSaved;
        this.review = review;
        this.isReviewed = isReviewed;
        this.rating = rating;
    }

    @NonNull
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

    public Float getRating() {
        return rating;
    }

    public void setId(@NonNull String id) {
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

    public void setRating(Float rating) {
        this.rating = rating;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeStringList(this.authors);
        dest.writeString(this.description);
        dest.writeString(this.publicationYear);
        dest.writeString(this.coverURI);
        dest.writeByte(this.isSaved ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isReviewed ? (byte) 1 : (byte) 0);
        dest.writeString(this.review);
        dest.writeValue(this.rating);
    }

    public void readFromParcel(Parcel source) {
        this.id = Objects.requireNonNull(source.readString());
        this.title = source.readString();
        this.authors = source.createStringArrayList();
        this.description = source.readString();
        this.publicationYear = source.readString();
        this.coverURI = source.readString();
        this.isSaved = source.readByte() != 0;
        this.isReviewed = source.readByte() != 0;
        this.review = source.readString();
        this.rating = (Float) source.readValue(Float.class.getClassLoader());
    }

    protected Book(Parcel in) {
        this.id = Objects.requireNonNull(in.readString());
        this.title = in.readString();
        this.authors = in.createStringArrayList();
        this.description = in.readString();
        this.publicationYear = in.readString();
        this.coverURI = in.readString();
        this.isSaved = in.readByte() != 0;
        this.isReviewed = in.readByte() != 0;
        this.review = in.readString();
        this.rating = (Float) in.readValue(Float.class.getClassLoader());
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
