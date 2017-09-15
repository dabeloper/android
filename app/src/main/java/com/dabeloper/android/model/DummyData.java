package com.dabeloper.android.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 31/07/2017.
 */


public class DummyData implements Parcelable {

    private String title;
    private String body;
    private String id;
    private String type; //can be usefully for some objects

    public DummyData(String title, String body, String id) {
        this.title  = title;
        this.body   = body;
        this.id     = id;
        this.type   = null;
    }


    public DummyData(String title, String body, String id, String type) {
        this.title  = title;
        this.body   = body;
        this.id     = id;
        this.type   = type;
    }

    public DummyData(Parcel in) {
        this.title  = null;
        this.body   = null;
        this.id     = null;
        this.type   = null;
        readFromParcel(in);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void readFromParcel(Parcel in) {
        this.title  = in.readString();
        this.body   = in.readString();
        this.id     = in.readString();
        this.type   = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(id);
        dest.writeString(type);
    }

    @Override
    public String toString() {
        return "DummyData{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static final Parcelable.Creator<DummyData> CREATOR
            = new Parcelable.Creator<DummyData>() {
        public DummyData createFromParcel(Parcel in) {
            return new DummyData(in);
        }

        public DummyData[] newArray(int size) {
            return new DummyData[size];
        }
    };


}