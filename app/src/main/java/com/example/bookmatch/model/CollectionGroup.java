package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;

import java.util.Objects;

@Entity(tableName = "collectionGroup",
        primaryKeys = {"collectionName", "bookId"}, // composite primary key
        indices = {@Index(value = {"collectionName"}), @Index(value = {"bookId"})}
)
public class CollectionGroup implements Parcelable {
    @ColumnInfo(name = "collectionName")
    @NonNull
    private String collectionName;

    @ColumnInfo(name = "bookId")
    @NonNull
    private String bookId;

    public CollectionGroup(@NonNull String collectionName, @NonNull String bookId) {
        this.collectionName = collectionName;
        this.bookId = bookId;
    }

    protected CollectionGroup(Parcel in) {
        collectionName = Objects.requireNonNull(in.readString());
        bookId = Objects.requireNonNull(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(collectionName);
        dest.writeString(bookId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectionGroup> CREATOR = new Creator<CollectionGroup>() {
        @Override
        public CollectionGroup createFromParcel(Parcel in) {
            return new CollectionGroup(in);
        }

        @Override
        public CollectionGroup[] newArray(int size) {
            return new CollectionGroup[size];
        }
    };

    @NonNull
    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(@NonNull String collectionName) {
        this.collectionName = collectionName;
    }

    @NonNull
    public String getBookId() {
        return bookId;
    }

    public void setBookId(@NonNull String bookId) {
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionGroup)) return false;
        CollectionGroup that = (CollectionGroup) o;
        return Objects.equals(getCollectionName(), that.getCollectionName()) && Objects.equals(getBookId(), that.getBookId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCollectionName(), getBookId());
    }
}

