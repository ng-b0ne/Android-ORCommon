package me.b0ne.android.orcommonsample;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by b0ne on 2015/03/21.
 */
public class ImageListFragment extends ListFragment {

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.image_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        ImageListAdapter listAdapter = new ImageListAdapter(mContext);
        String[] imgUrlList = getResources().getStringArray(R.array.sample_img_url_list);
        HashMap<String, String> item;

        for (int i=0; i<imgUrlList.length; i++) {
            item = new HashMap<String, String>();
            item.put("img_text", "Image " + (i+1));
            item.put("img_url", imgUrlList[i]);
            listAdapter.add(item);
        }
        setListAdapter(listAdapter);

    }
}
