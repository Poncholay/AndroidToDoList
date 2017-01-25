package com.poncholay.todolist;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;

import com.github.ybq.parallaxviewpager.ParallaxViewPager;
import com.poncholay.todolist.Activities.List.ListActivity;
import com.poncholay.todolist.controller.tab.TabsAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class OrientationChange {

	@Rule
	public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule<>(ListActivity.class);

	@Test
	public void testOrientationChange() throws Exception {
		Activity activity = mActivityRule.getActivity();
		ParallaxViewPager mParallaxViewPager;
		TabsAdapter tabsAdapter;

		mParallaxViewPager = (ParallaxViewPager) activity.findViewById(R.id.viewpager);
		tabsAdapter = (TabsAdapter) mParallaxViewPager.getAdapter();
		if (tabsAdapter.getCount() != 3) {
			throw new Exception("Adapter is uncomplete");
		}
		List<Fragment> list = new ArrayList<>();
		list.add(tabsAdapter.getItem(0));
		list.add(tabsAdapter.getItem(1));
		list.add(tabsAdapter.getItem(2));

		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getInstrumentation().waitForIdleSync();
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getInstrumentation().waitForIdleSync();

		mParallaxViewPager = (ParallaxViewPager) activity.findViewById(R.id.viewpager);
		tabsAdapter = (TabsAdapter) mParallaxViewPager.getAdapter();
		if (tabsAdapter.getCount() != 3) {
			throw new Exception("Adapter is uncomplete");
		}
		List<Fragment> listOther = new ArrayList<>();
		listOther.add(tabsAdapter.getItem(0));
		listOther.add(tabsAdapter.getItem(1));
		listOther.add(tabsAdapter.getItem(2));
		for (int i = 0; i < listOther.size(); i++) {
			Fragment fragment = list.get(i);
			Fragment other = listOther.get(i);
			if (!Objects.equals(fragment, other)) {
				throw new Exception("Fragments do not match");
			}
		}
	}
}
