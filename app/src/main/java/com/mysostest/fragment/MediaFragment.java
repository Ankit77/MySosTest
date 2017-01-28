package com.mysostest.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mysostest.R;
import com.mysostest.util.Constant;
import com.mysostest.util.GetFilePath;
import com.mysostest.util.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by indianic on 28/01/17.
 */

public class MediaFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Button btnCapturePhoto;
    private Button btnSelectPhoto;
    private Button btnCaptureVideo;
    private Button btnSelectVideo;
    //files
    private final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final int REQUEST_WRITE_ACCESS = 1000;

    private final int REQUEST_CAPTURE_IMAGE = 111;
    private final int REQUEST_CAPTURE_VIDEO = 222;
    private final int REQUEST_PICK_IMAGE = 333;
    private final int REQUEST_PICK_VIDEO = 444;
    private final int REQUEST_PICK_FILE = 555;
    private final int REQUEST_CATEGORY_CODE = 666;
    private final int REQUEST_TAG_CODE = 777;

    protected String filePath = null;
    protected String cameraFilePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_media, null);
        init();
        return view;


    }

    private void init() {
        btnCapturePhoto = (Button) view.findViewById(R.id.btn_capturePhoto);
        btnSelectPhoto = (Button) view.findViewById(R.id.btn_selectphoto);
        btnCaptureVideo = (Button) view.findViewById(R.id.btn_capturevideo);
        btnSelectVideo = (Button) view.findViewById(R.id.btn_selectvideo);
        btnCapturePhoto.setOnClickListener(this);
    }

    private void chooseImageFileFromStorage() {
        Intent intent = Utils.getFileChooserIntent("image/*");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_PICK_IMAGE);
        }

    }

    private void chooseVideoFileFromStorage() {
        Intent intent = Utils.getFileChooserIntent("video/*");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_PICK_VIDEO);
        }
    }

    /**
     * Opens camera activity
     */

    private void captureImage() {
        Intent intent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {

            Uri outputFileUri = getPostImageUri(true);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CAPTURE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureVideo() {
        Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        try {

            Uri outputFileUri = getPostVideoUri(true);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CAPTURE_VIDEO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == Activity.RESULT_OK) {
                // Callback from Gallery
                if (requestCode == REQUEST_CAPTURE_IMAGE) {
                    try {
                        if (!TextUtils.isEmpty(cameraFilePath)) {
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            if (!TextUtils.isEmpty(filePath)) {
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == REQUEST_CAPTURE_VIDEO) {
                    try {
                        if (!TextUtils.isEmpty(cameraFilePath)) {
                            final Uri uri = Uri.fromFile(new File(cameraFilePath));
                            filePath = GetFilePath.getPath(getActivity(), uri);
                            if (!TextUtils.isEmpty(filePath)) {
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    final Uri selectedImageUri = data.getData();
                    try {
                        filePath = GetFilePath.getPath(getActivity(), selectedImageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (requestCode == REQUEST_PICK_FILE) {
                        if (!TextUtils.isEmpty(filePath)) {
                        }
                    } else if (requestCode == REQUEST_PICK_IMAGE) {
                        if (!TextUtils.isEmpty(filePath)) {
                        }
                    } else if (requestCode == REQUEST_PICK_VIDEO) {
                        if (!TextUtils.isEmpty(filePath)) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets Uri of image to capture. An External Cache Directory is created and
     * a file for current_post is created in it, this method Uri for it.
     *
     * @param canCleanup : Can delete the file if already exists
     * @return : Generated Uri.
     */
    private Uri getPostImageUri(boolean canCleanup) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + System.currentTimeMillis() + Constant.IMAGE_EXTENSION);
        if (canCleanup) {
            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cameraFilePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    /**
     * Gets Uri of video to capture. An External Cache Directory is created and
     * a file for current_post is created in it, this method Uri for it.
     *
     * @param canCleanup : Can delete the file if already exists
     * @return : Generated Uri.
     */
    private Uri getPostVideoUri(boolean canCleanup) {
        final File file = new File(getActivity().getExternalCacheDir() + File.separator + System.currentTimeMillis() + Constant.VIDEO_EXTENSION);
        if (canCleanup) {
            if (file.exists()) {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cameraFilePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    //Permission check

    /**
     * check write access
     */
    public boolean isWritePermission() {
        // Check if the write permission is already available.
        if (ContextCompat.checkSelfPermission(getActivity(), WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestWritePermission();
            return false;
        }

        return true;
    }

    /**
     * Requests the write permission.
     * If the permission has been denied previously, a alert will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestWritePermission() {
        // permission has not been granted yet. Request it directly.
        FragmentCompat.requestPermissions(this, new String[]{WRITE_PERMISSION}, REQUEST_WRITE_ACCESS);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_WRITE_ACCESS) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for write permission.
            Log.i("Permission", "Received response for write permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // write permission has been granted
                Log.i("Permission", "permission has now been granted. Showing preview.");
                captureImage();
            } else {
                Log.i("Permission", "permission was NOT granted.");
                Toast.makeText(getActivity(), "msg_write_permission_needed", Toast.LENGTH_LONG).show();
            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnCapturePhoto) {
            if (isWritePermission()) {
                captureVideo();
            }
        }
    }
}
