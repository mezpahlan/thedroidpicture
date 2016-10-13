package uk.co.mezpahlan.thedroidpicture.data;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit Service Generator for The Boston Globe.
 */
public class BostonGlobeServiceGenerator {

    private static final String API_BASE_URL = "http://www.bostonglobe.com";

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())));

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
