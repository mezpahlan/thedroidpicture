package uk.co.mezpahlan.thedroidpicture;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mpahlan on 21/07/16.
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
        while (matcher.find()) {
            link = matcher.group(1);
        }

        String text = nodeText.replaceFirst(IMG_SRC_REG_EX, "")
                              .replaceAll(HTML_TAG_REG_EX, "");

        return new RssFeed.Description(text, link);
    }

    @Override
    public void write(OutputNode node, RssFeed.Description value) throws Exception {

    }
}
