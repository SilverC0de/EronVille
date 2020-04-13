package i.brains.eronville;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class XFragment extends Fragment {

    Activity fx;
    Context cx;
    FragmentManager fm;
    SharedPreferences data;


    @Override
    public void onAttach(@NonNull Context context) {
        fx = getActivity();
        cx = context;

        data = cx.getSharedPreferences(XClass.sharedPreferences, Context.MODE_PRIVATE);
        fm = getFragmentManager();
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onFragmentCreate(inflater, container, savedInstanceState);
    }

    public abstract View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle);
}
