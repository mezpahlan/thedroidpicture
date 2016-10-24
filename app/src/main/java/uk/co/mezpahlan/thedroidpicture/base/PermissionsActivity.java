package uk.co.mezpahlan.thedroidpicture.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import uk.co.mezpahlan.thedroidpicture.R;

/**
 * Helper Activity to grant permissions and inform user.
 * https://blog.stylingandroid.com/permissions-part-3/
 */
public class PermissionsActivity extends AppCompatActivity {
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "uk.co.mezpahlan.thedroidpicture.base.permissions.EXTRA_PERMISSIONS";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private PermissionsChecker checker;
    private boolean requiresCheck;

    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }

        checker = new PermissionsChecker(this);
        requiresCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck) {
            String[] permissions = getPermissions();

            if (checker.lacksPermissions(permissions)) {
                requestPermissions(permissions);
            } else {
                allPermissionsGranted();
            }
        } else {
            requiresCheck = true;
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private void requestPermissions(String... permissions) {
        educateInContextPermissionsDialog(permissions);
    }

    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;

            // showRationale = false if user clicks Never Ask Again, otherwise true
            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);

            if (showRationale) {
                showMissingPermissionDialog(permissions[0]);
            } else {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog(String permission) {
        // TODO: key from the input parameter for the case that we have multiple permissions we want to explain
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.permissions_title);
        dialogBuilder.setMessage(R.string.permissions_disable_functionality_warning);
        dialogBuilder.setNegativeButton(R.string.permissions_deny, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSIONS_DENIED);
                finish();
            }
        });
        dialogBuilder.setPositiveButton(R.string.permissions_make_selection_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });
        dialogBuilder.show();
    }

    private void educateInContextPermissionsDialog(final String... permissions) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PermissionsActivity.this);
        dialogBuilder.setTitle(R.string.permissions_title);
        dialogBuilder.setMessage(R.string.permissions_educate_in_context);
        dialogBuilder.setPositiveButton(R.string.permissions_make_selection_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(PermissionsActivity.this, permissions, PERMISSION_REQUEST_CODE);
            }
        });
        dialogBuilder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}