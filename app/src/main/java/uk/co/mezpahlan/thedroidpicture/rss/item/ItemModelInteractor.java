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
 * Created by mpahlan on 28/07/16.
 */
public class ItemModelInteractor implements ItemMvp.ModelInteractor {
    private ItemMvp.Presenter itemPresenter;
    private BostonGlobeClient client;

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
                        onFetched(convertHtmlToRssItem(response.body()));
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
    public void onFetched(RssItem rssItem) {
        itemPresenter.onLoadSuccess(rssItem.getPhotos());
    }

    @Override
    public void onError() {
        // TODO: Implement me
        Log.e("Mez", "Something went wrong in the ItemModelInteractor");
    }

    private RssItem convertHtmlToRssItem(ResponseBody body) throws IOException {
        // TODO: body.string() or body.charStream() or a custom converter??
        Document doc = Jsoup.parse(body.string());
        String headline = doc.select(".pictureInfo-headline").size() > 0 ? doc.select(".pictureInfo-headline").first().ownText() :
                "";
        String teaseText = doc.select(".subhead").size() > 0 ? doc.select(".subhead").first().ownText() : "";
        String byLine = doc.select(".byname").size() > 0 ? doc.select(".byname").first().ownText() : "";
        List<Element> photoLinks = doc.select("div.photo img");
        List<Element> captions = doc.select("article.pcaption div.gcaption");

        // The page markup doesn't have sane groupings for photo links and photo captions
        // Instead they are next to one another so we have to assume adjacent elements are related.
        // Check that we have the same number of photoLinks and captions
        List<RssItem.Photo> photoList;
        if (photoLinks.size() == captions.size()) {
            photoList = new ArrayList<>(0);
            for (int i = 0; i < photoLinks.size(); i++) {
                photoList.add(new RssItem.Photo("https:" + photoLinks.get(i).attr("src"), captions.get(i).ownText()));
            }
        } else {
            // TODO: Throw error because we haven't matched links to captions.
            onError();
            return null;
        }

        return new RssItem(headline, teaseText, byLine, photoList);
    }
}
