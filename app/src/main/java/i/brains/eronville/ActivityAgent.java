package i.brains.eronville;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ActivityAgent extends XActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        TextView after = findViewById(R.id.after);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                after.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }
}