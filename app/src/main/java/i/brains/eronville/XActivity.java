package i.brains.eronville;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class XActivity extends AppCompatActivity {


    SharedPreferences data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorSemiPrimary));
        if (Build.VERSION.SDK_INT > 22) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        data = getSharedPreferences(XClass.sharedPreferences, MODE_PRIVATE);

        super.onCreate(savedInstanceState);
    }
}
