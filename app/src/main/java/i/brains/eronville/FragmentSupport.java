package i.brains.eronville;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FragmentSupport extends XFragment{

    Button mail;

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_support, child, false);

        mail = view.findViewById(R.id.mail);
        mail.setOnClickListener(v -> {
            EditText edit_mail = view.findViewById(R.id.support_mail);
            EditText edit_name = view.findViewById(R.id.support_name);
            EditText edit_message = view.findViewById(R.id.support_message);

            String str_mail = edit_mail.getText().toString();
            String str_name = edit_name.getText().toString();
            String str_message = edit_message.getText().toString();

            sendMail(str_mail, str_name, str_message);
        });
        return view;
    }

    private void sendMail(String email, String name, String message){
        mail.setEnabled(false);
        mail.setText("Sending Message");
        mail.setBackgroundResource(R.drawable.button_off);

        Volley.newRequestQueue(cx).add(new StringRequest(Request.Method.POST, XClass.apiSendMail, response -> {
            mail.setEnabled(false);
            mail.setText("Mail Sent");
            mail.setTextColor(cx.getResources().getColor(R.color.colorText));
        }, error -> {
            mail.setEnabled(true);
            mail.setText("Resend Message");
            mail.setBackgroundResource(R.drawable.button);
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> post = new HashMap<>();
                post.put("mail", email);
                post.put("name", name);
                post.put("message", message);
                return post;
            }
        });
    }
}