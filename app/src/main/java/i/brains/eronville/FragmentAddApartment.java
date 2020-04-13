package i.brains.eronville;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddApartment extends XFragment implements AdapterView.OnItemSelectedListener {

    private Spinner add_city;
    private EditText lat, lon, price;
    String state, city, apartment_type;
    private Button add;
    private ProgressBar loader;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent uri) {
        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            String avi_path = "";
            String avi_name = String.valueOf(System.currentTimeMillis());
            String avi_link = "https://cdn.eronville.com/apartment/" + avi_name + ".jpeg";
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column = cursor.getColumnIndexOrThrow(projection[0]);
                avi_path = cursor.getString(column);
                cursor.close();
            }

            String str_latitude = lat.getText().toString().trim();
            String str_longitude = lon.getText().toString().trim();
            String str_price = price.getText().toString();
            uploadHouse(avi_path, avi_name, str_price, avi_link, str_latitude, str_longitude);
        }
        super.onActivityResult(requestCode, resultCode, uri);
    }


    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_add_apartment, child, false);

        add_city = view.findViewById(R.id.add_city);
        Spinner state_picker = view.findViewById(R.id.add_state);
        Spinner apartment = view.findViewById(R.id.add_apartment_type);
        loader = view.findViewById(R.id.loader);
        price = view.findViewById(R.id.add_price);
        lat = view.findViewById(R.id.add_latitude);
        lon = view.findViewById(R.id.add_longitude);
        add = view.findViewById(R.id.add);


        ArrayAdapter<CharSequence> state_adapter = ArrayAdapter.createFromResource(cx, R.array.state, R.layout.xml_spinner);
        state_adapter.setDropDownViewResource(R.layout.xml_spinner);
        state_picker.setAdapter(state_adapter);
        state_picker.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.apartment_type, R.layout.xml_spinner);
        apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
        apartment.setAdapter(apartment_adapter);
        apartment.setOnItemSelectedListener(this);


        add.setOnClickListener(c->{
            String n = lat.getText().toString();
            String e = lon.getText().toString();
            String p = price.getText().toString();
            if (state.equals("Pick a state") || city.isEmpty()){
                Toast.makeText(fx, "Pick a location", Toast.LENGTH_SHORT).show();
            } else if (n.isEmpty() || e.isEmpty()){
                Toast.makeText(fx, "Get the latitude and longitude from google map", Toast.LENGTH_SHORT).show();
            } else if (apartment_type.equals("Pick apartment type")) {
                Toast.makeText(fx, "Choose apartment type", Toast.LENGTH_SHORT).show();
            } else if(p.isEmpty()){
                Toast.makeText(fx, "Enter a yearly estimated price", Toast.LENGTH_SHORT).show();
            } else {
                loader.setVisibility(View.VISIBLE);
                Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
                addImage.setType("image/*");
                startActivityForResult(addImage, 22);
            }

        });
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<CharSequence> state_adapter;
        switch (parent.getId()){
            case R.id.add_state:
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
                add_city.setAdapter(state_adapter);
                add_city.setOnItemSelectedListener(this);
                state = parent.getItemAtPosition(position).toString();
                break;
            case R.id.add_city:
                city = parent.getItemAtPosition(position).toString();
                break;
            case R.id.add_apartment_type:
                apartment_type = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @SuppressLint("SetTextI18n")
    private void uploadHouse(final String file, final String name, final String price, final String image, final String latitude, final String longitude){
        add.setEnabled(false);
        add.setAlpha(.4f);
        add.setText("Please Wait");
        if (ContextCompat.checkSelfPermission(cx, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(fx, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            add.setText("Add apartment");
            loader.setVisibility(View.GONE);
        } else {
            @SuppressLint("StaticFieldLeak")
            class load extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... voids) {
                    int responseCode;
                    String network = "https://cdn.eronville.com/index.php?action=" + "apartment" + "&name=" + name;
                    HttpURLConnection conn;
                    DataOutputStream dos;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1024 * 1024;
                    File sourceFile = new File(file);

                    if (!sourceFile.isFile()) {
                        fx.runOnUiThread(() -> {
                            add.setText("Add apartment");
                            loader.setVisibility(View.GONE);
                        });
                    } else {
                        try {
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL(network);

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setUseCaches(false);
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("asset", file);

                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"asset\"; filename=\"" + file + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                            }

                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            responseCode = conn.getResponseCode();

                            if (responseCode == 200) {
                                fx.runOnUiThread(() -> {
                                    try {
                                        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiAddApartment, response -> {
                                            //jhgjh
                                            add.setText("Apartment added");
                                            loader.setVisibility(View.GONE);
                                            Toast.makeText(fx, "Apartment added", Toast.LENGTH_SHORT).show();
                                        }, error -> {
                                            Log.e("silvr", "--" + error.getMessage());
                                            add.setEnabled(true);
                                            add.setAlpha(1f);
                                            add.setText("Add apartment");
                                            loader.setVisibility(View.GONE);
                                            Toast.makeText(cx, "An error occurred", Toast.LENGTH_SHORT).show();
                                        }){
                                            @Override
                                            protected Map<String, String> getParams() {
                                                SharedPreferences data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);
                                                String agent = data.getString(XClass.agent, XClass.outcast);
                                                String agent_line = data.getString(XClass.agent_line, XClass.outcast);
                                                String agent_whatsapp = data.getString(XClass.agent_whatsapp, XClass.outcast);

                                                HashMap<String, String> body = new HashMap<>();
                                                body.put("state", state);
                                                body.put("city", city);
                                                body.put("type", apartment_type);
                                                body.put("price", price);
                                                body.put("image", image);
                                                body.put("latitude", latitude);
                                                body.put("longitude", longitude);
                                                body.put("agent", agent);
                                                body.put("agent_line", agent_line);
                                                body.put("agent_whatsapp", agent_whatsapp);
                                                return body;
                                            }
                                        };
                                        Volley.newRequestQueue(fx).add(request);
                                        Volley.newRequestQueue(fx).getCache().clear();
                                    } catch (NullPointerException ignored){ }
                                });
                            }

                            fileInputStream.close();

                            dos.flush();
                            dos.close();
                        } catch (final IOException e) {
                            fx.runOnUiThread(() -> {
                                Log.e("silvr", e.getMessage());
                                loader.setVisibility(View.GONE);
                                add.setText("Add apartment");
                            });
                        }
                    }
                    return null;
                }
            }
            new load().execute();
        }
    }


}
