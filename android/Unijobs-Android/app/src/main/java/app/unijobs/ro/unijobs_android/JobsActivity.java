package app.unijobs.ro.unijobs_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.unijobs.ro.unijobs_android.adapters.JobAdapter;
import app.unijobs.ro.unijobs_android.database.Database;
import app.unijobs.ro.unijobs_android.repository.JobRepository;

public class JobsActivity extends AppCompatActivity {

    private static final String TAG = "JobsActivity";
    Button signOutButton;
    Button addJobButton;
    ListView jobsList;
    static JobRepository jobRepository;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference jobsRef;

    static Context context;

    public static void toastObserverUpdate(){
        Toast.makeText(context, "Repository updated.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                jobsRef.setValue(jobRepository);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        context = getApplicationContext();

        jobsList = (ListView) findViewById(R.id.jobList);
        signOutButton = (Button) findViewById(R.id.signout_button);
        addJobButton = (Button) findViewById(R.id.add_job_button);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = Database.getDatabase();

        jobsRef = database.getReference("jobs");

        jobRepository = new JobRepository();

        jobsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue(JobRepository.class) != null) {
                    jobRepository.setJobs(dataSnapshot.getValue(JobRepository.class).getJobs());
                    jobsList.setAdapter(new JobAdapter(getApplicationContext(), R.layout.jobs_list, jobRepository.getJobs()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        jobsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                jobRepository.remove(i);
                                jobsRef.setValue(jobRepository);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(JobsActivity.this);
                ab.setMessage("Are you sure to delete this job?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });

        jobsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(JobsActivity.this, JobActivity.class);
                intent.putExtra("index", i);
                startActivityForResult(intent, 1);
            }
        });

        addJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobsActivity.this, AddJobActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                if(mAuth.getCurrentUser() == null){
                    Intent intent = new Intent(JobsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });

    }
}
