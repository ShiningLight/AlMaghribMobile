package com.almaghrib.mobile.instructors;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.instructors.jsonModels.InstructorModelContainer;
import com.almaghrib.mobile.util.AssetUtils;
import com.google.gson.Gson;

public class OurInstructorsActivity extends FragmentActivity implements
        AdapterView.OnItemSelectedListener {

    private CharSequence[] mInstructorNames;
    private CharSequence[] mInstructorProfileFiles;
    private CharSequence[] mInstructorPics;

    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.our_instructors);

        mInstructorNames = getResources().getTextArray(R.array.instructor_names);
        mInstructorProfileFiles = getResources().getTextArray(R.array.instructor_profile_json_files);
        mInstructorPics = getResources().getTextArray(R.array.instructor_images);

        mSpinner = (Spinner) findViewById(R.id.instructorSpinner);
        mSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        retrieveInfoAndPopulateInstructorViews(position);
    }

    private void retrieveInfoAndPopulateInstructorViews(int position) {
        final String profilePath = getString(R.string.instructor_profile_directory) + "/" +
                mInstructorProfileFiles[position];
        final String profileJsonString = AssetUtils.readFileFromAssets(
                getApplicationContext(), profilePath);
        if (profileJsonString != null) {
            final Gson gson = new Gson();
            final InstructorModelContainer instructorModel = gson.fromJson(
                    profileJsonString, InstructorModelContainer.class);
            populateInstructorViews(position, instructorModel);
        }
    }

    private void populateInstructorViews(int position, InstructorModelContainer instructorModel) {
        if (instructorModel != null) {
            final ImageView pic = (ImageView) findViewById(R.id.instructorImageView);
            final int picResourseId = getResources().getIdentifier(mInstructorPics[position].toString(), "drawable", getPackageName());
            pic.setImageResource(picResourseId);

            final TextView nameView = (TextView) findViewById(R.id.nameTextView);
            nameView.setText(instructorModel.getName());

            final TextView roleView = (TextView) findViewById(R.id.roleTextView);
            roleView.setText(instructorModel.getRole());

            final TextView cityView = (TextView) findViewById(R.id.cityTextView);
            cityView.setText(instructorModel.getCity());

            final TextView descriptionView = (TextView) findViewById(R.id.profileTextView);
            descriptionView.setText(instructorModel.getDescription());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
