package com.dynamsoft.tessocr;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class TessOCR {
	private TessBaseAPI mTess;
	
	public TessOCR(Context context) {
		// TODO Auto-generated constructor stub
		mTess = new TessBaseAPI();
		String datapath = context.getFilesDir() + "/tesseract/";
		String language = "eng";
		File dir = new File(datapath + "tessdata");
		File file = new File(datapath + "tessdata/eng.cube.lm");
		if (!file.exists()) {
			dir.mkdirs();
			AssetManager assetManager = context.getAssets();
			String[] files = null;
			try {
				files = assetManager.list("tessdata");
			} catch (IOException e) {
				Log.e("tag", "Failed to get asset file list.", e);
			}

			for(String filename : files) {

				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open("tessdata/"+filename);
					File outFile = new File(datapath+"tessdata/", filename);
					out = new FileOutputStream(outFile);
					copyFile(in, out);
				} catch (IOException e) {
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// NOOP
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// NOOP
						}
					}
				}

			}

		}

		mTess.init(datapath, language);
	}
	
	public String getOCRResult(Bitmap bitmap) {
		
		mTess.setImage(bitmap);
		String result = mTess.getUTF8Text();

		return result;
    }
	
	public void onDestroy() {
		if (mTess != null)
			mTess.end();
	}

	public void copyFileOrDirectory(String srcDir, String dstDir) {

		try {
			File src = new File(srcDir);
			File dst = new File(dstDir, src.getName());

			if (src.isDirectory()) {

				String files[] = src.list();
				int filesLength = files.length;
				for (int i = 0; i < filesLength; i++) {
					String src1 = (new File(src, files[i]).getPath());
					String dst1 = dst.getPath();
					copyFileOrDirectory(src1, dst1);

				}
			} else {
				copyFile(src, dst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.getParentFile().exists())
			destFile.getParentFile().mkdirs();

		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}
