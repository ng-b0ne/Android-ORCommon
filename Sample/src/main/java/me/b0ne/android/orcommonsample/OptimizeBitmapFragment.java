package me.b0ne.android.orcommonsample;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.b0ne.android.orcommon.ImageUtils;

/**
 * Created by b0ne on 2015/03/21.
 */
public class OptimizeBitmapFragment extends Fragment {

    private ImageView imgLarge;
    private ImageView imgMedium;
    private ImageView imgSmall;

    private static final String KEY_URI = "img_uri";

    public static OptimizeBitmapFragment newInstance(Uri uri) {
        OptimizeBitmapFragment fragment = new OptimizeBitmapFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_URI, uri);
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

        Uri imgUri = getArguments().getParcelable(KEY_URI);

        Bitmap bitmapLarge = ImageUtils.optimizeBitmap(
                getActivity().getContentResolver(), imgUri, 800);
        imgLarge.setImageBitmap(bitmapLarge);

        Bitmap bitmapMedium = ImageUtils.optimizeBitmap(
                getActivity().getContentResolver(), imgUri, 600);
        imgMedium.setImageBitmap(bitmapMedium);

        Bitmap bitmapSmall = ImageUtils.optimizeBitmap(
                getActivity().getContentResolver(), imgUri, 400);
        imgSmall.setImageBitmap(bitmapSmall);

    }
}
