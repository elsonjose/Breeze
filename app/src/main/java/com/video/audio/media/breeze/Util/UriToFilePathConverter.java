package com.video.audio.media.breeze.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

public class UriToFilePathConverter {

    Context context;

    public UriToFilePathConverter(Context context) {
        this.context = context;
    }

    public String getFilePath(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    result = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return result;
    }
}
