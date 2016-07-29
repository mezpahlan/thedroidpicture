package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
                    onFetched(convertHtmlToRssItem(response.body()));
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

    private RssItem convertHtmlToRssItem(ResponseBody body) {
        Document doc = Jsoup.parse(body.toString());
        return null;
    }
}
