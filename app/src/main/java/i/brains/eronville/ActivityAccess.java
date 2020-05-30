package i.brains.eronville;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivityAccess extends XActivity {

    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        signIn = findViewById(R.id.signin);
        EditText mail = findViewById(R.id.accessMail);
        EditText password = findViewById(R.id.accessPassword);
        TextView switchAccess = findViewById(R.id.switchAccess);

        String user = data.getString(XClass.mail, "");
        mail.setText(user);

        signIn.setOnClickListener(v -> {

            String userMail = mail.getText().toString();
            String userPassword = password.getText().toString();
            if (userMail.isEmpty() || userPassword.isEmpty()){
                Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show();
            } else {
                signUser(userMail, userPassword);
            }
        });

        switchAccess.setOnClickListener( v->{
            startActivity(new Intent(getApplicationContext(), ActivityRegister.class));
            finish();
        });
    }

    void signUser(String mail, String password){
        signIn.setEnabled(false);
        signIn.setAlpha(.4f);
        signIn.setText("Please Wait");
        StringRequest request = new StringRequest(Request.Method.POST, XClass.apiAccess, response -> {

            if (response.trim().equals("00")){
                //get all details
                SharedPreferences.Editor e = data.edit();
                e.putString(XClass.mail, mail);
                e.putBoolean(XClass.online, true);
                e.apply();

                startActivity(new Intent(getApplicationContext(), ActivityView.class));
                finishAffinity();
            } else {
                signIn.setEnabled(true);
                signIn.setAlpha(1f);
                signIn.setText("Sign In");
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
            }

        }, error -> {
            signIn.setEnabled(true);
            signIn.setAlpha(1f);
            signIn.setText("Sign In");
            Log.e("silvr", "--" + error.getMessage());
            Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> body = new HashMap<>();
                body.put("mail", mail);
                body.put("password", password);
                return body;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(request);
        Volley.newRequestQueue(getApplicationContext()).getCache().clear();
    }
}
