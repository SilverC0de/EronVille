package i.brains.eronville;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tmall.ultraviewpager.UltraViewPager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentVille extends XFragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Button reset;
    private UltraViewPager pager;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        double latitude = getArguments().getDouble("latitude");
        double longitude = getArguments().getDouble("longitude");

        MarkerOptions options = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(mapIcon(cx)).alpha(.8f);

        map.addMarker(options);
        map.setMinZoomPreference(4.0f);
        map.setMaxZoomPreference(map.getMaxZoomLevel());
        map.moveCamera(CameraUpdateFactory.zoomTo(16f));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        reset.setOnClickListener(v-> map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude))));
    }


    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_ville, child, false);

        pager = view.findViewById(R.id.ville_pager);
        ImageView back = view.findViewById(R.id.back);
        Button property = view.findViewById(R.id.property);

        back.setOnClickListener(v -> fx.finish());
        property.setOnClickListener(v ->{
            FragmentBook book = new FragmentBook();
            book.setArguments(getArguments());
            fm.beginTransaction().add(R.id.viewer, book).addToBackStack(null).commit();
        });

        String ville = getArguments().getString("i");
        initializeImages(ville);

        reset = view.findViewById(R.id.reset);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.neighbourhood);
        if (mapFragment != null) mapFragment.getMapAsync(this);
        return view;
    }


    private void initializeImages(String ville){
        ArrayList<String> img = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(XClass.apiImages + "?ville=" + ville).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                fx.runOnUiThread(() -> {
                    try {
                        JSONArray arr = new JSONArray(json);
                        if (arr.length() != 0){
                            for (int i = 0; i < arr.length(); i++){
                                JSONObject obj = arr.getJSONObject(i);
                                String path = obj.getString("path");
                                img.add(path);
                            }
                            XPager adapter = new XPager(cx, img);
                            pager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                            pager.setBackgroundResource(0);
                            pager.setAdapter(adapter);
                            pager.initIndicator();
                            pager.getIndicator()
                                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                                    .setFocusColor(getResources().getColor(R.color.colorPrimary))
                                    .setNormalColor(getResources().getColor(R.color.colorLighterBlue))
                                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));
                            pager.getIndicator().setMargin(0,0,0,44);
                            pager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                            pager.getIndicator().build();
                        }
                    } catch (JSONException ignored){}
                });
            }
        });
    }

    private BitmapDescriptor mapIcon(Context context) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.marker);
        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}