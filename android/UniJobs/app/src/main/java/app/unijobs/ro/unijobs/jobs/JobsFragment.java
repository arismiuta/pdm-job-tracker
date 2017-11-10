package app.unijobs.ro.unijobs.jobs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.unijobs.ro.unijobs.R;

/**
 * Created by Maniga on 11/9/2017.
 */

public class JobsFragment extends Fragment {
    private List<Job> jobsList;


    public JobsFragment() {
        jobsList = new ArrayList<>();
        jobsList.add(new Job("Groceries", "Buy a list of groceries from the nearest supermarket.", "40 RON"));
        jobsList.add(new Job("Sewing clothes", "Sew a couple pair of jeans.", "20 RON"));
        jobsList.add(new Job("Dish washing", "Part-time dish washing after restaurant closes.", "60 RON"));
        jobsList.add(new Job("Clothes washing", "Wash some bag of clothes at the nearest wash shop.", "30 RON"));
        jobsList.add(new Job("Cleaning", "Clean an old an dusty flat.", "200 RON"));
        jobsList.add(new Job("Private request", "Sign a confidentiality clause and have a private anonymus chat with the requester.", "500 RON"));
        jobsList.add(new Job("Data scraping", "Scrape all emails from Romania's Goverment Website.", "2000 RON"));
        jobsList.add(new Job("Mobile App", "Make a simple android app.", "1500 RON"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.jobs_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ListView jobsListView = (ListView) view.findViewById(R.id.jobsListId);

        jobsListView.setAdapter(new JobAdapter(getContext(), R.layout.jobs_list, jobsList));

        jobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                DetailsFragment jobDetailsFragment = new DetailsFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("position", (int) id);

                jobDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.main_fragment, jobDetailsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

}
