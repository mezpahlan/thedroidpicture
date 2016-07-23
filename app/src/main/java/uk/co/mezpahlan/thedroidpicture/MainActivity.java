package uk.co.mezpahlan.thedroidpicture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<RssFeed.Item> rssList = new ArrayList<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        final RssRecyclerViewAdapter rcAdapter = new RssRecyclerViewAdapter(rssList);
        recyclerView.setAdapter(rcAdapter);

        BostonGlobeClient client = BostonGlobeServiceGenerator.createService(BostonGlobeClient.class);

        Call<RssFeed> call = client.getRssFeed();

        call.enqueue(new Callback<RssFeed>() {
            @Override
            public void onResponse(Call<RssFeed> call, Response<RssFeed> response) {
                if (response.isSuccessful()) {
                    // turn all the items into list<rssfeed.items> and update the local variable
                    rssList.addAll(response.body().getChannel().getItem());
                    rcAdapter.notifyDataSetChanged();
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
