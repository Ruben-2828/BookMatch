package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

//TODO: Eliminare e spostare eventualmente le informazioni di questo utili nell'altro user
public class AppUser implements Parcelable {

    private String id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String cover;

    public AppUser(String id, String nickname, String firstName, String lastName, String email, String cover) {
        this.id = id;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.cover = cover;
    }

    protected AppUser(Parcel in) {
        id = in.readString();
        nickname = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        cover = in.readString();
    }

    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel in) {
            return new AppUser(in);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getCover() {
        return cover;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nickname);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(cover);
    }


}
