package i.brains.eronville;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

public class ActivityAgent extends XActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        TextView greetings = findViewById(R.id.greetings);

        String time;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12) time = "Good morning";
        else if(timeOfDay < 16) time = "Good afternoon";
        else time = "Good evening";

        greetings.setText(String.format("Hi, %s", time));
    }
}
