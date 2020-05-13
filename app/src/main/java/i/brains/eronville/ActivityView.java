package i.brains.eronville;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/* Drawer activity created by silver 2020 */
/* Use this activity as the main drawer activity */

public class ActivityView extends XActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentMap()).commit();

        bottomNavigation();
    }

    private void bottomNavigation(){
        ImageView map = findViewById(R.id.map);
        ImageView chat = findViewById(R.id.chat);
        ImageView profile = findViewById(R.id.profile);
        ImageView more = findViewById(R.id.more);

        map.setEnabled(false);

        map.setOnClickListener(v -> {
            more.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            more.setEnabled(true);
            map.setEnabled(false);
            chat.setEnabled(true);
            profile.setEnabled(true);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentMap()).commit();
        });

        chat.setOnClickListener(v -> {
            more.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            more.setEnabled(true);
            map.setEnabled(true);
            chat.setEnabled(false);
            profile.setEnabled(true);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentChat()).commit();

        });

        profile.setOnClickListener(v -> {
            more.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));


            more.setEnabled(true);
            map.setEnabled(true);
            chat.setEnabled(true);
            profile.setEnabled(false);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentProfile()).commit();
        });
        more.setOnClickListener(v -> {
            more.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            more.setEnabled(false);
            map.setEnabled(true);
            chat.setEnabled(true);
            profile.setEnabled(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentMore()).commit();
        });

    }
}