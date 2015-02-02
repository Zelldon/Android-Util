/*
 * Copyright (C) 2015 Christopher Zell <zelldon91@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.zell.android.util.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import de.zell.android.util.R;

/**
 * The splash screen of the application.
 *
 * @author Christopher Zell <zelldon91@googlemail.com>
 */
public abstract class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_view);
        ImageView iv = (ImageView) findViewById(R.id.splash_image);
        
        iv.setAnimation(getAnimation());
        
        startAsyncTasks();
        
        new Handler().postDelayed(new Runnable() {

            public void run() {
                Intent i = new Intent(SplashActivity.this, getMainClass());
                startActivity(i);

                finish();
            }
        }, getSplashTime());
    }
    
    /**
     * Returns the class which should be started after the splash screen.
     * 
     * @return the main class which will be started
     */
    protected abstract Class getMainClass();

    /**
     * Contains asynchronous tasks which will be started in the splash activity
     * to initialize the application.
     */
    protected abstract void startAsyncTasks();
    
    /**
     * Returns the Animation for the splash screen.
     * 
     * @return the splash screen animation
     */
    protected abstract Animation getAnimation();
    
    /**
     * Returns the time how long the splash screen is shown in ms. 
     * That means 2500l = 2.5 sec
     * 
     * @return the time
     */
    protected abstract long getSplashTime();
}
