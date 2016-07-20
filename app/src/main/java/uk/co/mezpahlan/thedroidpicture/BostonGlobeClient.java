package uk.co.mezpahlan.thedroidpicture;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mpahlan on 19/07/16.
 */
public interface BostonGlobeClient {
    @GET("rss/bigpicture")
    Call<RssFeed> getRssFeed();
}
