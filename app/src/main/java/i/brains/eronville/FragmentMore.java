package i.brains.eronville;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FragmentMore extends XFragment {
    @Override
    public View onFragmentCreate(LayoutInflater inflater, ViewGroup child, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_more, child, false);

        LinearLayout refer = view.findViewById(R.id.more_refer);
        LinearLayout support = view.findViewById(R.id.more_support);
        LinearLayout faq = view.findViewById(R.id.more_faq);
        LinearLayout rate = view.findViewById(R.id.more_rate);
        LinearLayout privacy = view.findViewById(R.id.more_privacy);
        LinearLayout terms = view.findViewById(R.id.more_terms);
        LinearLayout logout = view.findViewById(R.id.more_logout);
        LinearLayout exit = view.findViewById(R.id.more_exit);

        Button agent = view.findViewById(R.id.agent_mode);


        refer.setOnClickListener(v->{
            Intent intent = new Intent(fx, ActivityViewer.class);
            intent.putExtra("action", "refer");
            startActivity(intent);
        });

        support.setOnClickListener(v->{
            Intent intent = new Intent(fx, ActivityViewer.class);
            intent.putExtra("action", "support");
            startActivity(intent);
        });
        faq.setOnClickListener(v->{
            Intent intent = new Intent(fx, ActivityViewer.class);
            intent.putExtra("action", "faq");
            startActivity(intent);
        });
        rate.setOnClickListener(v->{
            Uri uri = Uri.parse("market://details?id=" + cx.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + cx.getPackageName())));
            }
        });
        privacy.setOnClickListener(v->{
            Intent privacyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eronville.com/privacy"));
            startActivity(privacyIntent);
        });
        terms.setOnClickListener(v->{
            Intent termsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eronville.com/terms"));
            startActivity(termsIntent);
        });
        logout.setOnClickListener(v->{
            SharedPreferences.Editor e = data.edit();
            e.putBoolean(XClass.online, false);
            e.apply();

            startActivity(new Intent(cx, ActivityIntro.class));
            fx.finish();
        });
        exit.setOnClickListener(v-> fx.finishAffinity());

        agent.setOnClickListener(v->startActivity(new Intent(cx, ActivityAgentAccess.class)));

        return view;
    }
}
