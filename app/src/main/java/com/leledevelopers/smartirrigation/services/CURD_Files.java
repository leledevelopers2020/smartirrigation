package com.leledevelopers.smartirrigation.services;

import android.content.Context;

import java.io.IOException;

public interface CURD_Files {
    public void createEmptyFile(Context context,String directory, String file_name) throws IOException;
    public boolean isFileExists(Context context,String directory, String file_name);
}
