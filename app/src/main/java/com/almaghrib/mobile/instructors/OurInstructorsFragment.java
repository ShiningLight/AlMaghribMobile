package com.almaghrib.mobile.instructors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaghrib.mobile.R;
import com.almaghrib.mobile.instructors.jsonModels.InstructorModelContainer;
import com.almaghrib.mobile.util.AssetUtils;
import com.google.gson.Gson;

public class OurInstructorsFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    private CharSequence[] mInstructorNames;
    private CharSequence[] mInstructorProfileFiles;
    private CharSequence[] mInstructorPics;

    private Spinner mSpinner;

    public OurInstructorsFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.our_instructors, container, false);

        mInstructorNames = getResources().getTextArray(R.array.instructor_names);
        mInstructorProfileFiles = getResources().getTextArray(R.array.instructor_profile_json_files);
        mInstructorPics = getResources().getTextArray(R.array.instructor_images);

        mSpinner = (Spinner) layoutView.findViewById(R.id.instructorSpinner);
        mSpinner.setOnItemSelectedListener(this);

        return layoutView;
    }

    @Override
    public void onDestroy() {
        if (getActivity().getSupportFragmentManager().getFragments().contains(this)) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        }
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        retrieveInfoAndPopulateInstructorViews(position);
    }

    private void retrieveInfoAndPopulateInstructorViews(int position) {
        final String profilePath = getString(R.string.instructor_profile_directory) + "/" +
                mInstructorProfileFiles[position];
        final String profileJsonString = AssetUtils.readFileFromAssets(
                getActivity().getApplicationContext(), profilePath);
        if (profileJsonString != null) {
            final Gson gson = new Gson();
            final InstructorModelContainer instructorModel = gson.fromJson(
                    profileJsonString, InstructorModelContainer.class);
            populateInstructorViews(position, instructorModel);
        }
    }

    private void populateInstructorViews(int position, InstructorModelContainer instructorModel) {
        if (instructorModel != null) {
            final View view = getView();
            if (view != null) {
                final ImageView pic = (ImageView) view.findViewById(R.id.instructorImageView);
                final int picResourseId = getResources().getIdentifier(
                        mInstructorPics[position].toString(), "drawable", getActivity().getPackageName());
                pic.setImageResource(picResourseId);

                final TextView nameView = (TextView) view.findViewById(R.id.nameTextView);
                nameView.setText(instructorModel.getName());

                final TextView roleView = (TextView) view.findViewById(R.id.roleTextView);
                roleView.setText(instructorModel.getRole());

                final TextView cityView = (TextView) view.findViewById(R.id.cityTextView);
                cityView.setText(instructorModel.getCity());

                final TextView descriptionView = (TextView) view.findViewById(R.id.profileTextView);
                descriptionView.setText(instructorModel.getDescription());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
