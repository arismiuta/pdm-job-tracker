package app.unijobs.ro.unijobs_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import app.unijobs.ro.unijobs_android.domain.Job;

public class AddJobActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    NumberPicker jobPay;

    int pickerValue = 0;

    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        title = (EditText) findViewById(R.id.addJobTitle);
        description = (EditText) findViewById(R.id.addJobDescription);
        jobPay = (NumberPicker) findViewById(R.id.addJobPay);
        addButton = (Button) findViewById(R.id.addButton);

        jobPay.setMinValue(0);
        jobPay.setMaxValue(1000);

        jobPay.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                pickerValue = i1;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Job job = new Job(title.getText().toString(), description.getText().toString(), pickerValue);
                JobsActivity.jobRepository.add(job);
                Intent rIntent = new Intent();
                setResult(RESULT_OK, rIntent);
                finish();
            }
        });

    }
}
