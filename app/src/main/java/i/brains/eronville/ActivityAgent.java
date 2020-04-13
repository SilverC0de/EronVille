package i.brains.eronville;

import android.os.Bundle;
import android.widget.LinearLayout;

public class ActivityAgent extends XActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        LinearLayout add = findViewById(R.id.add_apartment);

        add.setOnClickListener(v->{
            getSupportFragmentManager().beginTransaction().add(R.id.agent_view, new FragmentAddApartment()).addToBackStack(null).commit();
        });
    }
}
