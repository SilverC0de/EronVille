package i.brains.eronville;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class ActivityViewer extends XActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Bundle bnd = getIntent().getExtras();

        String action = bnd.getString("action");
        if (action != null) {
            switch (action){
                case "agent":
                    FragmentAgent agent = new FragmentAgent();
                    agent.setArguments(bnd);
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, agent).commit();
                    Log.e("silvr -- start", String.valueOf(System.currentTimeMillis()));
                    break;
                case "ville":
                    FragmentVille ville = new FragmentVille();
                    ville.setArguments(bnd);
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, ville).commit();
                    Log.e("silvr -- start", String.valueOf(System.currentTimeMillis()));

                    break;
                case "book":
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, new FragmentBook()).addToBackStack(null).commit();
                    break;
                case "refer":
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, new FragmentInvite()).addToBackStack(null).commit();
                    break;
                case "support":
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, new FragmentSupport()).addToBackStack(null).commit();                    break;
                case "faq":
                    getSupportFragmentManager().beginTransaction().add(R.id.viewer, new FragmentFAQ()).addToBackStack(null).commit();
                    break;
            }
        }
    }
}