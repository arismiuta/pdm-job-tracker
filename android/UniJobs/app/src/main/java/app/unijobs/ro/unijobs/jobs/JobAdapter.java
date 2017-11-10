package app.unijobs.ro.unijobs.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import app.unijobs.ro.unijobs.R;

/**
 * Created by Maniga on 11/9/2017.
 */

public class JobAdapter extends ArrayAdapter<Job> {
    private static List<Job> itemsList;
    private Context context;
    private int resLayout;

    public JobAdapter(Context context, int resource, List<Job> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resLayout = resource;
        this.itemsList = objects;
    }

    public static List<Job> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Job> itemsList) {
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, resLayout, null);

        TextView jobTitleTextView = (TextView) view.findViewById(R.id.jobTitle);
        TextView jobDescriptionTextView = (TextView) view.findViewById(R.id.jobDescription);
        TextView jobPayTextView = (TextView) view.findViewById(R.id.jobPay);

        Job currentJob = itemsList.get(position);

        jobTitleTextView.setText(currentJob.getTitle());
        jobDescriptionTextView.setText(currentJob.getDescription());
        jobPayTextView.setText(currentJob.getPay());

        return view;
    }

}
