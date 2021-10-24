package com.leledevelopers.smartirrigation.services.impl;

import android.content.Context;

import com.leledevelopers.smartirrigation.Screen_5;
import com.leledevelopers.smartirrigation.services.CURD_Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CURD_FilesImpl<T> implements CURD_Files {

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

    @Override
    public boolean isFileHasData(Context context,String file_name) {
        File file = new File(context.getExternalFilesDir(null), file_name);
        System.out.println("Data file length " + file.length());
        return ((file.length() != 0) ? true : false);
    }

    @Override
    public void createFile(Context context, String file_name, Object data) throws  IOException {
        File file = new File(context.getExternalFilesDir(null), file_name);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(data);
        out.close();
        fos.close();
    }

    @Override
    public void updateFile(Context context, String file_name, Object data) throws IOException {
        File file = new File(context.getExternalFilesDir(null), file_name);
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(data);
        out.close();
        fos.close();
    }

    @Override
    public Object getFile(Context context, String file_name) throws IOException, ClassNotFoundException {
        File file = new File(context.getExternalFilesDir(null), file_name);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        Object object = in.readObject();
        return object;
    }

    @Override
    public List getFileData(Context context, String file_name) throws IOException, ClassNotFoundException {
        File file = new File(context.getExternalFilesDir(null), file_name);
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fis);
        List<T> tList = (List<T>) in.readObject();
        return tList;
    }

    @Override
    public void updateFileData(Context context, String file_name, List data) throws IOException {
        this.createFile(context,file_name,data);
    }


}