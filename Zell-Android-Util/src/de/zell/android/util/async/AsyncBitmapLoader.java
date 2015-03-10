/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.android.util.async;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import de.zell.android.util.R;
import java.lang.ref.WeakReference;

/**
 * Represents a AsyncBitmapLoader which loads bitmaps asynchronously into a
 * given image view.
 *
 * Influenced by the Google Android Tutorial for "Processing Bitmaps Off the UI
 * Thread".
 * http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public class AsyncBitmapLoader extends AsyncProgressTask<Integer, Void, Bitmap> {

  /**
   * The weak reference of the image view which should get the loaded bitmap.
   */
  private final WeakReference<ImageView> imageViewReference;

  /**
   * The image drawable resource identifier.
   */
  public int imgRes = 0;
  /**
   * The application context to get access to the resources.
   */
  private final Context ctx;

  /**
   * The ctor to create a asynchronous bitmap loader object.
   *
   * @param imageView the image view which should get the loaded bitmap
   * @param ctx the application context
   */
  public AsyncBitmapLoader(ImageView imageView, Context ctx) {
    this.imageViewReference = new WeakReference<ImageView>(imageView);
    this.ctx = ctx;
  }

  @Override
  protected Bitmap doInBackground(Integer... ids) {
    if (ids.length > 0) {
      imgRes = ids[0];
      return createBitmap();
    } else {
      return null;
    }
  }

  // Once complete, see if ImageView is still around and set bitmap.
  @Override
  protected void onPostExecute(Bitmap bitmap) {
    if (isCancelled()) {
      bitmap = null;
    }

    if (imageViewReference != null && bitmap != null) {
      final ImageView imageView = imageViewReference.get();
      final AsyncBitmapLoader bitmapWorkerTask
              = getAsyncBitmapLoader(imageView);
      if (this == bitmapWorkerTask && imageView != null) {
        imageView.setImageBitmap(bitmap);
      }
    }
    super.onPostExecute(bitmap);
  }

  /**
   * Cancels the asynchronous bitmap loader for the given image view. Before
   * canceling the data of the task is compared with the given data if they are
   * equal then the task are not canceled.
   *
   * @param data the data of the asynchronous bitmap loader (img res)
   * @param imageView the image view which contains the loader
   * @return true if the task was canceled, false otherwise
   */
  public static boolean cancelPotentialWork(int data, ImageView imageView) {
    final AsyncBitmapLoader bitmapWorkerTask = getAsyncBitmapLoader(imageView);

    if (bitmapWorkerTask != null) {
      final int bitmapData = bitmapWorkerTask.imgRes;
      // If bitmapData is not yet set or it differs from the new data
      if (bitmapData == 0 || bitmapData != data) {
        // Cancel previous task
        bitmapWorkerTask.cancel(true);
      } else {
        // The same work is already in progress
        return false;
      }
    }
    // No task associated with the ImageView, or an existing task was cancelled
    return true;
  }

  /**
   * Returns for the given image view the corresponding asynchronous bitmap
   * loader.
   *
   * @param imageView the image view which contains the async bitmap loader
   * @return the async bitmap loader
   */
  private static AsyncBitmapLoader getAsyncBitmapLoader(ImageView imageView) {
    if (imageView != null) {
      final Drawable drawable = imageView.getDrawable();
      if (drawable instanceof AsyncDrawable) {
        final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
        return asyncDrawable.getAsyncBitmapLoader();
      }
    }
    return null;
  }

  /**
   * Represents a bitmap drawable which contains his own asynchronous bitmap
   * loader.
   *
   * @author Christopher Zell <zelldon91@googlemail.com>
   */
  public static class AsyncDrawable extends BitmapDrawable {

    /**
     * The weak reference of his own asynchronous bitmap loader.
     */
    private final WeakReference<AsyncBitmapLoader> asyncBitmapLoader;

    /**
     * The ctor to create an AsyncDrawable object.
     *
     * @param res the application resources
     * @param bitmap the bitmap for the async drawable
     * @param bitmapLoader the bitmap loader which is used for the drawable
     */
    public AsyncDrawable(Resources res, Bitmap bitmap,
            AsyncBitmapLoader bitmapLoader) {
      super(res, bitmap);
      asyncBitmapLoader
              = new WeakReference<AsyncBitmapLoader>(bitmapLoader);
    }

    /**
     * Returns the corresponding async bitmap loader if its exists if not null
     * will be returned.
     *
     * @return the async bitmap loader or null
     */
    public AsyncBitmapLoader getAsyncBitmapLoader() {
      if (asyncBitmapLoader != null) {
        return asyncBitmapLoader.get();
      } else {
        return null;
      }
    }
  }

  /**
   * Creates from the drawable resource id a bitmap object.
   *
   * @return the bitmap object
   */
  private Bitmap createBitmap() {
    if (imgRes != 0) {
      BitmapFactory.Options o = new BitmapFactory.Options();
      o.inJustDecodeBounds = true;
      BitmapFactory.decodeResource(ctx.getResources(), imgRes, o);
      if (imageViewReference != null) {
        ImageView img = imageViewReference.get();
        if (img != null) {
          o.inJustDecodeBounds = false;
          o.inDensity = o.inTargetDensity;
          Bitmap b = BitmapFactory.decodeResource(ctx.getResources(), imgRes, o);
          Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                  getBitmapConfigurationMatrix(img, b), true);
          if (b != b2) {
            b.recycle();
            b = null;
          }
          logAllocationBytes(b2);
          return b2;
        }
      }
    }
    return null;
  }

  /**
   * Method to log the allocated bytes by the bitmap.
   * 
   * @param b the bitmap which was created
   */
  private void logAllocationBytes(Bitmap b) {
    int bytes;
    if (Build.VERSION.SDK_INT >= 19) {
      bytes = b.getAllocationByteCount();
    } else {
      bytes = b.getByteCount();
    }
    Log.d(AsyncBitmapLoader.class.getName(),
            String.format(ctx.getString(R.string.log_bitmap_alloc), bytes));
  }

  /**
   * Creates for the given image view a matrix which resize and rotates the
   * corresponding bitmap if necessary, to save memory.
   *
   * Checks the dimensions of the bitmap if the width is larger than the height
   * the bitmap should be rotated. Scales the bitmap to save memory and to show
   * the hole image in the image view.
   *
   * @param imageView the image view which contains the bitmap
   * @param b the bitmap which should be shown
   * @return the matrix to configure the bitmap
   */
  private Matrix getBitmapConfigurationMatrix(ImageView imageView, Bitmap b) {
    Matrix m = new Matrix();
    int imgWidth = b.getWidth();
    int imgHeight = b.getHeight();
    if (imgWidth > imgHeight) {
      m.postRotate(270);
      //SWAP values
      //the img will be rotated so the height is the new width
      int temp = imgWidth;
      imgWidth = imgHeight;
      imgHeight = temp;
    }

    int viewWidth = imageView.getWidth();
    int viewHeight = imageView.getHeight();

    float scaleX = viewWidth / (float) imgWidth;
    float scaleY = viewHeight / (float) imgHeight;
    m.postScale(scaleX == 0 ? 1 : scaleX,
            scaleY == 0 ? 1 : scaleY);

    return m;
  }
}
