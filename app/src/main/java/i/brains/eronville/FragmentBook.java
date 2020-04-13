package i.brains.eronville;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;

public class FragmentBook extends XFragment {

    private String time = "morning";
    private String schedule = "tomorrow";
    private TextView preview;
    private boolean tomorrow = true;


    @SuppressLint("SetTextI18n")
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_book, child, false);

        preview = view.findViewById(R.id.inspection_preview);
        ImageView phone = view.findViewById(R.id.chat_phone);
        ImageView mail = view.findViewById(R.id.chat_mail);
        ImageView whatsapp = view.findViewById(R.id.chat_whatsapp);
        ImageView back = view.findViewById(R.id.back);

        LinearLayout picker = view.findViewById(R.id.inspection_picker);
        TextView date = view.findViewById(R.id.inspection_date);

        TextView today = view.findViewById(R.id.inspection_picker_today);
        LinearLayout today_view = view.findViewById(R.id.inspection_picker_today_view);
        TextView this_morning = view.findViewById(R.id.inspection_morning);
        TextView this_afternoon = view.findViewById(R.id.inspection_afternoon);
        TextView this_evening = view.findViewById(R.id.inspection_evening);
        TextView morning = view.findViewById(R.id.inspection_picker_morning);
        TextView afternoon = view.findViewById(R.id.inspection_picker_afternoon);
        TextView evening = view.findViewById(R.id.inspection_picker_evening);

        Button send = view.findViewById(R.id.inspect);

        Bundle bnd = getArguments();
        String str_i = bnd.getString("i");
        String str_image = bnd.getString("image");
        String str_mail = bnd.getString("agent");
        String str_line = bnd.getString("agent_line");
        String str_whatsapp = bnd.getString("agent_whatsapp");
        String str_price = bnd.getString("price");
        String str_details = bnd.getString("details");

        Calendar cc = Calendar.getInstance();
        schedule = "Today, " + new SimpleDateFormat("MMMM d", Locale.ENGLISH).format(cc.getTime());
        int timeOfDay = cc.get(Calendar.HOUR_OF_DAY); //0 - 24

        if (timeOfDay > 10) this_morning.setVisibility(View.GONE);
        if (timeOfDay > 14) this_afternoon.setVisibility(View.GONE);
        if (timeOfDay > 18) {
            today.setVisibility(View.GONE);
            today_view.setVisibility(View.GONE);
            tomorrow = true;
        }


        today.setText(schedule);
        this_morning.setOnClickListener(x-> {
            cl(view);
            time = "morning";
            preview.setText(String.format("You have scheduled to meet with the agent this %s", time));
            this_morning.setBackgroundResource(R.drawable.button_off);
        });
        this_afternoon.setOnClickListener(x-> {
            cl(view);
            time = "afternoon";
            preview.setText(String.format("You have scheduled to meet with the agent this %s", time));

            this_afternoon.setBackgroundResource(R.drawable.button_off);

        });
        this_evening.setOnClickListener(x-> {
            cl(view);
            time = "evening";
            preview.setText(String.format("You have scheduled to meet with the agent this %s", time));
            this_evening.setBackgroundResource(R.drawable.button_off);

        });

        morning.setOnClickListener(x-> {
            cl(view);
            time = "morning";

            if (tomorrow) preview.setText("You have scheduled to meet with the agent tomorrow in the " + time);
            else preview.setText(String.format("You have scheduled to meet with the agent on %s in the %s", schedule, time));

            morning.setBackgroundResource(R.drawable.button_off);
        });
        afternoon.setOnClickListener(x-> {
            cl(view);
            time = "afternoon";

            if (tomorrow) preview.setText("You have scheduled to meet with the agent tomorrow in the " + time);
            else preview.setText(String.format("You have scheduled to meet with the agent on %s in the %s", schedule, time));

            afternoon.setBackgroundResource(R.drawable.button_off);
        });
        evening.setOnClickListener(x-> {
            cl(view);
            time = "evening";

            if (tomorrow) preview.setText("You have scheduled to meet with the agent tomorrow in the " + time);
            else preview.setText(String.format("You have scheduled to meet with the agent on %s in the %s", schedule, time));

            evening.setBackgroundResource(R.drawable.button_off);
        });


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
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{str_mail});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "I need to get an apartment from you via EronVille");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I would love to get an apartment, I found your number on EronVille app.");
            emailIntent.setType("message/rfc822");
            try {
                startActivity(emailIntent);
            } catch (android.content.ActivityNotFoundException ignored) {
                Toast.makeText(fx, "No email clients installed", Toast.LENGTH_SHORT).show();
            }
        });
        phone.setOnClickListener(x->{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + str_line));
            startActivity(intent);
        });



        picker.setOnClickListener(v->{
            final Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH);
            int dd = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(cx, (i, yearOfCentury, monthOfYear, dayOfMonth) -> {
                        //cl(view);
                        tomorrow = false;
                        int day = i.getDayOfMonth();
                        int month = i.getMonth();
                        int year = i.getYear();
                        cc.set(year, month, day);
                        String dob = new SimpleDateFormat("MMMM d", Locale.ENGLISH).format(cc.getTime()); //25 Jan 2019
                        date.setText(dob);
                        schedule = "on " + dob;
                        preview.setText(String.format("You have scheduled to meet with the agent %s in the %s", schedule, time));
                    }, yy, mm, dd);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        send.setOnClickListener(v-> {
            Dialog dl = new Dialog(fx);
            dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dl.setContentView(R.layout.xml_ok);
            dl.setOnCancelListener(c-> fm.popBackStack());
            dl.show();

            Realm realm = Realm.getDefaultInstance();

            final XChat ch = new XChat();
            ch.setI(str_i);
            ch.setImage(str_image);
            ch.setLine(str_line);
            ch.setMail(str_mail);
            ch.setPrice(str_price);
            ch.setDetails(str_details);
            ch.setWhatsapp(str_whatsapp);
            ch.setStamp(String.valueOf(System.currentTimeMillis()));
            realm.executeTransaction(rm -> rm.copyToRealmOrUpdate(ch));

            send.setTextColor(getResources().getColor(R.color.colorText));
            send.setBackgroundResource(R.drawable.button_off);
            send.setEnabled(false);
            send.setText("Inspection request has been sent");
        });

        back.setOnClickListener(v-> fm.popBackStack());
        return view;
    }

    private void cl(View x){
        TextView this_morning = x.findViewById(R.id.inspection_morning);
        TextView this_afternoon = x.findViewById(R.id.inspection_afternoon);
        TextView this_evening = x.findViewById(R.id.inspection_evening);
        TextView morning = x.findViewById(R.id.inspection_picker_morning);
        TextView afternoon = x.findViewById(R.id.inspection_picker_afternoon);
        TextView evening = x.findViewById(R.id.inspection_picker_evening);

        this_morning.setBackgroundResource(R.drawable.cover);
        this_afternoon.setBackgroundResource(R.drawable.cover);
        this_evening.setBackgroundResource(R.drawable.cover);

        morning.setBackgroundResource(R.drawable.cover);
        afternoon.setBackgroundResource(R.drawable.cover);
        evening.setBackgroundResource(R.drawable.cover);
    }
}
