package constants.android.commsware.com.taaaaab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by yong on 15. 1. 28.
 */
public class GlideFragment extends Fragment {

    public View view;
    private ImageView imgGlide;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_glide, container, false);
        imgGlide = (ImageView) view.findViewById(R.id.imgGlide);

        Glide.with(GlideFragment.this)
                .load("https://raw.githubusercontent.com/bumptech/glide/master/static/glide_logo.png")
                .into(imgGlide);

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
