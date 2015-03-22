package me.b0ne.android.orcommonsample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by b0ne on 2015/03/21.
 */
public class MenuListFragment extends Fragment {

    private Context mContext;
    private ListView mListView;

    private static final int REQUEST_CODE_GET_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_list, container, false);
        mListView = (ListView)rootView.findViewById(R.id.listview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        final String[] items = getResources().getStringArray(R.array.menu_items);
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, items);
        mListView.setAdapter(itemsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = items[position];
                Fragment fragment = new Fragment();
                switch (position) {
                    case 0:
                        selectImage();
                        return;
                    case 1:
                        fragment = new ImageListFragment();
                        break;
                    case 2:
                        break;
                }
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.main_content, fragment)
                        .addToBackStack(null)
                        .commit();

                getActivity().setTitle(title);
            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Select image");
        startActivityForResult(chooser, REQUEST_CODE_GET_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GET_IMAGE) {
                Bundle args = new Bundle();
                args.putString("img_uri", data.getData().toString());
                OptimizeBitmapFragment optimizeBitmapFragment = new OptimizeBitmapFragment();
                optimizeBitmapFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.main_content, optimizeBitmapFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
