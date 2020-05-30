package i.brains.eronville;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class FragmentChat extends XFragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_chat, child, false);
        LinearLayout list_blind = view.findViewById(R.id.chat_blind);
        ListView villas = view.findViewById(R.id.chat_list);

        List<HashMap<String, String>> list = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<XChat> object = realm.where(XChat.class).findAll();

        object.load();
        realm.beginTransaction();

        if (object.size() != 0){
            list_blind.setVisibility(View.GONE);
            for (int i = 0; i < object.size(); i++){
                HashMap<String, String> map = new HashMap<>();


                int cost = Integer.parseInt(object.get(i).getPrice());
                map.put("mail", object.get(i).getMail());
                map.put("line", object.get(i).getLine());
                map.put("whatsapp", object.get(i).getWhatsapp());
                map.put("image", object.get(i).getImage());
                map.put("price", "₦" + NumberFormat.getIntegerInstance().format(cost));
                map.put("details", object.get(i).getDetails());

                list.add(map);
            }

            String[] from = {"price", "details"};
            int[] to = {R.id.chat_details};

            SimpleAdapter adapter = new SimpleAdapter(cx, list, R.layout.xml_agent, from, to){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    Spinner status = v.findViewById(R.id.chat_status);
                    ImageView whatsapp = v.findViewById(R.id.chat_whatsapp);
                    ImageView mail = v.findViewById(R.id.chat_mail);
                    ImageView line = v.findViewById(R.id.chat_phone);

                    ArrayAdapter<CharSequence> apartment_adapter = ArrayAdapter.createFromResource(cx, R.array.inspection, R.layout.xml_spinner);
                    apartment_adapter.setDropDownViewResource(R.layout.xml_spinner);
                    status.setAdapter(apartment_adapter);
                    status.setOnItemSelectedListener(FragmentChat.this);

                    String str_line = list.get(position).get("line");
                    String str_mail = list.get(position).get("mail");
                    String str_image = list.get(position).get("image");
                    String str_whatsapp = list.get(position).get("whatsapp");

                    whatsapp.setOnClickListener(x->{
                        String whatsAppRoot = "http://api.whatsapp.com/";
                        String number = "send?phone=" + str_whatsapp;
                        String text = "&text=Hello, I would love to get an apartment, i found your number on EronVille app.";
                        String uri = whatsAppRoot+number+text;

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);
                    });
                    mail.setOnClickListener(x->{
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",str_mail, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I need to get an apartment from you via EronVille");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I would love to get an apartment, I found your number on EronVille app.");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    });
                    line.setOnClickListener(x->{
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + str_line));
                        startActivity(intent);
                    });
                    return v;
                }
            };
            villas.setAdapter(adapter);
            villas.setOnItemClickListener((parent, view1, position, id) -> {

                parent.getItemAtPosition(position);
            });
        }
        realm.close();
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
