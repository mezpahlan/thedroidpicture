package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;
import uk.co.mezpahlan.thedroidpicture.rss.item.ItemActivity;

/**
 * Created by mpahlan on 24/07/16.
 */
public class FeedFragment extends Fragment implements FeedMvp.View {

    private FeedRecyclerViewAdapter listAdapter;
    private FeedMvp.Presenter presenter;
    private List<RssFeed.Item> rssList = new ArrayList<>(0);

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new FeedRecyclerViewAdapter(rssList);
    }

    @Override
    public void onResume(){
        super.onResume();

        presenter.load(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        presenter = new FeedPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load(true);
            }
        });

        return root;

    }

    @Override
    public void showLoading(boolean active) {
        // TODO: Show a loading icon?
    }

    @Override
    public void showContent(List<RssFeed.Item> rssItems) {
        rssList.addAll(rssItems);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void showRssItem(String rssItemLink) {
        // Naive implementation using an intent to move between activities
        // TODO: Think about using an event bus??
        // TODO: Think about getContext() which is API 23
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(ItemActivity.EXTRA_ITEM_URL, rssItemLink);
        startActivity(intent);
    }
}
