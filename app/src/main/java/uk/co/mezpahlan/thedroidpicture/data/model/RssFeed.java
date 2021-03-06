package uk.co.mezpahlan.thedroidpicture.data.model;

import com.google.common.base.Objects;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.data.DescriptionConverter;

@Root(name="rss")
public class RssFeed {

    @Element(name="channel", required=false)
    Channel channel;

    @Attribute(name="version", required=false)
    Double version;

    public Channel getChannel() {return this.channel;}
    public void setChannel(Channel value) {this.channel = value;}

    public Double getVersion() {return this.version;}
    public void setVersion(Double value) {this.version = value;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RssFeed)) return false;
        RssFeed rssFeed = (RssFeed) o;
        return Objects.equal(channel, rssFeed.channel) &&
                Objects.equal(version, rssFeed.version);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(channel, version);
    }

    public static class Image {

        @Element(name="link", required=false)
        String link;

        @Element(name="title", required=false)
        String title;

        @Element(name="url", required=false)
        String url;

        public String getLink() {return this.link;}
        public void setLink(String value) {this.link = value;}

        public String getTitle() {return this.title;}
        public void setTitle(String value) {this.title = value;}

        public String getUrl() {return this.url;}
        public void setUrl(String value) {this.url = value;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Image)) return false;
            Image image = (Image) o;
            return Objects.equal(link, image.link) &&
                    Objects.equal(title, image.title) &&
                    Objects.equal(url, image.url);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(link, title, url);
        }
    }

    public static class Item {

        @Element(name="link", required=false)
        String link;

        @Element(name="description", required=false)
        @Convert(DescriptionConverter.class)
        Description description;

        @Element(name="guid", required=false)
        String guid;

        @Element(name="categories", required=false)
        String categories;

        @Element(name="title", required=false)
        String title;

        @Element(name="pubDate", required=false)
        String pubDate;

        public String getLink() {return this.link;}
        public void setLink(String value) {this.link = value;}

        public Description getDescription() {return this.description;}
        public void setDescription(Description value) {this.description = value;}

        public String getGuid() {return this.guid;}
        public void setGuid(String value) {this.guid = value;}

        public String getCategories() {return this.categories;}
        public void setCategories(String value) {this.categories = value;}

        public String getTitle() {return this.title;}
        public void setTitle(String value) {this.title = value;}

        public String getPubDate() {return this.pubDate;}
        public void setPubDate(String value) {this.pubDate = value;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Item)) return false;
            Item item = (Item) o;
            return Objects.equal(link, item.link) &&
                    Objects.equal(description, item.description) &&
                    Objects.equal(guid, item.guid) &&
                    Objects.equal(categories, item.categories) &&
                    Objects.equal(title, item.title) &&
                    Objects.equal(pubDate, item.pubDate);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(link, description, guid, categories, title, pubDate);
        }
    }

    public static class Channel {

        @Element(name="image", required=false)
        Image image;

        @Element(name="copyright", required=false)
        String copyright;

        @ElementList(name="item", required=false, entry="item", inline=true)
        List<Item> item;

        @Element(name="description", required=false)
        String description;

        @Element(name="generator", required=false)
        String generator;

        @Element(name="language", required=false)
        String language;

        @Element(name="title", required=false)
        String title;

        public Image getImage() {return this.image;}
        public void setImage(Image value) {this.image = value;}

        public String getCopyright() {return this.copyright;}
        public void setCopyright(String value) {this.copyright = value;}

        public List<Item> getItem() {return this.item;}
        public void setItem(List<Item> value) {this.item = value;}

        public String getDescription() {return this.description;}
        public void setDescription(String value) {this.description = value;}

        public String getGenerator() {return this.generator;}
        public void setGenerator(String value) {this.generator = value;}

        public String getLanguage() {return this.language;}
        public void setLanguage(String value) {this.language = value;}

        public String getTitle() {return this.title;}
        public void setTitle(String value) {this.title = value;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Channel)) return false;
            Channel channel = (Channel) o;
            return Objects.equal(image, channel.image) &&
                    Objects.equal(copyright, channel.copyright) &&
                    Objects.equal(item, channel.item) &&
                    Objects.equal(description, channel.description) &&
                    Objects.equal(generator, channel.generator) &&
                    Objects.equal(language, channel.language) &&
                    Objects.equal(title, channel.title);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(image, copyright, item, description, generator, language, title);
        }
    }

    public static class Description {

        String descriptionText;
        String descriptionLink;

        public Description(String descriptionText, String descriptionLink) {
            this.descriptionText = descriptionText;
            this.descriptionLink = descriptionLink;
        }

        public String getDescriptionText() {return this.descriptionText;}
        public void setDescriptionText(String value) {this.descriptionText = value;}

        public String getDescriptionLink() {return descriptionLink;}
        public void setDescriptionLink(String descriptionLink) {this.descriptionLink = descriptionLink;}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Description)) return false;
            Description that = (Description) o;
            return Objects.equal(descriptionText, that.descriptionText) &&
                    Objects.equal(descriptionLink, that.descriptionLink);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(descriptionText, descriptionLink);
        }
    }
}