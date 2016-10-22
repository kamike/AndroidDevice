package com.wangtao.universallylibs.utils;

import android.content.Context;
import android.os.Environment;

import com.wangtao.universallylibs.ConfigProperties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/10/22.
 */

public class LogSaveUtils {
    private final Context context;
    private String filePath;

    public LogSaveUtils(String fileName, Context context) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f=new File(dir+"/"+fileName);
        if(!f.exists()){
            try {
                f.createNewFile();
                filePath=f.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.context = context;
    }
    public void writeString(Object str){
        try {
            FileOutputStream out=new FileOutputStream(filePath,true);
            out.write("\n".getBytes());
            out.write((str+"").getBytes(ConfigProperties.CHAR_SET_NAME));
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
