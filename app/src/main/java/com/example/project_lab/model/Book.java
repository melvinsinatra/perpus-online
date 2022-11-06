package com.example.project_lab.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{

    private int id;
    private String name, author, cover, synopsis;

    public Book(int id, String name, String author, String cover, String synopsis) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.cover = cover;
        this.synopsis = synopsis;
    }

    protected Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
        author = in.readString();
        cover = in.readString();
        synopsis = in.readString();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(cover);
        dest.writeString(synopsis);
    }
}
