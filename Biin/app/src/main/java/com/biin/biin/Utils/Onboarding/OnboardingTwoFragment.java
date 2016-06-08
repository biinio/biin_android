package com.biin.biin.Utils.Onboarding;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.biin.biin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingTwoFragment extends Fragment {


    public OnboardingTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding_two, container, false);
    }

}
