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

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView nav;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        initializeDrawer();
        bottomNavigation();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentHome()).commit();
    }

    private void initializeDrawer(){

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        nav = findViewById(R.id.navigation_view); //get footer from here
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.app_name, R.string.app_name);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable menu = ResourcesCompat.getDrawable(getResources(), R.drawable.menu, getApplicationContext().getTheme());
        toggle.setHomeAsUpIndicator(menu);
        toggle.setToolbarNavigationClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        //beginning of footer
        Button agent_mode = nav.findViewById(R.id.agent_mode);
        agent_mode.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ActivityAgent.class));
        });
        //end of footer

        nav.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nav_item_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();
                    break;
                case R.id.nav_item_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentProfile()).commit();
                    break;
                case R.id.nav_item_refer:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentInvite()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_support:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentSupport()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_faq:
                    getSupportFragmentManager().beginTransaction().add(R.id.fragment, new FragmentFAQ()).addToBackStack(null).commit();
                    break;
                case R.id.nav_item_rate:
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    break;
                case R.id.nav_item_privacy:
                    Intent privacyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eronville.com/privacy"));
                    startActivity(privacyIntent);
                    break;
                case R.id.nav_item_terms:
                    Intent termsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eronville.com/terms"));
                    startActivity(termsIntent);
                    break;
                case R.id.nav_item_logout:
                    SharedPreferences.Editor e = data.edit();
                    e.putBoolean(XClass.online, false);
                    e.apply();

                    startActivity(new Intent(getApplicationContext(), ActivityAccess.class));
                    finish();
                    break;
                case R.id.nav_item_exit:
                    finishAffinity();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        nav.setItemIconTintList(null);
    }

    private void bottomNavigation(){
        ImageView home = findViewById(R.id.home);
        ImageView map = findViewById(R.id.map);
        ImageView chat = findViewById(R.id.chat);
        ImageView profile = findViewById(R.id.profile);

        home.setEnabled(false);
        home.setOnClickListener(v -> {
            home.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            home.setEnabled(false);
            map.setEnabled(true);
            chat.setEnabled(true);
            profile.setEnabled(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentHome()).commit();
        });

        map.setOnClickListener(v -> {
            home.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            home.setEnabled(true);
            map.setEnabled(false);
            chat.setEnabled(true);
            profile.setEnabled(true);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentMap()).commit();
        });

        chat.setOnClickListener(v -> {
            home.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));
            profile.setBackgroundColor(getResources().getColor(R.color.colorTransparent));


            home.setEnabled(true);
            map.setEnabled(true);
            chat.setEnabled(false);
            profile.setEnabled(true);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentChat()).commit();

        });

        profile.setOnClickListener(v -> {
            home.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            map.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            chat.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            profile.setBackgroundColor(getResources().getColor(R.color.colorLighterBlue));


            home.setEnabled(true);
            map.setEnabled(true);
            chat.setEnabled(true);
            profile.setEnabled(false);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new FragmentProfile()).commit();
        });
    }
}