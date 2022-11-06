package com.example.project_lab.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Requests implements Parcelable {

    private int id, book_id, requester_id, receiver_id;
    private double latitude, longitude;

    public Requests(int id, int book_id, int requester_id, int receiver_id, double latitude, double longitude) {
        this.id = id;
        this.book_id = book_id;
        this.requester_id = requester_id;
        this.receiver_id = receiver_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Requests(Parcel in) {
        id = in.readInt();
        book_id = in.readInt();
        requester_id = in.readInt();
        receiver_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Requests> CREATOR = new Creator<Requests>() {
        @Override
        public Requests createFromParcel(Parcel in) {
            return new Requests(in);
        }

        @Override
        public Requests[] newArray(int size) {
            return new Requests[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getRequester_id() {
        return requester_id;
    }

    public void setRequester_id(int requester_id) {
        this.requester_id = requester_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(book_id);
        dest.writeInt(requester_id);
        dest.writeInt(receiver_id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
