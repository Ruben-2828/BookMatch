package com.example.bookmatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class AppUser implements Parcelable {

    private String id;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String pic;

    public AppUser(String id, String nickname, String firstName, String lastName, String email, String pic) {
        this.id = id;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pic = pic;
    }

    protected AppUser(Parcel in) {
        id = in.readString();
        nickname = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        pic = in.readString();
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

    public String getPic() {
        return pic;
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
        dest.writeString(pic);
    }



}
