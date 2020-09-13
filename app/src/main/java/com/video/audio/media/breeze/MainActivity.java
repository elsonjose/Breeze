package com.video.audio.media.breeze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.video.audio.media.breeze.Adapter.VideoAdapter;
import com.video.audio.media.breeze.Model.Video;
import com.video.audio.media.breeze.Util.ScrollListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int READ_WRITE_PERMISSION_REQ_CODE = 100;

    ConstraintLayout mainConstraintLayout;
    RecyclerView videoRecyclerView;

    List<Video> videoList = new ArrayList<Video>();
    VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainConstraintLayout = findViewById(R.id.main_root_layout);
        videoRecyclerView = findViewById(R.id.video_recyclerview);
        videoRecyclerView.setHasFixedSize(true);
        // need to check orientation before applying span count
        int orientation = getResources().getConfiguration().orientation;
        if(orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            videoRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
        else if(orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            videoRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        videoAdapter= new VideoAdapter(this,videoList);
        videoRecyclerView.setAdapter(videoAdapter);

        checkStorageAccessPermission();

        videoRecyclerView.addOnScrollListener(new ScrollListener(){
            @Override
            public void onHide() {
                super.onHide();

                if(getActionBar()!=null)
                {
                    getActionBar().hide();
                }
            }

            @Override
            public void onShow() {
                super.onShow();

                if(getActionBar()!=null)
                {
                    getActionBar().show();
                }
            }
        });

    }

    private void checkStorageAccessPermission() {

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            listVideos();
        }
        else if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},READ_WRITE_PERMISSION_REQ_CODE);

        }
        else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Needed")
                    .setMessage("Breeze need your storage permission to function properly. Enable permission ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},READ_WRITE_PERMISSION_REQ_CODE);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Snackbar.make(mainConstraintLayout,"Permission denied. Enable storage permission and try again", BaseTransientBottomBar.LENGTH_LONG).show();

                        }
                    }).create().show();
        }
        else
        {
            Snackbar.make(mainConstraintLayout,"Permission denied. Enable storage permission and try again", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            videoRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
            videoAdapter.notifyDataSetChanged();

        }
        else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            videoRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
            videoAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case READ_WRITE_PERMISSION_REQ_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                   listVideos();
                }
                else
                {
                    Snackbar.make(mainConstraintLayout,"Permission denied. Enable storage permission and try again", BaseTransientBottomBar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void listVideos() {

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED
        };
        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " ASC";

        try (Cursor cursor = getApplicationContext().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                String dateAdded = cursor.getString(dateAddedColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList.add(new Video(contentUri, name, duration, size, dateAdded));
            }
            videoAdapter.notifyDataSetChanged();

        }


    }
}