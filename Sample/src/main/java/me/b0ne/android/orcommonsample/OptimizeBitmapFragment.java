package me.b0ne.android.orcommonsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.b0ne.android.orcommon.ImageUtils;
import me.b0ne.android.orcommon.KVStorage;

/**
 * Created by b0ne on 2015/03/21.
 */
public class OptimizeBitmapFragment extends Fragment {

    private Context mContext;
    private ImageView imgLarge;
    private ImageView imgMedium;
    private ImageView imgSmall;

    private static final String KEY_URI = "img_uri";
    private static final String KEY_SAVED_KEY = "img_saved_key";
    public static final String KEY_SAVE_IMG_KEY = "save_img_draft";

    public static OptimizeBitmapFragment newInstance(Uri uri) {
        OptimizeBitmapFragment fragment = new OptimizeBitmapFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    public static OptimizeBitmapFragment newInstance(String savedKey) {
        OptimizeBitmapFragment fragment = new OptimizeBitmapFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SAVED_KEY, savedKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.optimize_bitmap, container, false);
        imgLarge = (ImageView)rootView.findViewById(R.id.img_view_large);
        imgMedium= (ImageView)rootView.findViewById(R.id.img_view_medium);
        imgSmall = (ImageView)rootView.findViewById(R.id.img_view_small);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        Uri imgUri = getArguments().getParcelable(KEY_URI);
        String savedImgKey = getArguments().getString(KEY_SAVED_KEY);
        KVStorage kvStorage = new KVStorage(mContext);

        if (imgUri != null) {

            Bitmap bitmapLarge = ImageUtils.optimizeBitmap(
                    getActivity().getContentResolver(), imgUri, 800);
            imgLarge.setImageBitmap(bitmapLarge);


            kvStorage.saveBitmap(KEY_SAVE_IMG_KEY, bitmapLarge);
            Log.v("TEST", "savedBitmap = " + kvStorage.getBitmap(KEY_SAVE_IMG_KEY));

//        Bitmap bitmapMedium = ImageUtils.optimizeBitmap(
//                getActivity().getContentResolver(), imgUri, 600);
//        imgMedium.setImageBitmap(bitmapMedium);
//
//        Bitmap bitmapSmall = ImageUtils.optimizeBitmap(
//                getActivity().getContentResolver(), imgUri, 400);
//        imgSmall.setImageBitmap(bitmapSmall);
        } else if (savedImgKey != null) {
            Bitmap bitmap = kvStorage.getBitmap(savedImgKey);
            Log.v("TEST", "view img from savedImgKey :" + savedImgKey + " : " + bitmap);
            imgLarge.setImageBitmap(bitmap);

            Bitmap bitmapSmall = ImageUtils.optimizeBitmap(bitmap, 400);
            imgSmall.setImageBitmap(bitmapSmall);
        }

    }
}
