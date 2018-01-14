package app.unijobs.ro.unijobs_android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.unijobs.ro.unijobs_android.adapters.JobAdapter;
import app.unijobs.ro.unijobs_android.repository.JobRepository;

public class GuestActivity extends AppCompatActivity {

    private static final String TAG = "GuestActivity";
    Button signOutButton;
    Button addJobButton;
    ListView jobsList;
    static JobRepository jobRepository;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference jobsRef;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously();

        context = getApplicationContext();

        jobsList = (ListView) findViewById(R.id.jobList);
        signOutButton = (Button) findViewById(R.id.signout_button);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        jobsRef = database.getReference("jobs");

        jobRepository = new JobRepository();

        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue(JobRepository.class) != null) {
                    jobRepository.setJobs(dataSnapshot.getValue(JobRepository.class).getJobs().subList(0,3));
                    jobsList.setAdapter(new JobAdapter(getApplicationContext(), R.layout.jobs_list, jobRepository.getJobs()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                if(mAuth.getCurrentUser() == null){
                    Intent intent = new Intent(GuestActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
