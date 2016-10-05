package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.base.StateMaintainer;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;
import uk.co.mezpahlan.thedroidpicture.rss.feed.FeedMvp;
import uk.co.mezpahlan.thedroidpicture.rss.item.ItemMvp;

/**
 * Fragment for RssItemDetail. Part of the MVP View layer. Also responsible for FAB click events.
 */
public class DetailFragment extends Fragment implements DetailMvp.View {

    public static final String ARGUMENT_DETAIL_PHOTO_LIST = "DETAIL_PHOTO_LIST";
    public static final String ARGUMENT_DETAIL_POSITION = "DETAIL_POSITION";
    private static final String TAG = "DetailFragment";

    private DetailViewPagerAdapter pagerAdapter;
    private ViewPager contentView;
    private View loadingView;
    private View fab;
    private StateMaintainer stateMaintainer;
    private DetailPresenter presenter;
    private List<RssItem.Photo> photosList = new ArrayList<>(0);
    private int startPosition = -1;


    public static DetailFragment newInstance(Parcelable wrappedPhotoList, int startPosition) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_DETAIL_PHOTO_LIST, wrappedPhotoList);
        arguments.putInt(ARGUMENT_DETAIL_POSITION, startPosition);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<RssItem.Photo> receivedPhotoList = Parcels.unwrap(getArguments().getParcelable(ARGUMENT_DETAIL_PHOTO_LIST));
        startPosition = getArguments().getInt(ARGUMENT_DETAIL_POSITION);

        photosList.addAll(receivedPhotoList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rss_detail, container, false);

        // Set up the loading view
        loadingView = root.findViewById(R.id.loadingView);

        // Set up view pager
        contentView = (ViewPager) root.findViewById(R.id.content_view);
        pagerAdapter = new DetailViewPagerAdapter(photosList);
        contentView.setAdapter(pagerAdapter);
        contentView.setCurrentItem(startPosition);

        // Set up the FAB
        fab = root.findViewById(R.id.fab_view);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectSharePictureAndText(contentView.getCurrentItem());
            }
        });

        return root;
    }

    @Override
    public void onStart(){
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

    private void initialise(DetailMvp.View view) throws InstantiationException, IllegalAccessException{
        presenter = new DetailPresenter(view);
        stateMaintainer.put(FeedMvp.Presenter.class.getSimpleName(), presenter);
        presenter.load();
    }

    private void reinitialise(DetailMvp.View view) throws InstantiationException, IllegalAccessException {
        presenter = stateMaintainer.get(ItemMvp.Presenter.class.getSimpleName());

        if (presenter == null) {
            // If we can't find a presenter assume that its not there and initialise it again.
            initialise(view);
        } else {
            // Otherwise tell it that the configuration has changed
            presenter.onConfigurationChanged(view);
        }
    }

    @Override
    public void showLoading(boolean active) {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void updateContent(List<RssItem.Photo> itemPhotos) {
        photosList.clear();
        photosList.addAll(itemPhotos);
        pagerAdapter.notifyDataSetChanged();
        contentView.setCurrentItem(startPosition);
    }

    @Override
    public void onSelectSharePicture(int currentPosition) {
        Toast.makeText(getActivity(), "Selected to share only the picture at postition: " + currentPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectSharePictureAndText(int currentPosition) {
        Toast.makeText(getActivity(), "Selected to share both the picture and the text at postition: " + currentPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectSetAsWallpaper(int currentPosition) {
        Toast.makeText(getActivity(), "Selected to set as wallpaper at postition: " + currentPosition, Toast.LENGTH_SHORT).show();
    }
}
