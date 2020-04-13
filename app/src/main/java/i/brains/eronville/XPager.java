package i.brains.eronville;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class XPager extends PagerAdapter {

    private Context cx;
    private ArrayList<String> images;

    public XPager(Context cx, ArrayList<String> images) {
        this.cx = cx;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(cx, R.layout.xml_image, null);
        SimpleDraweeView img = view.findViewById(R.id.image);

        Log.e("silvr", images.get(position));
        img.setImageURI(images.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}