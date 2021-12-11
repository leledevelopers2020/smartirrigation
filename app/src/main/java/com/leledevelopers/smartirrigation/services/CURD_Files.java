package com.leledevelopers.smartirrigation.services;

import android.content.Context;

import java.io.IOException;
import java.util.List;

public interface CURD_Files<T> {
    public void createEmptyFile(Context context, String directory, String file_name) throws IOException;

    public boolean isFileExists(Context context, String directory, String file_name);

    public boolean isFileHasData(Context context, String file_name);

    public void createFile(Context context, String file_name, Object data) throws IOException;

    public void updateFile(Context context, String file_name, Object data) throws IOException;

    public Object getFile(Context context, String file_name) throws IOException, ClassNotFoundException;

    List<T> getFileData(Context context, String file_name) throws IOException, ClassNotFoundException;

    public void updateFileData(Context context, String file_name, List data) throws IOException;
}