package i.brains.eronville;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentMap extends XFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private LottieAnimationView loader;
    private String state = "", city = "";
    private int page = 0;
    private TextView info;

    List<XBase> list;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        loadMarkers(page);
    }



    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_map, child, false);
        ImageView filter = view.findViewById(R.id.filter_view);

        list = new ArrayList<>();
        loader = view.findViewById(R.id.loader);
        info = view.findViewById(R.id.map_status);

        Bundle bnd = getArguments();
        if (bnd != null) {
            state = getArguments().getString("state");
            city = getArguments().getString("city");
            if (city.equals(" ")) info.setText(String.format("Showing apartments in %s", state));
            else info.setText(String.format("Showing apartments in %s, %s", city, state));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        filter.setOnClickListener(v ->{
            FragmentMap.Lookup lookup = new FragmentMap.Lookup();
            fm.beginTransaction().replace(R.id.filter_frame, lookup).addToBackStack(null).commit();
        });
        return view;
    }

    private void loadMarkers(int batch){
        StringRequest request = new StringRequest(Request.Method.GET, XClass.apiApartments + "?batch=" + batch + "&state=" + state + "&city=" + city, response -> {
            try{
                loader.setVisibility(View.GONE);
                JSONArray array = new JSONArray(response);
                if (array.length() == 0){
                    //empty
                    Toast.makeText(fx, "No apartments here owned by us", Toast.LENGTH_SHORT).show();
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

                        MarkerOptions options = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(mapIcon(cx)).alpha(.8f);

                        map.addMarker(options);
                        list.add(new XBase(x, city, type, price, image, agent, agent_line, agent_whatsapp, latitude, longitude));
                    }
                    map.setMinZoomPreference(4.0f);
                    map.setMaxZoomPreference(map.getMaxZoomLevel());
                    map.moveCamera(CameraUpdateFactory.zoomTo(5f));
                    map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(9.0820, 8.6753))); //Nigeria
                    if (array.length() > 18) new Handler().post(() -> loadMarkers(page++));
                }
            } catch (JSONException ignored){}
        }, error -> loadMarkers(page++));
        Volley.newRequestQueue(cx).add(request);
        Volley.newRequestQueue(cx).getCache().clear();
    }

    private BitmapDescriptor mapIcon(Context context) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.marker);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    public static class Lookup extends XFragment implements AdapterView.OnItemSelectedListener {

        private String state = "", city = "", x_min = "0", x_max = "0";
        private Spinner city_spinner;

        @Override
        public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
            View view = inflater.inflate(R.layout.fragment_lookup, child, false);

            ImageView close = view.findViewById(R.id.close);
            Spinner state_spinner = view.findViewById(R.id.filter_state);
            Button filter = view.findViewById(R.id.filter_results);
            RangeSeekBar price = view.findViewById(R.id.filter_price);
            TextView price_view = view.findViewById(R.id.filter_price_display);

            city_spinner = view.findViewById(R.id.filter_city);

            close.setOnClickListener(v -> fm.popBackStack());
            price.setOnRangeChangedListener(new OnRangeChangedListener() {
                @Override
                public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                    x_min = String.valueOf(Math.round(leftValue));
                    x_max = String.valueOf(Math.round(rightValue));
                    String min = "₦" + NumberFormat.getNumberInstance().format(Math.round(leftValue));
                    String max = "₦" + NumberFormat.getNumberInstance().format(Math.round(rightValue));
                    price_view.setText(String.format("Price between %s and %s", min, max));
                }

                @Override
                public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

                }

                @Override
                public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

                }
            });
            filter.setOnClickListener( v-> {
                if (city.equals("Select a state first")){
                    fm.popBackStack();
                } else {
                    fm.popBackStack();
                    FragmentMap map = new FragmentMap();

                    Bundle bnd = new Bundle();
                    bnd.putString("state", state);
                    bnd.putString("city", city);
                    bnd.putString("min", x_min);
                    bnd.putString("max", x_max);

                    map.setArguments(bnd);
                    fm.beginTransaction().replace(R.id.fragment, map).commit();
                }
            });


            ArrayAdapter<CharSequence> apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.state, R.layout.xml_spinner);
            apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
            state_spinner.setAdapter(apartment_adapter);
            state_spinner.setOnItemSelectedListener(this);

            return view;
        }


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ArrayAdapter<CharSequence> state_adapter;
            switch (parent.getId()){
                case R.id.filter_state:
                    switch (position){
                        case 1:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.abia, R.layout.xml_spinner);
                            break;
                        case 2:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.adamawa, R.layout.xml_spinner);
                            break;
                        case 3:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.akwa_ibom, R.layout.xml_spinner);
                            break;
                        case 4:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.anambra, R.layout.xml_spinner);
                            break;
                        case 5:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.bauchi, R.layout.xml_spinner);
                            break;
                        case 6:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.bayelsa, R.layout.xml_spinner);
                            break;
                        case 7:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.benue, R.layout.xml_spinner);
                            break;
                        case 8:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.borno, R.layout.xml_spinner);
                            break;
                        case 9:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.cross_river, R.layout.xml_spinner);
                            break;
                        case 10:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.delta, R.layout.xml_spinner);
                            break;
                        case 11:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.ebonyi, R.layout.xml_spinner);
                            break;
                        case 12:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.edo, R.layout.xml_spinner);
                            break;
                        case 13:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.ekiti, R.layout.xml_spinner);
                            break;
                        case 14:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.enugu, R.layout.xml_spinner);
                            break;
                        case 15:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.fct, R.layout.xml_spinner);
                            break;
                        case 16:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.gombe, R.layout.xml_spinner);
                            break;
                        case 17:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.imo, R.layout.xml_spinner);
                            break;
                        case 18:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.jigawa, R.layout.xml_spinner);
                            break;
                        case 19:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.kaduna, R.layout.xml_spinner);
                            break;
                        case 20:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.kano, R.layout.xml_spinner);
                            break;
                        case 21:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.katsina, R.layout.xml_spinner);
                            break;
                        case 22:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.kebbi, R.layout.xml_spinner);
                            break;
                        case 23:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.kogi, R.layout.xml_spinner);
                            break;
                        case 24:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.kwara, R.layout.xml_spinner);
                            break;
                        case 25:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.lagos, R.layout.xml_spinner);
                            break;
                        case 26:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.nasarawa, R.layout.xml_spinner);
                            break;
                        case 27:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.niger, R.layout.xml_spinner);
                            break;
                        case 28:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.ogun, R.layout.xml_spinner);
                            break;
                        case 29:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.ondo, R.layout.xml_spinner);
                            break;
                        case 30:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.osun, R.layout.xml_spinner);
                            break;
                        case 31:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.oyo, R.layout.xml_spinner);
                            break;
                        case 32:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.plateau, R.layout.xml_spinner);
                            break;
                        case 33:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.rivers, R.layout.xml_spinner);
                            break;
                        case 34:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.sokoto, R.layout.xml_spinner);
                            break;
                        case 35:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.taraba, R.layout.xml_spinner);
                            break;
                        case 36:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.zamfara, R.layout.xml_spinner);
                            break;
                        default:
                            state_adapter = ArrayAdapter.createFromResource(cx, R.array.city, R.layout.xml_spinner);
                    }
                    state_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    city_spinner.setAdapter(state_adapter);
                    city_spinner.setOnItemSelectedListener(this);
                    state = parent.getItemAtPosition(position).toString();
                    break;
                case R.id.filter_city:
                    city = parent.getItemAtPosition(position).toString();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //do nothing
        }
    }
}
