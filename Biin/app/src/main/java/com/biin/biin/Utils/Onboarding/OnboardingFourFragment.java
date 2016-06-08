package com.biin.biin.Utils.Onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biin.biin.R;
import com.biin.biin.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnboardingFourFragment extends Fragment {

    public OnboardingFourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboarding_four, container, false);

        // Set the button click
        TextView tvStart = (TextView) view.findViewById(R.id.tvOnboardingStart);
        tvStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getContext(), SplashActivity.class);
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

}
