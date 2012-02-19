package com.manning.aip.media;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Pair;

import static java.lang.Math.*;

public class BitmapUtil {
	public static Bitmap decodeDownsizedBitmapStream(File file, int target, Context context) throws IOException {
	    FileInputStream stream = new FileInputStream(file);
	    Pair<Integer, Integer> source = getDimensionsForStream(stream);
	    stream.close();
	    FileInputStream in = new FileInputStream(file);
	    Options options = new Options();
	    options.inSampleSize = 1 + getDownSampleSize(max(source.first, source.second), target);
	    return BitmapFactory.decodeStream(in, null, options);
	}
	public static Pair<Integer, Integer> getDimensionsForStream(InputStream in){
	    Options options = new Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(in, null, options);
	    return new Pair<Integer, Integer>(options.outWidth, options.outHeight);
	}
	public static int getDownSampleSize(int source, int target){
	    int size = 1;
	    if (source <= 2*target){
	        int power = (int) ((log (source / target)) / log(2));
	        size = (int) pow(2, power);
	    }
	    return size;
	}

}
