package com.tasmanian.properties.imageloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileCache {

	private File cacheDir;
	private static String PATH = Environment.getExternalStorageDirectory()
			.toString();
	public static final String CACHEPATH = PATH + "/Property/cache/images/";

	public FileCache(Context context, boolean save) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			/*
			 * if(save) cacheDir = new File(Constants.CACHEIMAGE); else
			 */
			cacheDir = new File(CACHEPATH);
		} else
			cacheDir = context.getCacheDir();

		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution 
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}