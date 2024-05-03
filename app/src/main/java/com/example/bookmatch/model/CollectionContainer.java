package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class CollectionContainer implements Parcelable {

    @PrimaryKey
    @NonNull
    private String name;
    private String description;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imageData;

    public CollectionContainer(@NonNull String name, String description, byte[] imageData) {
        this.name = name;
        this.description = description;
        this.imageData = imageData;
    }

    protected CollectionContainer(Parcel in) {
        name = Objects.requireNonNull(in.readString());
        description = in.readString();
        imageData = in.createByteArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByteArray(imageData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CollectionContainer> CREATOR = new Creator<CollectionContainer>() {
        @Override
        public CollectionContainer createFromParcel(Parcel in) {
            return new CollectionContainer(in);
        }

        @Override
        public CollectionContainer[] newArray(int size) {
            return new CollectionContainer[size];
        }
    };

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionContainer)) return false;
        CollectionContainer that = (CollectionContainer) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

