package com.kunalcodes.feedme.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class FoursquarePhotosResponse {
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("response")
    @Expose
    private Response response;

    @Data
    public static class Meta {

        @SerializedName("code")
        @Expose
        private int code;
        @SerializedName("requestId")
        @Expose
        private String requestId;

    }

    @Data
    public static class Response {

        @SerializedName("photos")
        @Expose
        private Photos photos;

    }

    @Data
    public static class Photos {

        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("items")
        @Expose
        private List<Item> items;
        @SerializedName("dupesRemoved")
        @Expose
        private int dupesRemoved;

    }

    @Data
    public static class Item {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("createdAt")
        @Expose
        private int createdAt;
        @SerializedName("source")
        @Expose
        private Source source;
        @SerializedName("prefix")
        @Expose
        private String prefix;
        @SerializedName("suffix")
        @Expose
        private String suffix;
        @SerializedName("width")
        @Expose
        private int width;
        @SerializedName("height")
        @Expose
        private int height;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("checkin")
        @Expose
        private Checkin checkin;
        @SerializedName("visibility")
        @Expose
        private String visibility;

    }

    @Data
    public static class Checkin {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("createdAt")
        @Expose
        private int createdAt;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("timeZoneOffset")
        @Expose
        private int timeZoneOffset;

    }

    @Data
    public static class Source {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("url")
        @Expose
        private String url;

    }

    @Data
    public static class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("photo")
        @Expose
        private Photo photo;

    }

    @Data
    public static class Photo {

        @SerializedName("prefix")
        @Expose
        private String prefix;
        @SerializedName("suffix")
        @Expose
        private String suffix;

    }


}
