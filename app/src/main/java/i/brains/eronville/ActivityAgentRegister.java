package i.brains.eronville;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityAgentRegister extends XActivity {

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_register);
        EditText mail = findViewById(R.id.registerMail);
        EditText password = findViewById(R.id.registerPassword);
        EditText surname = findViewById(R.id.registerSurname);
        EditText othername = findViewById(R.id.registerOthername);
        EditText whatsapp = findViewById(R.id.registerWhatsapp);
        EditText line = findViewById(R.id.registerNumber);

        TextView switchAccess = findViewById(R.id.switchAccess);


        register = findViewById(R.id.register);

        register.setOnClickListener(v -> {
            String userMail = mail.getText().toString();
            String userSurname = surname.getText().toString();
            String userOthername = othername.getText().toString();
            String userLine = line.getText().toString();
            String userPassword = password.getText().toString();
            String userWhatsapp = whatsapp.getText().toString();

            if (userMail.isEmpty()){
                Toast.makeText(this, "Enter your mail address", Toast.LENGTH_SHORT).show();
            } else if(userPassword.isEmpty()){
                Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
            } else if (userWhatsapp.length() < 12) {
                Toast.makeText(this, "Whatsapp number should be in int'l format", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(userMail, userPassword, userLine, userSurname, userOthername, userWhatsapp);
            }
        });

        switchAccess.setOnClickListener( v->{
            startActivity(new Intent(getApplicationContext(), ActivityAgentAccess.class));
            finish();
        });
    }


    private void registerUser(String mail, String password, String line, String surname, String  othername, String whatsapp){
        register.setEnabled(false);
        register.setAlpha(.4f);
        register.setText("Please Wait");
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiAgentRegister, response -> {
            if (response.trim().equals("00")){
                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.agent, mail);
                e.putString(XClass.agent_line, line);
                e.putString(XClass.surname, surname);
                e.putString(XClass.othername, othername);
                e.putString(XClass.agent_whatsapp, whatsapp);
                e.apply();

                startActivity(new Intent(getApplicationContext(), ActivityAgent.class));
                finish();
            } else {
                register.setEnabled(true);
                register.setAlpha(1f);
                register.setText("Register");
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Log.e("silvr", "--" + error.getMessage());
            register.setEnabled(true);
            register.setAlpha(1f);
            register.setText("Register");
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> body = new HashMap<>();
                body.put("mail", mail);
                body.put("password", password);
                body.put("line", line);
                body.put("surname", surname);
                body.put("othername", othername);
                return body;

            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Volley.newRequestQueue(getApplicationContext()).getCache().clear();
    }
}
