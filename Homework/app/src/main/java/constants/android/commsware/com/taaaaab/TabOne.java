package constants.android.commsware.com.taaaaab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yong on 15. 1. 23.
 */
public class TabOne extends Fragment {
    public View view;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Log.d(TAG,"onActivityCreated");
        super.onActivityCreated(savedInstanceState);


    }

    public void onResume() {
        super.onResume();
        // 작성.
    }

    public void onPause() {
        super.onPause();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_one, container, false);
        return view;
    }
}
