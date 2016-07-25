package uk.co.mezpahlan.thedroidpicture.data;

import retrofit2.Call;
import retrofit2.http.GET;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Created by mpahlan on 19/07/16.
 */
public interface BostonGlobeClient {
    @GET("rss/bigpicture")
    Call<RssFeed> getRssFeed();
}
