package com.jakemadethis.pinball;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.jakemadethis.pinball.game.Pinball;

import android.os.Bundle;

public class MainActivity extends AndroidApplication {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(new Pinball(), true);
    }
}