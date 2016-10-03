package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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

import java.util.ArrayList;
import java.util.List;

import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;

/**
 * Created by mpahlan on 27/07/16.
 */
public class ItemFragment extends Fragment implements ItemMvp.View {

    public static final String ARGUMENT_ITEM_TITLE = "ITEM_TITLE";
    public static final String ARGUMENT_ITEM_URL = "ITEM_URL";
    private ItemRecyclerViewAdapter listAdapter;
    private ItemViewPagerAdapter pagerAdapter;
    private ItemMvp.Presenter presenter;
    private List<RssItem.Photo> photosList = new ArrayList<>(0);
    private ViewPager viewPager;
    private View contentView;
    private View loadingView;
    private ActionMode actionMode;
    private int selectedDetailPosition;
    private View selectedView;
    private Uri sharedUri;

    public static ItemFragment newInstance(String itemTitle, String itemUrl) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_ITEM_TITLE, itemTitle);
        arguments.putString(ARGUMENT_ITEM_URL, itemUrl);

        ItemFragment fragment = new ItemFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listAdapter = new ItemRecyclerViewAdapter(photosList, photoClickListener);
        pagerAdapter = new ItemViewPagerAdapter(photosList, detailLongClickListener);
    }

    @Override
    public void onResume(){
        super.onResume();
        String itemUrl = getArguments().getString(ARGUMENT_ITEM_URL);
        presenter.load(itemUrl);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        presenter = new ItemPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rss_item, container, false);

        // Set up recycler view
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.content_view);
        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // Set up view pager
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);

        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loadingView);
        contentView = view.findViewById(R.id.content_view);
    }


    @Override
    public void showLoading(boolean active) {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showContent(List<RssItem.Photo> itemPhotos) {
        setTitle(getArguments().getString(ARGUMENT_ITEM_TITLE));
        // TODO: Double check this is sensible to do. We are throwing away old data and replacing
        // TODO: it with new data. It just so happen that we are confident they match and are the
        // TODO: same. But perhaps we should avoid refetching the data on configuration change??
        // FIXME: We have a problem here with retained states. Need to do this properly
        photosList.clear();
        photosList.addAll(itemPhotos);
        listAdapter.notifyDataSetChanged();
        pagerAdapter.notifyDataSetChanged();
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {

    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void showExpandedPicture(int position) {
        viewPager.setCurrentItem(position, false);
        viewPager.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
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
                    shareLink(selectedDetailPosition, selectedView);
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

    private void shareLink(int selectedDetailPosition, View selectedView) {
        uriFromImageView((ImageView) selectedView);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        RssItem.Photo photo = photosList.get(selectedDetailPosition);
        shareIntent.putExtra(Intent.EXTRA_TEXT, photo.getDescription());
        shareIntent.putExtra(Intent.EXTRA_STREAM, sharedUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(shareIntent, "Share via"), 1337);
    }

    private void uriFromImageView(ImageView selectedView) {
        Drawable drawable = selectedView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                bitmap, "Image Description", null);

        sharedUri = Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        // Delete file
        if (requestCode == 1337) {
            deleteAfterShareIntent();
        }
    }

    private void deleteAfterShareIntent() {
        if (sharedUri != null) {
            getActivity().getContentResolver().delete(sharedUri, null, null);
            sharedUri = null;
        }
    }

    public interface PhotoClickListener {
        void onItemClick(int position);
    }

    public interface DetailLongClickListener {
        void onDetailLongClick(int position, View v);
    }
}