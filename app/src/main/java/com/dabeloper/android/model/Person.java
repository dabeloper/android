package com.dabeloper.android.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.dabeloper.android.sqlite_model.PersonContract;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 11/08/2017.
 */

public class Person implements Parcelable{

    private long id;
    private String  name,
                    profession,
                    curriculum;

    private Person(Parcel in) {
        this.id             = 0;
        this.name           = null;
        this.profession   = null;
        this.curriculum   = null;
        readFromParcel(in);
    }

    public Person(long id, String name, String profession, String curriculum) {
        this.id = id;
        this.name = name;
        this.profession = profession;
        this.curriculum = curriculum;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PersonContract.FeedEntry._ID, id);
        values.put(PersonContract.FeedEntry.COLUMN_NAME_NAME, name);
        values.put(PersonContract.FeedEntry.COLUMN_NAME_PROFESSION, profession);
        values.put(PersonContract.FeedEntry.COLUMN_NAME_CURRICULUM, curriculum);
        return values;
    }


    private void readFromParcel(Parcel in) {
        this.id             = in.readLong();
        this.name           = in.readString();
        this.profession     = in.readString();
        this.curriculum     = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(profession);
        dest.writeString(curriculum);
    }


    public static final Parcelable.Creator<Person> CREATOR
            = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
