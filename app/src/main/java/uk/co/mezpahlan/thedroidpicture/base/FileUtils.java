package uk.co.mezpahlan.thedroidpicture.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utilities for working with Files.
 */
public class FileUtils {

    public static final String PACKAGE = "uk.co.mezpahlan.thedroidpicture.fileprovider";

    private final Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    public File getTempImageFile() {
        final File tempImageDirectory = getTempDirectory("images");
        for (File file : tempImageDirectory.listFiles()) {
            file.delete();
        }
        return getFile(tempImageDirectory, System.currentTimeMillis() + ".png");
    }

    public File getImageFile() {
        return getImageFile(String.valueOf(System.currentTimeMillis()));
    }

    public File getImageFile(String filename) {
        final File imageDirectory = getImageDirectory();
        return getFile(imageDirectory, filename + ".png");
    }

    public File getImageDirectory() {
        File imagesDirectory = new File(context.getFilesDir(), "images");

        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs();
        }

        return imagesDirectory;
    }

    public File getTempDirectory(String type) {
        File tempDirectory = new File(context.getCacheDir(), type);

        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        return tempDirectory;
    }

    public File getFile(String filename) {
        return new File(filename);
    }

    public File getFile(File directory, String filename) {
        return new File(directory, filename);
    }

    public Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(context, PACKAGE, file);
    }

    public void saveImage(Bitmap bmp, File directoryPath, String filename) throws IOException {
        // Create the storage directory if it does not exist
        if (!directoryPath.exists() && !directoryPath.mkdirs()) {
            throw new IOException();
        }

        FileOutputStream stream = new FileOutputStream(directoryPath + File.separator + filename);
        bmp.compress(Bitmap.CompressFormat.PNG, 90, stream);
        stream.close();
    }
}
