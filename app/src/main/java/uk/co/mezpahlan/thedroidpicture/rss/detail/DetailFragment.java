package uk.co.mezpahlan.thedroidpicture.rss.detail;

import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.mezpahlan.thedroidpicture.R;
import uk.co.mezpahlan.thedroidpicture.base.FileUtils;
import uk.co.mezpahlan.thedroidpicture.base.StateMaintainer;
import uk.co.mezpahlan.thedroidpicture.data.BostonGlobeClient;
import uk.co.mezpahlan.thedroidpicture.data.model.RssItem;
import uk.co.mezpahlan.thedroidpicture.rss.feed.FeedMvp;
import uk.co.mezpahlan.thedroidpicture.rss.item.ItemMvp;

/**
 * Fragment for Rss.Detail. Part of the MVP View layer. Also responsible for FAB click events.
 */
public class DetailFragment extends Fragment implements DetailMvp.View {

    public static final String ARGUMENT_DETAIL_PHOTO_LIST = "DETAIL_PHOTO_LIST";
    public static final String ARGUMENT_DETAIL_POSITION = "DETAIL_POSITION";
    private static final String ARGUMENT_DETAIL_HAS_EXTERNAL_STORAGE_PERMISSION = "DETAIL_HAS_EXTERNAL_STORAGE_PERMISSION";
    private static final String TAG = "DetailFragment";

    private DetailViewPagerAdapter pagerAdapter;

    private View loadingView;
    private ViewPager contentView;
    private View errorView;
    private FabSpeedDial fabSpeedDial;

    private StateMaintainer stateMaintainer;
    private DetailPresenter presenter;
    private List<RssItem.Photo> photosList = new ArrayList<>(0);
    private int startPosition = -1;
    private boolean hasExternalStoragePermission;


    public static DetailFragment newInstance(Parcelable wrappedPhotoList, int startPosition, boolean hasExternalStoragePermission) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGUMENT_DETAIL_PHOTO_LIST, wrappedPhotoList);
        arguments.putInt(ARGUMENT_DETAIL_POSITION, startPosition);
        arguments.putBoolean(ARGUMENT_DETAIL_HAS_EXTERNAL_STORAGE_PERMISSION, hasExternalStoragePermission);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<RssItem.Photo> receivedPhotoList = Parcels.unwrap(getArguments().getParcelable(ARGUMENT_DETAIL_PHOTO_LIST));
        startPosition = getArguments().getInt(ARGUMENT_DETAIL_POSITION);
        hasExternalStoragePermission = getArguments().getBoolean(ARGUMENT_DETAIL_HAS_EXTERNAL_STORAGE_PERMISSION);

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
        fabSpeedDial = (FabSpeedDial) root.findViewById(R.id.fab_view);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                // Handle item selection
                switch (menuItem.getItemId()) {
                    case R.id.action_save:
                        if (hasExternalStoragePermission) {
                            onSelectSavePicture(contentView.getCurrentItem());
                        } else {
                            Toast.makeText(getActivity(), "Missing permissions. Please enable in application settings.", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    case R.id.action_share:
                        onSelectSharePictureAndText(contentView.getCurrentItem());
                        return true;
                    case R.id.action_wallpaper:
                        onSelectSetAsWallpaper(contentView.getCurrentItem());
                        return true;
                    default:
                        return super.onMenuItemSelected(menuItem);
                }
            }
        });

        return root;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingView = view.findViewById(R.id.loadingView);
        errorView = view.findViewById(R.id.error_view);
        fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.fab_view);
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
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.INVISIBLE);
        fabSpeedDial.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        contentView.setVisibility(View.VISIBLE);
        fabSpeedDial.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        errorView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        fabSpeedDial.setVisibility(View.GONE);
    }

    @Override
    public void updateContent(List<RssItem.Photo> itemPhotos) {
        photosList.clear();
        photosList.addAll(itemPhotos);
        pagerAdapter.notifyDataSetChanged();
        contentView.setCurrentItem(startPosition);
    }

    @Override
    public void onSelectSavePicture(int currentPosition) {
        presenter.selectSavePicture(currentPosition);
    }

    @Override
    public void savePicture(int currentPosition) {
        final RssItem.Photo photo = photosList.get(currentPosition);
        final String imageLink = photo.getImageLink();
        final String description = photo.getDescription();
        final FileUtils fileUtils = new FileUtils(getActivity());

        Subscriber<String> savedPictureSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // Log error and show error screen
                showError();
            }

            @Override
            public void onNext(String imagePath) {
                Toast.makeText(getActivity(), "Saved picture: " + imagePath, Toast.LENGTH_SHORT).show();
            }
        };

        Observable.just(fileUtils.getImageFile())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return downloadFile(file, imageLink);
                    }
                })
                .flatMap(new Func1<File, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(File file) {
                        return Observable.just(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                })
                .flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        return Observable.just(insertImageToGallery(bitmap));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(savedPictureSubscriber);
    }

    private String insertImageToGallery(Bitmap file) {
       return MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file, null, null);
    }

    @Override
    public void onSelectSetAsWallpaper(int currentPosition) {
        presenter.selectSetAsWallpaper(currentPosition);
    }

    @Override
    public void setAsWallpaper(int currentPosition) {
        final RssItem.Photo photo = photosList.get(currentPosition);
        final String imageLink = photo.getImageLink();
        final FileUtils fileUtils = new FileUtils(getActivity());

        Subscriber<Bitmap> bitMapSubscriber = new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // Log error and show error screen
                showError();
            }

            @Override
            public void onNext(Bitmap bitmap) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getActivity());
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(getActivity(), "Set wallpaper successfully!", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Observable.just(fileUtils.getTempImageFile())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return downloadFile(file, imageLink);
                    }
                })
                .flatMap(new Func1<File, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(File file) {
                        return Observable.just(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitMapSubscriber);
    }

    @Override
    public void onSelectSharePictureAndText(int currentPosition) {
        presenter.selectSharePictureAndText(currentPosition);
    }

    @Override
    public void sharePictureAndText(final int currentPosition) {
        final RssItem.Photo photo = photosList.get(currentPosition);
        final String imageLink = photo.getImageLink();
        final String description = photo.getDescription();
        final FileUtils fileUtils = new FileUtils(getActivity());

        Subscriber<Uri> uriSubscriber = new Subscriber<Uri>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                // Log error and show error screen
                showError();
            }

            @Override
            public void onNext(Uri uri) {
                sharePictureAndTextIntent(uri, description);
            }
        };

        Observable.just(fileUtils.getTempImageFile())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return downloadFile(file, imageLink);
                    }
                })
                .flatMap(new Func1<File, Observable<Uri>>() {
                    @Override
                    public Observable<Uri> call(File file) {
                        return getUriFromFile(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uriSubscriber);
    }

    private Observable<File> downloadFile(final File file, String imageLink) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.bostonglobe.com")
                .client(new OkHttpClient.Builder().build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        BostonGlobeClient bostonGlobeClient = retrofit.create(BostonGlobeClient.class);


        return bostonGlobeClient.downloadFile(imageLink)
                .flatMap(new Func1<Response<ResponseBody>, Observable<File>>() {
                    @Override
                    public Observable<File> call(final Response<ResponseBody> responseBodyResponse) {
                        return Observable.create(new Observable.OnSubscribe<File>() {
                            @Override
                            public void call(Subscriber<? super File> subscriber) {
                                try {
                                    BufferedSink sink = Okio.buffer(Okio.sink(file));

                                    sink.writeAll(responseBodyResponse.body().source());
                                    sink.close();
                                    subscriber.onNext(file);
                                    subscriber.onCompleted();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    subscriber.onError(e);
                                }
                            }
                        });
                    }
                });
    }

    private Observable<Uri> getUriFromFile(File file) {
        final FileUtils fileUtils = new FileUtils(getActivity());
        return Observable.just(fileUtils.getUriForFile(file));
    }

    private void sharePictureAndTextIntent(Uri contentUri, String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Choose an app"));
    }
}