package app.unijobs.ro.unijobs.jobs;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import app.unijobs.ro.unijobs.R;

/**
 * Created by Maniga on 11/9/2017.
 */

public class DetailsFragment extends Fragment {
    private Job currentJob;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        int position = 0;

        if (bundle != null) {
            position = bundle.getInt("position");
        }
        currentJob = JobAdapter.getItemsList().get(position);

        return inflater.inflate(R.layout.details_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editJobTitle = (EditText) view.findViewById(R.id.editJobTitle);
        final EditText editJobDescription = (EditText) view.findViewById(R.id.editJobDescription);
        final EditText editJobPay = (EditText) view.findViewById(R.id.editJobPay);

        editJobTitle.setText(currentJob.getTitle());
        editJobDescription.setText(currentJob.getDescription());
        editJobPay.setText(currentJob.getPay());

        final String email_text = "I am reaching out regarding this job: " + this.currentJob.getTitle();

        Button sendFeedbackButton = (Button) view.findViewById(R.id.sendSysAdminButton);
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGmail(getActivity(), new String[]{"syadmin@unijobs.com"}, "Hello SysAdmin!", email_text);
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentJob.setTitle(String.valueOf(editJobTitle.getText()));
                currentJob.setDescription(String.valueOf(editJobDescription.getText()));
                currentJob.setPay(String.valueOf(editJobPay.getText()));
                Toast.makeText(getContext(), "Saved new job details for: " + currentJob.getTitle() , Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStackImmediate();
            }
        });
    }

    public static void openGmail(Activity activity, String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for(final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        activity.startActivity(emailIntent);
    }
}
