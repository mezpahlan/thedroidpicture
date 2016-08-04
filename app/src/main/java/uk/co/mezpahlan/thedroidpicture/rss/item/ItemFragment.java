package uk.co.mezpahlan.thedroidpicture.rss.item;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private ItemMvp.Presenter presenter;
    private List<RssItem.Photo> photosList = new ArrayList<>(0);

    public ItemFragment() {
        // Required empty constructor
    }

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

        listAdapter = new ItemRecyclerViewAdapter(photosList);
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
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(listAdapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        return root;
    }

    @Override
    public void showLoading(boolean active) {
        // TODO: Show a loading icon?
    }

    @Override
    public void showContent(List<RssItem.Photo> itemPhotos) {
        setTitle(getArguments().getString(ARGUMENT_ITEM_TITLE));
        photosList.addAll(itemPhotos);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    @Override
    public void showExpandedPicture() {

    }

}
