package i.brains.eronville;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

public class FragmentAgent extends XFragment {

    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_agent, child, false);


        return view;
    }
}
