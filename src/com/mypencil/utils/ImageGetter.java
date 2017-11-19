package com.mypencil.utils;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageGetter extends AsyncTask<File, Void, Bitmap> {
    private ImageView iv;
    public ImageGetter(ImageView v) {
        iv = v;
    }
    @Override
    protected Bitmap doInBackground(File... params) {
        return decodeSampledBitmapFromUri(params[0].getAbsolutePath(),200,200);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        iv.setImageBitmap(result);
    }
    
    /**
	 * Decode bigger images to be small enough to fit to the grid item..For scrolling performance
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;   
	}

	/**
	 * calculate sample size based on required width & height
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return proper sample size based on required width & height
	 */
	public int calculateInSampleSize(

			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);    
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);    
			}   
		}

		return inSampleSize;    
	}

	
	private class ViewHolder
	{
		public ImageView imageView;
	}
}