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

        // Utils.md5String and sha256String Sample
        String originText = "This is origin text.";
        Log.v("TEST", "origin text = " + originText);
        Log.v("TEST", "md5 text = " + Utils.md5String(originText));
        Log.v("TEST", "sha256 text = " + Utils.sha256String(originText));

        // Utils.isMatchHttpUrl Sample
        String string1 = "https://twitter.com";
        String string2 = "twitter";
        Log.v("TEST", "Is match url1 : " + Utils.isMatchHttpUrl(string1));
        Log.v("TEST", "Is match url2 : " + Utils.isMatchHttpUrl(string2));

        // Utils.replaceAll Sample
        String sampleText = "be together. not the same. Android... be yourself. do your thing. see what's up. Android";
        Log.v("TEST", sampleText);
        Log.v("TEST", Utils.replaceAll(sampleText, "Android", "XXXXXXX"));

    }
}
