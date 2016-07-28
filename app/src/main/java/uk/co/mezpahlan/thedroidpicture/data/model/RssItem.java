package uk.co.mezpahlan.thedroidpicture.data.model;

import java.util.List;

/**
 * Created by mpahlan on 28/07/16.
 */
public class RssItem {

    private String headline;
    private String teaseText;
    private String byline;
    private String date;
    private List<Photo> photos;

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getTeaseText() { return teaseText; }
    public void setTeaseText(String teaseText) { this.teaseText = teaseText; }

    public String getByline() { return byline; }
    public void setByline(String byline) { this.byline = byline; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }

    public static class Photo {
        private String imageLink;
        private String description;

        public String getImageLink() {return this.imageLink;}
        public void setImageLink(String value) {this.imageLink = value;}

        public String getDescription() {return this.description;}
        public void setDescription(String value) {this.description = value;}
    }
}
