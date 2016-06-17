package com.biin.biin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.biin.biin.Utils.BNUtils;
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
        setNavBarColor(R.color.colorAccentGray);
        setIndicatorColor(R.color.colorWhite, R.color.colorAccentGray);
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
        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(BNUtils.BNStringExtras.BNBiinie);
        editor.commit();

        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }
}
