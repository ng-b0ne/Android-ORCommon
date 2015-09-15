package me.b0ne.android.orcommon;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by b0ne on 14/01/08.
 */
public class ImageUtils {
    public static final int IMAGE_OPTIMIZE_MAX_SIZE_WIDTH = 2000;
    public static final int IMAGE_OPTIMIZE_MAX_SIZE_HEIGHT = 2000;

    /**
     * 画像の最適化、サイズ調整
     * @param cr
     * @param imageUri
     * @param maxWidth
     * @return
     */
    public static Bitmap optimizeBitmap(ContentResolver cr, Uri imageUri, int maxWidth, int maxHeight) {
        Bitmap bitmap = null;
        HashMap<String, String> imgObj = getImageData(cr, imageUri);
        String imgPath = imgObj.get(MediaStore.Images.Media.DATA);

        try {
            InputStream inputStream = cr.openInputStream(imageUri);

            // 画像サイズ情報を取得する
            BitmapFactory.Options imageOptions = new BitmapFactory.Options();
            imageOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, imageOptions);

            inputStream.close();

            // もし、画像が大きかったら縮小して読み込む
            // imageScaleSizeMaxの大きさに合わせる
            int imageScaleSizeMax = 500;
            inputStream = cr.openInputStream(imageUri);
            float imageScaleWidth = (float)imageOptions.outWidth / imageScaleSizeMax;
            float imageScaleHeight = (float)imageOptions.outHeight / imageScaleSizeMax;

            // もしも、縮小できるサイズならば、縮小して読み込む
            if (imageScaleWidth > 2 && imageScaleHeight > 2) {
                BitmapFactory.Options imageOptions2 = new BitmapFactory.Options();

                // 縦横、小さい方に縮小するスケールを合わせる
                int imageScale = (int)Math.floor((imageScaleWidth > imageScaleHeight ? imageScaleHeight : imageScaleWidth));

                // inSampleSizeには2のべき上が入るべきなので、imageScaleに最も近く、かつそれ以下の2のべき上の数を探す
                for (int i = 2; i < imageScale; i *= 2) {
                    imageOptions2.inSampleSize = i;
                }

                bitmap = BitmapFactory.decodeStream(inputStream, null, imageOptions2);
            } else {
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            inputStream.close();

            // 写真の向きの調整
            Matrix matrix = new Matrix();

            ExifInterface exif = new ExifInterface(imgPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                    matrix.postScale(-1, 1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postScale(-1, -1);
                    break;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    matrix.postScale(1, -1);
                    break;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    matrix.postScale(1, -1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    matrix.postRotate(270);
                    matrix.postScale(1, -1);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            float _imgWidth = bitmap.getWidth();
            float _imgHeight = bitmap.getHeight();

            // 最大幅が指定されているとき
            if (maxWidth != 0) {
                // 最大幅より小さかったらそのまま返す。 大きかったら最大幅までリサイズする。
                if (_imgWidth <= maxWidth) {
                    return bitmap;
                } else { // 大きかったら最大幅までリサイズする。
                    float _newWidth = maxWidth;
                    float ratioSize = _imgWidth / _imgHeight;
                    float _newHeight = _newWidth / ratioSize;
                    int newWidth = maxWidth;
                    int newHeight = Integer.valueOf(String.valueOf(Math.round(_newHeight)));

                    bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                }
            } else if (maxHeight != 0) { // 最大高さが指定されているとき
                if (_imgHeight <= maxHeight) {
                    return bitmap;
                } else {
                    float _newHeight = maxHeight;
                    float ratioSize = _imgHeight / _imgWidth;
                    float _newWidth = _newHeight / ratioSize;
                    int newHeight = maxHeight;
                    int newWidth = Integer.valueOf(String.valueOf(Math.round(_newWidth)));

                    bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
                }
            }

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return bitmap;
    }

    /**
     * 画像の最適化、サイズ調整
     * @param cr
     * @param imageUri
     * @return
     */
    public static Bitmap optimizeBitmap(ContentResolver cr, Uri imageUri) {
        return optimizeBitmap(cr, imageUri,
                IMAGE_OPTIMIZE_MAX_SIZE_WIDTH, IMAGE_OPTIMIZE_MAX_SIZE_HEIGHT);
    }

    public static Bitmap optimizeBitmap(ContentResolver cr, Uri imageUri, int maxWidth) {
        return optimizeBitmap(cr, imageUri, maxWidth, 0);
    }

    /**
     * 画像の最適化、サイズ調整
     * @param cr
     * @param imageUri
     * @return
     */
    public static InputStream optimizeBitmapToInputStream(ContentResolver cr, Uri imageUri) {
        return optimizeBitmapToInputStream(cr, imageUri,
                IMAGE_OPTIMIZE_MAX_SIZE_WIDTH, IMAGE_OPTIMIZE_MAX_SIZE_HEIGHT);
    }

    public static InputStream optimizeBitmapToInputStream(ContentResolver cr, Uri imageUri,
                                                          int maxWidth) {
        return optimizeBitmapToInputStream(cr, imageUri, maxWidth, 0);
    }

    /**
     * 画像の最適化、サイズ調整
     * @param cr
     * @param imageUri
     * @param maxWidth
     * @return
     */
    public static InputStream optimizeBitmapToInputStream(ContentResolver cr, Uri imageUri,
                                                          int maxWidth, int maxHeight) {
        InputStream inputStreamResult = null;
        try {
            Bitmap bitmap = optimizeBitmap(cr, imageUri, maxWidth, maxHeight);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 97, os);
            bitmap.recycle();
            inputStreamResult = new ByteArrayInputStream(os.toByteArray());
            os.close();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        return inputStreamResult;
    }

    /**
     * 画像のメタデータを取得する
     * @param cr
     * @param imageUri
     * @return
     */
    public static HashMap<String, String> getImageData(ContentResolver cr, Uri imageUri) {
        HashMap<String, String> result = new HashMap<String, String>();
        String[] columns = {
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA,
                };

        Cursor c = cr.query(imageUri, columns, null, null, null);
        c.moveToFirst();
        result.put(MediaStore.Images.Media.ORIENTATION, String.valueOf(c.getInt(0)));
        result.put(MediaStore.Images.Media.DISPLAY_NAME, c.getString(1));
        result.put(MediaStore.Images.Media.SIZE, String.valueOf(c.getLong(2)));
        result.put(MediaStore.Images.Media.DATA, String.valueOf(c.getString(3)));
        c.close();

        return result;
    }

    /**
     * カメラファイルの保存先データを取得する
     * @param saveDirName
     * @return
     */
    public static HashMap<String, String> getCameraFileValues(String saveDirName) {
        HashMap<String, String> fileValues = new HashMap<String, String>();
        long currentTime = System.currentTimeMillis();
        String title = String.valueOf(currentTime);
        String dirPath = Utils.getDirectoryPath(saveDirName);
        String fileName = "toflea_" + title + ".jpg";
        String path = dirPath + "/" + fileName;

        fileValues.put(MediaStore.Images.Media.TITLE, title);
        fileValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        fileValues.put(MediaStore.Images.Media.DATA, path);

        return fileValues;
    }

    /**
     * カメラ撮影した写真がギャラリーに表示するようにデータベースに保存する
     * @param contentResolver
     * @param fileValues
     * @return
     */
    public static Uri saveImageFile(ContentResolver contentResolver, HashMap<String, String> fileValues) {
        String imgPath = fileValues.get(MediaStore.Images.Media.DATA);
        long imgDate = System.currentTimeMillis();

        int orientation = 0;
        try {
            ExifInterface exif = new ExifInterface(imgPath);
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (exifOrientation) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    orientation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    orientation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    orientation = 270;
                    break;
            }
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.TITLE, fileValues.get(MediaStore.Images.Media.TITLE));
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileValues.get(MediaStore.Images.Media.DISPLAY_NAME));
        contentValues.put(MediaStore.Images.Media.DATA, imgPath);
        contentValues.put(MediaStore.Images.Media.DATE_ADDED, imgDate);
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, imgDate);
        contentValues.put(MediaStore.Images.Media.DATE_MODIFIED, imgDate);
        contentValues.put(MediaStore.Images.Media.ORIENTATION, orientation);

        return contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

}

