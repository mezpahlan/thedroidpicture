package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.mezpahlan.thedroidpicture.data.BostonGlobeClient;
import uk.co.mezpahlan.thedroidpicture.data.BostonGlobeServiceGenerator;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 24/07/16.
 */
public class FeedModelInteractor implements FeedMvp.ModelInteractor {
    private static final String TAG = "FeedModelInteractor";
    private FeedMvp.Presenter feedPresenter;
    private BostonGlobeClient client;

    public FeedModelInteractor(FeedMvp.Presenter feedPresenter) {
        this.feedPresenter = feedPresenter;
        client = BostonGlobeServiceGenerator.createService(BostonGlobeClient.class);
    }

    @Override
    public void fetch() {
        Call<RssFeed> call = client.getRssFeed();

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    // We just want the response. Don't do any conversions here.
                    onFetched(response.body());
                } else {
                    // We just want the error. Don't do any conversions here.
                    onError();
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                // We just want the error. Don't do any conversions here.
                onError();
            }
        });
    }

    @Override
    public void onFetched(RssFeed rssFeed) {
        List<RssFeed.Item> rssItems = rssFeed.getChannel().getItem();
        feedPresenter.onLoadSuccess(rssItems);
    }

    @Override
    public void onError() {
        // TODO: Implement me
        Log.e("Mez", "Something went wrong in the FeedModelInteractor");
    }

    @Override
    public void onDestroy() {
        // TODO: Implement me
        Log.d(TAG, "onDestroy: Clean up model interactor long running stuff here.");
    }
}
