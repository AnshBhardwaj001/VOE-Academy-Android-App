package com.voeacademy.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.voeacademy.application.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;

public class IntroSlider extends AppIntro {
    Button skip, done;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        skip = findViewById(com.github.appintro.R.id.skip);
        done = findViewById(com.github.appintro.R.id.done);
        done.setText("Get Started");
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });


        addSlide(AppIntroFragment.newInstance("VOE ACADEMY",
                "Lighting the Path of Knowledge\n" +
                        "\n" +
                        "Education - Character - Life",
                R.drawable.voe_academy_intro_slide_1,
                ContextCompat.getColor(getApplicationContext(),R.color.voe_color),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                R.font.poppins_black,
                R.font.poppins));


        addSlide(AppIntroFragment.newInstance("Our Teaching", "Our teaching style acts as a catalyst for creating excellence in the education sector and promoting the convergence of knowledge, Information, Technology & Skills.",
                R.drawable.voe_academy_intro_slide_2,  ContextCompat.getColor(getApplicationContext(),R.color.voe_color),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                R.font.poppins_black,
                R.font.poppins));
        addSlide(AppIntroFragment.newInstance("Why Us?", "One Stop Learning Solution for School Learning(K-10)& Language Learning\n" + "\nScholarship for SEBC whose family income is less than 1 lakh per annum.\n" + "\nFocus on Meta-Teaching Concepts.",
                R.drawable.voe_academy_intro_slide_3,  ContextCompat.getColor(getApplicationContext(),R.color.voe_color),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                ContextCompat.getColor(getApplicationContext(),R.color.white),
                R.font.poppins_black,
                R.font.poppins));

    }
}
