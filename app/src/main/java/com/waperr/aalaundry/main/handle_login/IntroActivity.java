package com.waperr.aalaundry.main.handle_login;

/**
 * Created by farhan on 10/23/16.
 */

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.waperr.aalaundry.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(false);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });
        //intro 1
        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.splashscreen1)
                .backgroundColor(R.color.colorIntro1)
                .buttonsColor(R.color.colorButtonIntro)
                .title("Harga mulai Rp. 4000,-")
                .description("Murah kan?")
                .build());
        //intro 2
        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.splashscreen2)
                .backgroundColor(R.color.colorIntro2)
                .buttonsColor(R.color.colorButtonIntro)
                .title("Mudah digunakan dimana saja!")
                .description("Praktis kan?")
                .build());
        //intro 3
        addSlide(new SlideFragmentBuilder()
                .image(R.drawable.splashscreen3)
                .backgroundColor(R.color.colorIntro3)
                .buttonsColor(R.color.colorButtonIntro)
                .title("Jemput dalam 60 menit!")
                .description("Mau cobain?")
                .build());

    }

    @Override
    public void onFinish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {

    }
}