package uk.co.mezpahlan.thedroidpicture.data;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.mezpahlan.thedroidpicture.base.BaseMvp;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;
import uk.co.mezpahlan.thedroidpicture.rss.feed.FeedModelInteractor;

/**
 * Created by mpahlan on 24/07/16.
 */
public class BostonGlobe implements RssRepository {
    BostonGlobeClient client;
    BaseMvp.ModelInteractor mvpModelInteractor;

    public BostonGlobe(BaseMvp.ModelInteractor mvpModelInteractor) {
        this.mvpModelInteractor = mvpModelInteractor;
        client = BostonGlobeServiceGenerator.createService(BostonGlobeClient.class);

    }

    @Override
    public void loadFeed(FeedModelInteractor feedModelInteractor) {
        Call<RssFeed> call = client.getRssFeed();

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    onFeedLoaded(response.body());
                } else {
                    Log.e("Mez", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<RssFeed> call, Throwable t) {
                Log.e("Mez", t.getMessage());
            }
        });
    }

    @Override
    public void onFeedLoaded(RssFeed rssFeed) {
        mvpModelInteractor.onFetched(rssFeed);
    }

    @Override
    public void onFeedError() {

    }

    @Override
    public RssFeed.Item loadItem(String itemLink) {
        return null;
    }
}
