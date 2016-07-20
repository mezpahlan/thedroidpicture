package uk.co.mezpahlan.thedroidpicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BostonGlobeClient client = BostonGlobeServiceGenerator.createService(BostonGlobeClient.class);

        Call<RssFeed> call = client.getRssFeed();

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    Log.d("Mez", response.body().toString());
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
}
