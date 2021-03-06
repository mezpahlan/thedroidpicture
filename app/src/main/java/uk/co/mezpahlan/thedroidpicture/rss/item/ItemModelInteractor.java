package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.mezpahlan.thedroidpicture.data.BostonGlobeClient;
import uk.co.mezpahlan.thedroidpicture.data.BostonGlobeServiceGenerator;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * ModelInteractor for Rss.Item.
 */
public class ItemModelInteractor implements ItemMvp.ModelInteractor {
    private ItemMvp.Presenter itemPresenter;
    private BostonGlobeClient client;
    private RssItem rssItem;

    public ItemModelInteractor(ItemMvp.Presenter itemPresenter) {
        this.itemPresenter = itemPresenter;
        client = BostonGlobeServiceGenerator.createService(BostonGlobeClient.class);
    }

    @Override
    public void fetch(String itemUrl) {
        Call<ResponseBody> call = client.getRssItem(itemUrl);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        rssItem = convertHtmlToRssItem(response.body());
                        onFetched(rssItem);
                    } catch (IOException e) {
                        onFailure(call, e);
                    }
                } else {
                    // We just want the error. Don't do any conversions here.
                    onError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // We just want the error. Don't do any conversions here.
                onError();
            }
        });
    }

    @Override
    public void fetchCached(String itemUrl) {
        if (rssItem == null) {
            fetch(itemUrl);
        } else {
            onFetched(rssItem);
        }
    }

    @Override
    public void onFetched(RssItem rssItem) {
        itemPresenter.onLoadSuccess(rssItem.getPhotos());
    }

    @Override
    public void onError() {
        itemPresenter.onLoadError();
    }

    private RssItem convertHtmlToRssItem(ResponseBody body) throws IOException {
        // TODO: body.string() or body.charStream() or a custom converter??
        Document doc = Jsoup.parse(body.string());
        String headline = doc.select(".pictureInfo-headline").size() > 0 ? doc.select(".pictureInfo-headline").first().ownText() :
                "";
        String teaseText = doc.select(".subhead").size() > 0 ? doc.select(".subhead").first().ownText() : "";
        String byLine = doc.select(".byname").size() > 0 ? doc.select(".byname").first().ownText() : "";
        List<Element> photoLinks = doc.select("div.photo img");
        List<Element> captions = doc.select("article.pcaption");

        // The page markup doesn't have sane groupings for photo links and photo captions
        // Instead they are next to one another so we have to assume adjacent elements are related.
        // Check that we have the same number of photoLinks and captions
        List<RssItem.Photo> photoList;
        if (photoLinks.size() == captions.size()) {
            photoList = new ArrayList<>(0);
            for (int i = 0; i < photoLinks.size(); i++) {
                final Element captionElement = captions.get(i).select("div.gcaption").first();
                final Element photoElement = photoLinks.get(i);
                String caption = captionElement != null ? captionElement.ownText() : "Caption not available";
                String photoLink = "http:" + photoElement.attr("src");
                photoList.add(new RssItem.Photo(photoLink, caption));
            }
        } else {
            onError();
            return null;
        }

        return new RssItem(headline, teaseText, byLine, photoList);
    }
}
