package app.unijobs.ro.unijobs;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.unijobs.ro.unijobs.jobs.JobsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        JobsFragment jobsFragment = new JobsFragment();
        fragmentTransaction.add(R.id.main_fragment, jobsFragment);
        fragmentTransaction.commit();
    }
}
