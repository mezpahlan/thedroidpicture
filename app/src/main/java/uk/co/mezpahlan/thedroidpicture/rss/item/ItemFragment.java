package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.base.StateMaintainer;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;
import uk.co.mezpahlan.thedroidpicture.rss.detail.DetailActivity;
import uk.co.mezpahlan.thedroidpicture.rss.feed.FeedMvp;

/**
 * Fragment for RssItem. Part of the MVP View layer.
 */
public class ItemFragment extends Fragment implements ItemMvp.View {

    public static final String ARGUMENT_ITEM_TITLE = "ITEM_TITLE";
    public static final String ARGUMENT_ITEM_URL = "ITEM_URL";
    private static final String TAG = "ItemFragment";

    private StateMaintainer stateMaintainer;
    private ItemRecyclerViewAdapter listAdapter;
    private ItemMvp.Presenter presenter;
    private List<RssItem.Photo> photosList = new ArrayList<>(0);
    private View contentView;
    private View loadingView;
    private ActionMode actionMode;
    private int selectedDetailPosition;
    private View selectedView;
    private String itemUrl;

    public static ItemFragment newInstance(String itemTitle, String itemUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ITEM_TITLE, itemTitle);
        arguments.putString(ARGUMENT_ITEM_URL, itemUrl);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemUrl = getArguments().getString(ARGUMENT_ITEM_URL);
        setTitle(getArguments().getString(ARGUMENT_ITEM_TITLE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rss_item, container, false);

        // Set up recycler view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.content_view);
        listAdapter = new ItemRecyclerViewAdapter(photoClickListener);
        listAdapter.setItemList(photosList);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loadingView);
        contentView = view.findViewById(R.id.content_view);
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

    private void initialise(ItemMvp.View view) throws InstantiationException, IllegalAccessException{
        presenter = new ItemPresenter(view);
        stateMaintainer.put(FeedMvp.Presenter.class.getSimpleName(), presenter);
        presenter.load(itemUrl);
    }

    private void reinitialise(ItemMvp.View view) throws InstantiationException, IllegalAccessException {
        presenter = stateMaintainer.get(ItemMvp.Presenter.class.getSimpleName());

        if (presenter == null) {
            // If we can't find a presenter assume that its not there and initialise it again.
            initialise(view);
        } else {
            // Otherwise tell it that the configuration has changed
            presenter.onConfigurationChanged(view, itemUrl);
        }
    }

    @Override
    public void showLoading(boolean active) {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent() {
        // FIXME: We have a problem here with retained states. Need to do this properly
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
        listAdapter.setItemList(itemPhotos);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void setTitle(String title) {
        // TODO: What's this??
        getActivity().setTitle(title);
    }

    @Override
    public void showExpandedPicture(int position) {
        // Naive implementation using an intent to move between activities
        // TODO: Think about getContext() which is API 23
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_DETAIL_PHOTO_LIST, Parcels.wrap(photosList));
        intent.putExtra(DetailActivity.EXTRA_DETAIL_POSITION, position);
        startActivity(intent);
    }

    // Listener for clicks on items in the RecyclerView.
    PhotoClickListener photoClickListener = new PhotoClickListener() {
        @Override
        public void onItemClick(int position) {
            presenter.onSelectPhoto(position);
        }
    };

    DetailLongClickListener detailLongClickListener = new DetailLongClickListener() {
        @Override
        public void onDetailLongClick(int position, View view) {
            if (actionMode != null) {
                return;
            }

            actionMode = getActivity().startActionMode(actionModeCallback);
            selectedDetailPosition = position;
            selectedView = view;
        }
    };

    ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_share:
                    // TODO: extract URI from here in a method
                    shareImageAndText(selectedDetailPosition, selectedView);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            selectedView = null;
            selectedDetailPosition = -1;
        }
    };

    private void shareImageAndText(int selectedDetailPosition, View selectedView) {
        Bitmap bitmap = getBitmapFromImageView((ImageView) selectedView);
        File tempFile = saveTempImage(bitmap);
        Uri contentUri = FileProvider.getUriForFile(getActivity(), "uk.co.mezpahlan.thedroidpicture.fileprovider", tempFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            RssItem.Photo photo = photosList.get(selectedDetailPosition);
            shareIntent.putExtra(Intent.EXTRA_TEXT, photo.getDescription());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    @Nullable
    private File saveTempImage(Bitmap bmp) {
        File cachePath = new File(getActivity().getCacheDir(), "images");
        // TODO: We don't use the output of this method. Investigate.
        cachePath.mkdirs();

        try {
            // Overwrites this image every time
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bmp.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(cachePath, "image.png");
    }

    private Bitmap getBitmapFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        return  ((BitmapDrawable)drawable).getBitmap();
    }

    public interface PhotoClickListener {
        void onItemClick(int position);
    }

    public interface DetailLongClickListener {
        void onDetailLongClick(int position, View v);
    }
}