package me.b0ne.android.orcommonsample;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.b0ne.android.orcommon.KVStorage;
import me.b0ne.android.orcommon.Utils;

/**
 * Created by b0ne on 2015/03/23.
 */
public class UtilsFragment extends Fragment {
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.utils, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        // KVStorage Sample
        KVStorage kvStorage = new KVStorage(mContext);
        // KVStorage kvStorage = new KVStorage(mContext, "my-app-storage");

        String myKey = "my-key";
        String stringValue = "android common library";
        String getString = kvStorage.getString(myKey);
        String showResult = "no string for " + myKey;
        if (getString == null) {
            Log.v("TEST", showResult);
            Log.v("TEST", "save [" + stringValue + "] to " + myKey);
            kvStorage.saveString(myKey, stringValue);
        } else {
            showResult = "KVStorage getString = " + getString;
            Log.v("TEST", showResult);
        }

        // Utils.md5String Sample
        String originText = "This is origin text.";
        Log.v("TEST", "origin text = " + originText);
        Log.v("TEST", "md5 text = " + Utils.md5String(originText));


    }
}
