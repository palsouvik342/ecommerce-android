package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_GIFT_List;

import android.content.Context;

import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class BannerModel {

    Context context;
    VolleyService volleyService;
    String imageUrl;
    String imageVideoLink;

    public String getImageUrl() {
        return imageUrl;
    }

    public BannerModel(String imageUrl, String imageVideoLink) {
        this.imageUrl = imageUrl;
        this.imageVideoLink = imageVideoLink;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageVideoLink() {
        return imageVideoLink;
    }

    public void setImageVideoLink(String imageVideoLink) {
        this.imageVideoLink = imageVideoLink;
    }

}
