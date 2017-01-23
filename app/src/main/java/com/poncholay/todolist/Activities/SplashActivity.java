package com.poncholay.todolist.Activities;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.poncholay.todolist.Activities.List.ListActivity;
import com.poncholay.todolist.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

/**
 * Created by wilmot_g on 17/01/17.
 */

public class SplashActivity extends AwesomeSplash {

	@Override
	public void initSplash(ConfigSplash configSplash) {
		configSplash.setBackgroundColor(R.color.colorPrimary);
		configSplash.setAnimCircularRevealDuration(1000);
		configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
		configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

		configSplash.setLogoSplash(R.drawable.icon);
		configSplash.setAnimLogoSplashDuration(1000); //int ms
		configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn);

		configSplash.setTitleSplash("ToDoList");
		configSplash.setTitleTextColor(R.color.colorButton);
		configSplash.setTitleTextSize(30f);
		configSplash.setAnimTitleDuration(1000);
		configSplash.setAnimTitleTechnique(Techniques.DropOut);
		configSplash.setTitleFont("roboto.ttf");
	}

	@Override
	public void animationsFinished() {
		Intent launchApp = new Intent(this, ListActivity.class);
		this.startActivity(launchApp);
	}
}