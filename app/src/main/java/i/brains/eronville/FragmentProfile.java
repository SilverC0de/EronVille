package i.brains.eronville;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class FragmentProfile extends XFragment implements AdapterView.OnItemSelectedListener {

    private String dob, mail, avi_path;
    private Spinner edit_gender;
    private EditText edit_name, edit_number, edit_address;
    private TextView account_name;
    private TextView edit_dob;
    private SimpleDraweeView avi;
    private Button save;
    private ProgressBar avi_progress;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent uri) {
        if (requestCode == 22 && resultCode == Activity.RESULT_OK) {

            avi.setImageURI(uri.getData());

            String avi_name = String.valueOf(System.currentTimeMillis());
            String avi_link = "https://cdn.eronville.com/avi/" + avi_name + ".jpeg";
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column = cursor.getColumnIndexOrThrow(projection[0]);
                avi_path = cursor.getString(column);
                cursor.close();
            }
            avi_progress.setVisibility(View.VISIBLE);

            saveData(XClass.avi, avi_link);
            XClass.updateUser("avi", avi_link, mail);
            XClass.upload(fx, avi_progress, avi_path, avi, avi_name, "avi");
        }
        super.onActivityResult(requestCode, resultCode, uri);
    }

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_profile, child, false);


        TextView account_mail = view.findViewById(R.id.account_mail);

        save = view.findViewById(R.id.save);
        mail = data.getString(XClass.mail, null);

        avi = view.findViewById(R.id.avi);
        avi_progress = view.findViewById(R.id.avi_progress);
        account_name = view.findViewById(R.id.account_name);

        edit_name = view.findViewById(R.id.edit_name);
        edit_number = view.findViewById(R.id.edit_number);
        edit_address = view.findViewById(R.id.edit_address);
        edit_gender = view.findViewById(R.id.edit_gender);
        edit_dob = view.findViewById(R.id.edit_dob);


        String xname = data.getString(XClass.name, XClass.outcast);
        String xmail = data.getString(XClass.mail, XClass.outcast);
        String xnumber = data.getString(XClass.number, XClass.outcast);
        String xdob = data.getString(XClass.dob, XClass.outcast);
        String xaddress = data.getString(XClass.address, XClass.outcast);

        account_name.setText(xname);
        account_mail.setText(xmail);

        edit_name.setText(xname);
        edit_number.setText(xnumber);
        edit_dob.setText(xdob);
        edit_address.setText(xaddress);

        String avatar = data.getString(XClass.avi, "00");
        avi.setImageURI(avatar);
        avi.setOnClickListener(v->{
            Intent addImage = new Intent(Intent.ACTION_GET_CONTENT);
            addImage.setType("image/*");
            startActivityForResult(addImage, 22);
        });

        save.setOnClickListener(v-> {
            String name = edit_name.getText().toString().trim();
            edit_name.setEnabled(false);

            account_name.setText(name);

            saveData(XClass.name, name);
            XClass.updateUser("name", name, mail);




            String number = edit_number.getText().toString().trim();
            edit_number.setEnabled(false);

            saveData(XClass.number, number);
            XClass.updateUser("line", number, mail);





            String addr = edit_address.getText().toString().trim();
            edit_address.setEnabled(false);

            saveData(XClass.address, addr);
            XClass.updateUser("address", addr, mail);

            save.setText("Saved");
        });
        initializeEditor();
        return view;
    }


    private void initializeEditor() {
        ArrayAdapter<CharSequence> bed_adapter = ArrayAdapter.createFromResource(cx, R.array.gender, R.layout.xml_spinner);
        bed_adapter.setDropDownViewResource(R.layout.xml_spinner);
        edit_gender.setAdapter(bed_adapter);







        edit_gender.setOnItemSelectedListener(this);

        edit_dob.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(cx,
                    (view, yearOfCentury, monthOfYear, dayOfMonth) -> {
                        Calendar cc = Calendar.getInstance();
                        int day1 = view.getDayOfMonth();
                        int month1 = view.getMonth();
                        int year1 = view.getYear();
                        cc.set(year1, month1, day1);
                        dob = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(cc.getTime()); //25 Jan 2019
                        edit_dob.setText(dob);
                        saveData(XClass.dob, dob);
                        XClass.updateUser("dob", dob, mail);

                    },
                    year,
                    month,
                    day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }
    


    private void saveData(String name, String value){

        SharedPreferences.Editor e = data.edit();
        e.putString(name, value);
        e.apply();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String gender = parent.getItemAtPosition(position).toString();
        saveData(XClass.gender, gender);
        XClass.updateUser("gender", gender, mail);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}