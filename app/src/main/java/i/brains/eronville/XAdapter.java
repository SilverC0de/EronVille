package i.brains.eronville;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;

public class XAdapter extends ArrayAdapter<XBase> {

    private int layout;
    private Context cx;
    private FragmentManager fm;
    private List<XBase> list;


    XAdapter(@NonNull Context context, int resource, @NonNull List<XBase> list, FragmentManager fm) {
        super(context, resource, list);
        this.layout = resource;
        this.cx = context;
        this.list = list;
        this.fm = fm;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Material obj;
        if (convertView == null){
            obj = new Material();
            LayoutInflater inflater = LayoutInflater.from(cx);
            convertView = inflater.inflate(layout, parent, false);

            obj.image = convertView.findViewById(R.id.list_image);
            obj.city = convertView.findViewById(R.id.list_city);
            obj.price = convertView.findViewById(R.id.list_price);

            convertView.setTag(obj);
        } else {
            obj = (Material) convertView.getTag();
        }

        int cost = Integer.parseInt(list.get(position).getPrice());

        obj.image.setImageURI(list.get(position).getImage());
        obj.city.setText(String.format("%s at %s", list.get(position).getType(), list.get(position).getCity()));
        obj.price.setText(String.format("₦%s/Year", NumberFormat.getIntegerInstance().format(cost)));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getViewTypeCount() {
        return list.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    static class Material{
        TextView city, price;
        SimpleDraweeView image;
    }
}
