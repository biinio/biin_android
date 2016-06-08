package com.biin.biin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biin.biin.Utils.Onboarding.OnboardingFourFragment;
import com.biin.biin.Utils.Onboarding.OnboardingOneFragment;
import com.biin.biin.Utils.Onboarding.OnboardingThreeFragment;
import com.biin.biin.Utils.Onboarding.OnboardingTwoFragment;
import com.github.paolorotolo.appintro.AppIntro;

public class OnboardingActivity extends AppIntro {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new OnboardingOneFragment());
        addSlide(new OnboardingTwoFragment());
        addSlide(new OnboardingThreeFragment());
        addSlide(new OnboardingFourFragment());

        showSkipButton(false);
        setNavBarColor(R.color.colorAccent);
        setIndicatorColor(R.color.colorWhite, R.color.colorAccent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
    }

    @Override
    public void onBackPressed() {
        returnToSignUp();
    }

    private void returnToSignUp(){
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }
}
