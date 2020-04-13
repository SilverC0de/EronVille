package i.brains.eronville;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class FragmentAddApartmentImages extends XFragment implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        return null;
    }

    private void addImage(Intent uri){
        //avi.setImageURI(uri.getData());

        String avi_path = "";
        String avi_name = String.valueOf(System.currentTimeMillis());
        String avi_link = "https://cdn.eronville.com/apartment/" + avi_name + ".jpeg";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = cx.getContentResolver().query(uri.getData(), projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column = cursor.getColumnIndexOrThrow(projection[0]);
            avi_path = cursor.getString(column);
            cursor.close();
        }


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
        SimpleDraweeView draweeView = new SimpleDraweeView(cx);
        draweeView.setImageURI(uri.getData());
        draweeView.setLayoutParams(params);

        //image_row.addView(draweeView);

        //avi_progress.setVisibility(View.VISIBLE);

        XClass.addImageOnServer("avi", avi_link, "mail");
        //XClass.upload(fx, avi_progress, avi_path, draweeView, avi_name);
    }

}