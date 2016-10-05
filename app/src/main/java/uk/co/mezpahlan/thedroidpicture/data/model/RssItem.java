package uk.co.mezpahlan.thedroidpicture.data.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

/**
 * Created by mpahlan on 28/07/16.
 */
public class RssItem {
    private String headline;
    private String teaseText;
    private String byline;
    private List<Photo> photos;

    public RssItem(String headline, String teaseText, String byline, List<Photo> photos) {
        this.headline = headline;
        this.teaseText = teaseText;
        this.byline = byline;
        this.photos = photos;
    }

    public String getHeadline() { return headline; }
    public String getTeaseText() { return teaseText; }
    public String getByline() { return byline; }
    public List<Photo> getPhotos() { return photos; }

    @Parcel(Parcel.Serialization.BEAN)
    public static class Photo {
        private String imageLink;
        private String description;

        @ParcelConstructor
        public Photo(String imageLink, String description) {
            this.imageLink = imageLink;
            this.description = description;
        }

        public String getImageLink() {return this.imageLink;}
        public String getDescription() {return this.description;}
    }
}
