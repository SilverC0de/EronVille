package i.brains.eronville;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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

import java.text.NumberFormat;
import java.util.Objects;

public class FragmentMap extends XFragment implements OnMapReadyCallback {

    private GoogleMap map;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(7.401590, 3.907290);

        MarkerOptions house1 = new MarkerOptions().position(new LatLng(7.401590, 3.907290)).icon(mapIcon(cx)).alpha(.8f);
        MarkerOptions house2 = new MarkerOptions().position(new LatLng(7.403530, 3.904011)).icon(mapIcon(cx)).alpha(.8f);

        map.addMarker(house1);
        map.addMarker(house2);


        map.moveCamera(CameraUpdateFactory.newLatLng(latLng)); //for ibadan, add bounds
        map.moveCamera(CameraUpdateFactory.zoomTo(14f));
        map.setMinZoomPreference(4.0f);
        map.setMaxZoomPreference(map.getMaxZoomLevel());
    }



    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_map, child, false);
        ImageView filter = view.findViewById(R.id.filter_view);


        initializeMap();
        filter.setOnClickListener(v ->{
            FragmentMap.Lookup lookup = new FragmentMap.Lookup();
            fm.beginTransaction().replace(R.id.filter_frame, lookup).addToBackStack(null).commit();
        });
        return view;
    }

    private void initializeMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);

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

        private String city = "", x_min = "0", x_max = "0";
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
