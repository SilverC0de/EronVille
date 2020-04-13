package i.brains.eronville;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FragmentHome extends XFragment {

    private LottieAnimationView loader;
    private ImageView empty;
    private List<XBase> list;
    private XListView listings;
    private int page = 0;
    private Button reload;

    private String location = XClass.outcast;



    @SuppressLint("SetTextI18n")
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_home, child, false);
        TextView greetings = view.findViewById(R.id.greetings);
        TextView search_view = view.findViewById(R.id.search_view);
        String surname = data.getString(XClass.surname, "");

        String time;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12) time = "Good morning";
        else if(timeOfDay < 16) time = "Good afternoon";
        else time = "Good evening";

        if (!surname.isEmpty()) greetings.setText(String.format("Hi %s, %s", surname, time));
        else greetings.setText(String.format("Hi, %s", time));


        loader = view.findViewById(R.id.loader);
        empty = view.findViewById(R.id.empty_box);
        listings = view.findViewById(R.id.list);
        reload = view.findViewById(R.id.reload);

        if (getArguments() != null) {
            location = getArguments().getString("city");
            assert location != null;
            if (location.isEmpty()) search_view.setText("Based on your filter");
            else search_view.setText(String.format("Apartments at %s", location));
        } else if (location.isEmpty()) search_view.setText("Recently uploaded apartments");

        loadApartments(page, location);
        return view;
    }


    private void loadApartments(int batch, String location){
        //page = batch + 1;
        list = new ArrayList<>();


        StringRequest request = new StringRequest(Request.Method.GET, XClass.apiApartments + "?batch=" + batch + "&city=" + location, response -> {
            try{
                JSONArray array = new JSONArray(response);
                if (array.length() == 0){
                    loader.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        String x = object.getString("i");
                        String city = object.getString("city");
                        String type = object.getString("type");
                        String price = object.getString("price");
                        String image = object.getString("image");
                        String agent = object.getString("agent"); //mail
                        String agent_line = object.getString("agent_line");
                        String agent_whatsapp = object.getString("agent_whatsapp");

                        double latitude = object.getDouble("latitude");
                        double longitude = object.getDouble("longitude");

                        list.add(new XBase(x, city, type, price, image, agent, agent_line, agent_whatsapp, latitude, longitude));
                    }
                    XAdapter adapter = new XAdapter(cx, R.layout.xml_list, list, fm);
                    loader.setVisibility(View.GONE);
                    listings.setVisibility(View.VISIBLE);
                    listings.setAdapter(adapter);
                    listings.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(fx, ActivityViewer.class);
                        intent.putExtra("action", "ville");
                        intent.putExtra("i", list.get(position).getI());
                        intent.putExtra("image", list.get(position).getImage());
                        intent.putExtra("price", list.get(position).getPrice());
                        intent.putExtra("latitude", list.get(position).getLatitude());
                        intent.putExtra("longitude", list.get(position).getLongitude());
                        intent.putExtra("agent", list.get(position).getAgent());
                        intent.putExtra("agent_line", list.get(position).getLine());
                        intent.putExtra("agent_whatsapp", list.get(position).getWhatsapp());
                        intent.putExtra("details", String.format("%s at %s", list.get(position).getType(), list.get(position).getCity()));
                        startActivity(intent);
                    });
                    if (array.length() > 18) new Handler().post(() -> loadApartments(page++, location));
                }
            } catch (JSONException ignored){}
        }, error -> {
            loader.setVisibility(View.GONE);

            reload.setVisibility(View.VISIBLE);
            reload.setOnClickListener(v -> fm.beginTransaction().replace(R.id.fragment, new FragmentHome()).commit());
        });
        Volley.newRequestQueue(cx).add(request);
        Volley.newRequestQueue(cx).getCache().clear();
    }
}