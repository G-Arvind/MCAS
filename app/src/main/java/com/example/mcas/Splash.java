package com.example.mcas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Splash extends AwesomeSplash {


    @Override
    public void initSplash(ConfigSplash configSplash) {


        configSplash.setBackgroundColor(R.color.backgroundcolor);
        configSplash.setAnimCircularRevealDuration(1000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

        configSplash.setLogoSplash(R.drawable.logo);

        configSplash.setAnimLogoSplashDuration(1000);
        //configSplash.setAnimLogoSplashTechnique(Techniques.BounceInDown);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeInRight);
        configSplash.setOriginalHeight(10);
        configSplash.setOriginalWidth(10);


        configSplash.setTitleSplash("MCAS");
        configSplash.setTitleTextColor(R.color.colorPrimaryDark);
        configSplash.setAnimTitleDuration(1000);
        //configSplash.setTitleFont("font/monts.ttf");
        configSplash.setTitleTextSize(30f);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    }

    @Override
    public void animationsFinished() {
        startActivity(new Intent(Splash.this,MainActivity.class));
        finish();
    }



}
