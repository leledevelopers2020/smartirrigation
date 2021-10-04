package com.leledevelopers.smartirrigation.services.impl;

import android.content.Context;

import com.leledevelopers.smartirrigation.services.CURD_Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CURD_FilesImpl implements CURD_Files {

    @Override
    public void createEmptyFile(Context context, String directory, String file_name) throws IOException {
        File file = new File(context.getExternalFilesDir(directory), file_name);
        FileOutputStream fos = null;
        fos = new FileOutputStream(file);
        String data = "";
        fos.write(data.getBytes());
    }

    @Override
    public boolean isFileExists(Context context, String directory, String file_name) {
        return new File(context.getExternalFilesDir(directory), file_name).exists();
    }
}
