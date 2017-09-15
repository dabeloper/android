package com.dabeloper.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by David Antonio Bolaños AKA DABELOPER on 07/09/2017.
 */

public class IOSApp implements Parcelable {

    //generally pixels: 53, 75 and 100
    private String[] images;

    private String  title,
                    name,
                    summary,
                    rights,
                    link,
                    price,
                    releaseDate,
                    owner,
                    categoryTitle,
                    packageName,
                    textId;

    private int categoryId,
                numberId;

    //The only one var that is not setted directly from JSON
    //this is getted from SQLite database
    private boolean hasType = false;


    private IOSApp(Parcel in) {
        readFromParcel(in);
    }

    public IOSApp(JSONObject object){

        if( object.has("im:image") ){
            images = new String[3];
            try {
                JSONArray imgObject = object.getJSONArray("im:image");
                images[0] = imgObject.getJSONObject(0).getString("label");
                images[1] = imgObject.getJSONObject(1).getString("label");
                images[2] = imgObject.getJSONObject(2).getString("label");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }//END images

        try {
            title   = object.has("title")   ? object.getJSONObject("title").getString("label") : "";
            name    = object.has("im:name") ? object.getJSONObject("im:name").getString("label") : "";
            summary = object.has("summary") ? object.getJSONObject("summary").getString("label") : "";
            rights  = object.has("rights")  ? object.getJSONObject("rights").getString("label") : "";
            link    = object.has("link")    ? object.getJSONObject("link").getJSONObject("attributes").getString("href") : "";

            if( object.has("im:price") ){
                JSONObject priceObj = object.getJSONObject("im:price").getJSONObject("attributes");
                price               = priceObj.getString("amount") + " " + priceObj.getString("currency");
            }//END price

            releaseDate     = object.has("im:releaseDate")    ? object.getJSONObject("im:releaseDate").getString("label") : "";
            owner           = object.has("im:artist") ? object.getJSONObject("im:artist").getString("label") : "";

            if( object.has("category") ){
                JSONObject categoryObj = object.getJSONObject("category").getJSONObject("attributes");
                categoryTitle   = categoryObj.getString("label");
                categoryId      = categoryObj.getInt("im:id");
            }//END category

            if( object.has("id") ){
                JSONObject idObj    = object.getJSONObject("id").getJSONObject("attributes");
                packageName         = idObj.getString("im:bundleId");
                numberId            = idObj.getInt("im:id");
                textId              = idObj.getString("im:id");
            }//END id

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }//END Constructor


    public String[] getImages() {
        return images;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getRights() {
        return rights;
    }

    public String getLink() {
        return link;
    }

    public String getPrice() {
        return price;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOwner() {
        return owner;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getTextId() {
        return textId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getNumberId() {
        return numberId;
    }

    public boolean hasType() {
        return hasType;
    }

    public void setHasType(boolean hasType) {
        this.hasType = hasType;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    private void readFromParcel(Parcel in) {

        this.images         = in.createStringArray();

        this.title          = in.readString();
        this.name           = in.readString();
        this.summary        = in.readString();
        this.rights         = in.readString();
        this.link           = in.readString();
        this.price          = in.readString();
        this.releaseDate    = in.readString();
        this.owner          = in.readString();
        this.categoryTitle  = in.readString();
        this.textId         = in.readString();

        this.numberId       = in.readInt();
        this.categoryId     = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeArray(images);

        parcel.writeString(title);
        parcel.writeString(name);
        parcel.writeString(summary);
        parcel.writeString(rights);
        parcel.writeString(link);
        parcel.writeString(price);
        parcel.writeString(releaseDate);
        parcel.writeString(owner);
        parcel.writeString(categoryTitle);
        parcel.writeString(textId);

        parcel.writeInt(numberId);
        parcel.writeInt(categoryId);
    }


    public static final Parcelable.Creator<IOSApp> CREATOR
            = new Parcelable.Creator<IOSApp>() {
        public IOSApp createFromParcel(Parcel in) {
            return new IOSApp(in);
        }

        public IOSApp[] newArray(int size) {
            return new IOSApp[size];
        }
    };

}
