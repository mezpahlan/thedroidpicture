package uk.co.mezpahlan.thedroidpicture.data;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * DescriptionConverter scrapes the description of the item and link to the item
 * from the received description from the RSS feed. We're not currently writing
 * data back to the server so no need to implement the reverse operation.
 */
public class DescriptionConverter implements Converter<RssFeed.Description> {

    @Override
    public RssFeed.Description read(InputNode node) throws Exception {
        final String IMG_SRC_REG_EX = "<img src=([^>]+)>";
        final String HTML_TAG_REG_EX = "</?[^>]+>";

        // TODO: Named capture groups
        String nodeText = node.getValue();

        Pattern imageLinkPattern = Pattern.compile(IMG_SRC_REG_EX);
        Matcher matcher = imageLinkPattern.matcher(nodeText);

        String link = null;
        int startIndex = 0;
        while (matcher.find()) {
            link = matcher.group(1);
            startIndex = matcher.start(1);
        }

        // Remove everything after the link because that represents the actual
        // image description rather than the rss item description. We take
        // substring - 9 which represents the start of the link we extracted
        // moved backward to account for '<src img=' that our regex does not
        // take into account.
        String text = nodeText.substring(0, startIndex - 9)
                              .replaceAll(HTML_TAG_REG_EX, "");

        return new RssFeed.Description(text, link);
    }

    @Override
    public void write(OutputNode node, RssFeed.Description value) throws Exception {

    }
}
