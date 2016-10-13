package uk.co.mezpahlan.thedroidpicture.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;

/**
 * Retrofit client for The Boston Globe
 */
public interface BostonGlobeClient {

    @GET("rss/bigpicture")
    Call<RssFeed> getRssFeed();

    @GET
    Call<ResponseBody> getRssItem(@Url String itemUrl);
}
