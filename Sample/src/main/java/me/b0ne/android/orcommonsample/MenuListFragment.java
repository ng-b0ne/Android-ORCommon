package me.b0ne.android.orcommonsample;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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
                new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, items);
        mListView.setAdapter(itemsAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = items[position];
                Fragment fragment = new Fragment();
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
//                getFragmentManager().beginTransaction()
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .replace(R.id.ma)

                getActivity().setTitle(title);
            }
        });


    }

}
