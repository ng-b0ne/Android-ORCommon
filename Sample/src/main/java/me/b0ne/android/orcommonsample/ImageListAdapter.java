package me.b0ne.android.orcommonsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;

import me.b0ne.android.orcommon.ImageUtils;

/**
 * Created by b0ne on 2015/03/22.
 */
public class ImageListAdapter extends ArrayAdapter<HashMap<String, String>> {

    private ImageLoader mImageLoader;

    public ImageListAdapter(Context context) {
        super(context, 0);

        mImageLoader = ImageUtils.getImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image_row, parent, false);
        }

        HashMap<String, String> item = getItem(position);
        if (item != null) {
            TextView imgText = (TextView) view.findViewById(R.id.image_text);
            NetworkImageView imgView = (NetworkImageView) view.findViewById(R.id.image_view);

            imgText.setText(item.get("img_text"));
            imgView.setImageUrl(item.get("img_url"), mImageLoader);
        }

        return view;
    }
}