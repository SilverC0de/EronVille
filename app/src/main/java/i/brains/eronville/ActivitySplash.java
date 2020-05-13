package i.brains.eronville;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivitySplash extends XActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            String mail = data.getString(XClass.mail, XClass.outcast);
            boolean isOnline = data.getBoolean(XClass.online, false);

            if (isOnline) {
                if (mail.isEmpty()){
                    startActivity(new Intent(getApplicationContext(), ActivityIntro.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), ActivityView.class));
                }
            } else {
                startActivity(new Intent(getApplicationContext(), ActivityIntro.class));
            }
            finish();
        }, 2000);
    }
}
