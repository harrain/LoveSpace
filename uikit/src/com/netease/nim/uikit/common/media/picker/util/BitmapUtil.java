package com.netease.nim.uikit.common.media.picker.util;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;


public class BitmapUtil {
	
	public static Bitmap reviewPicRotate(Bitmap bitmap,String path){
		int degree = getPicRotate(path);
		if(degree!=0){
			try{
				Matrix m = new Matrix();  
				int width = bitmap.getWidth();  
				int height = bitmap.getHeight();  
				m.setRotate(degree); 
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,m, true);
			}catch(Exception e){
				e.printStackTrace();
			}catch(Error err){
				err.printStackTrace();
			}
		}
		return bitmap;
	}
	
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int getPicRotate(String path) {
		int degree  = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h)
	{
		if(bitmap == null) {
			return null;
		}

		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		return Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
	}
}
