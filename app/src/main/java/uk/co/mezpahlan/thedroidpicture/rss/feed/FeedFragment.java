package uk.co.mezpahlan.thedroidpicture.rss.feed;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.base.StateMaintainer;
import uk.co.mezpahlan.thedroidpicture.data.model.RssFeed;
import uk.co.mezpahlan.thedroidpicture.rss.item.ItemActivity;

/**
 * Fragment for Rss.Feed. Part of the View layer. Also responsible for click events in the feed.
 */
public class FeedFragment extends Fragment implements FeedMvp.View {
    private static final String TAG = "FeedFragment";

    private StateMaintainer stateMaintainer;
    private FeedRecyclerViewAdapter listAdapter;
    private FeedMvp.Presenter presenter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private View loadingView;
    private View contentView;

    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rss_feed, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);

        listAdapter = new FeedRecyclerViewAdapter();
        listAdapter.setRssFeedItemClickListener(rssFeedItemClickListener);
        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Pull-to-refresh
        swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.content_view);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.load();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loadingView);
        contentView = view.findViewById(R.id.content_view);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupStateMaintainer();
        checkForRetainedState();
    }

    private void setupStateMaintainer() {
        if (stateMaintainer == null) {
            stateMaintainer = new StateMaintainer(getActivity().getFragmentManager(), TAG);
        }
    }

    private void checkForRetainedState() {
        try {
            if (stateMaintainer.isFirstTimeIn()) {
                initialise(this);
            } else {
                reinitialise(this);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialise(FeedMvp.View view) throws InstantiationException, IllegalAccessException{
        presenter = new FeedPresenter(view);
        stateMaintainer.put(FeedMvp.Presenter.class.getSimpleName(), presenter);
        presenter.load();
    }

    private void reinitialise(FeedMvp.View view) throws InstantiationException, IllegalAccessException {
        presenter = stateMaintainer.get(FeedMvp.Presenter.class.getSimpleName());

        if (presenter == null) {
            // If we can't find a presenter assume that its not there and initialise it again.
            initialise(view);
        } else {
            // Otherwise tell it that the configuration has changed
            presenter.onConfigurationChanged(view);
        }
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void updateContent(List<RssFeed.Item> rssItems) {
        listAdapter.updateItems(rssItems);
        listAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showRssItem(String rssItemTitle, String rssItemLink) {
        // Naive implementation using an intent to move between activities
        // TODO: Think about getContext() which is API 23
        Intent intent = new Intent(getActivity(), ItemActivity.class);
        intent.putExtra(ItemActivity.EXTRA_ITEM_TITLE, rssItemTitle);
        intent.putExtra(ItemActivity.EXTRA_ITEM_URL, rssItemLink);
        startActivity(intent);
    }

    /**
     * Listener for clicks on items in the RecyclerView.
     */
    RssFeedItemClickListener rssFeedItemClickListener = new RssFeedItemClickListener() {
        @Override
        public void onRssFeedItemClick(RssFeed.Item rssFeedItem) {
            presenter.onSelectRssItem(rssFeedItem);
        }
    };

    public interface RssFeedItemClickListener {
        void onRssFeedItemClick(RssFeed.Item rssFeedItem);
    }
}