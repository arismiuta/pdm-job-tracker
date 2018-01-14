package app.unijobs.ro.unijobs_android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.List;

import app.unijobs.ro.unijobs_android.domain.Job;

public class JobActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    EditText jobPay;

    Button saveButton2;

    Job job;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job2);
        title = (EditText) findViewById(R.id.editJobTitle);
        description = (EditText) findViewById(R.id.editJobDescription);
        jobPay = (EditText) findViewById(R.id.editJobPay);
        saveButton2 = (Button) findViewById(R.id.saveButton);

        Intent intent = getIntent();

        index = intent.getIntExtra("index", -1);

        if(index != -1) {
            job = JobsActivity.jobRepository.getJobs().get(index);
            title.setText(job.getTitle());
            description.setText(job.getDescription());
            jobPay.setText(Integer.toString(job.getPay()));
        }

        saveButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                job.setTitle(title.getText().toString());
                job.setDescription(description.getText().toString());
                job.setPay(Integer.parseInt(jobPay.getText().toString()));

                JobsActivity.jobRepository.update(index, job);
                Intent rIntent = new Intent();
                setResult(RESULT_OK, rIntent);
                finish();
            }
        });

        Button sendFeedbackButton = (Button) findViewById(R.id.sendSysAdminButton);
        sendFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGmail(JobActivity.this, new String[]{"syadmin@unijobs.com"}, "Hello SysAdmin!", "About this job: " + title.getText().toString());
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            }
        });

        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);

        List<Job> jobs = JobsActivity.jobRepository.getJobs();

        mBarChart.addBar(new BarModel("Current job", jobs.get(index).getPay(), 0xFF123456));

        for (int i = 0; i < jobs.size(); i++) {
            if(i != index) {
                mBarChart.addBar(new BarModel("Other", jobs.get(i).getPay(),  0xFF1BA4E6));
            }
        }

        mBarChart.startAnimation();

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
