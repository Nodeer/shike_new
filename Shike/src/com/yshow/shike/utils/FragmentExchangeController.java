package com.yshow.shike.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

import com.yshow.shike.R;


public class FragmentExchangeController {
	public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
		replaceFragment(fragmentManager, targetFragment, tag, android.R.id.content);
	}
	public static void replaceFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(postion, targetFragment, tag);
		transaction.commitAllowingStateLoss();
	}
	public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag) {
		initFragment(fragmentManager, targetFragment, tag, android.R.id.content);
	}
	public static void initFragment(FragmentManager fragmentManager, Fragment targetFragment, String tag, int postion) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(postion, targetFragment, tag);
		transaction.commitAllowingStateLoss();
	}
	public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_close_in, R.anim.anim_close_out);
		Fragment fragment = supportFragmentManager.findFragmentById(content);
		if (fragment != null) {
			if (fragment instanceof OnBackStackChangedListener) {
				supportFragmentManager.addOnBackStackChangedListener((OnBackStackChangedListener) fragment);
			}
			transaction.hide(fragment);
		}
		transaction.add(content, baseDialogFragment, tag);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}

	public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag,int in,int out,int closein,int closeout) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.setCustomAnimations(in,out,closein,closeout);
		Fragment fragment = supportFragmentManager.findFragmentById(content);
		if (fragment != null) {
			if (fragment instanceof OnBackStackChangedListener) {
				supportFragmentManager.addOnBackStackChangedListener((OnBackStackChangedListener) fragment);
			}
			transaction.hide(fragment);
		}
		transaction.add(content, baseDialogFragment, tag);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public static void addWithoutAnimFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.add(content, baseDialogFragment, tag);
		transaction.addToBackStack(tag);
		transaction.commitAllowingStateLoss();
	}
	public static void addWithoutStackFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
		// TODO Auto-generated method stub
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.add(content, baseDialogFragment, tag);
		transaction.commitAllowingStateLoss();
	}
	public static void removeFragment(FragmentManager fragmentManager, String tag) {
		// TODO Auto-generated method stub
		if (fragmentManager != null) {
			try {
				if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
					fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
			if (targetFragment != null) {
				FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
				localFragmentTransaction.remove(targetFragment);
				localFragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
			}
		}
	}
	public static void removeFragment(FragmentManager fragmentManager, Fragment targetFragment) {
		// TODO Auto-generated method stub
		if (fragmentManager != null) {
			String tag = targetFragment.getTag();
			try {
				if (fragmentManager != null && fragmentManager.findFragmentByTag(tag) != null) {
					fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}
				FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
				localFragmentTransaction.remove(targetFragment);
				localFragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
			} catch (Exception e) {
				// TODO: handle exception
			}
			Fragment fragment = fragmentManager.findFragmentByTag(tag);
			if (fragment != null) {
				FragmentTransaction localFragmentTransaction = fragmentManager.beginTransaction();
				localFragmentTransaction.remove(fragment);
				localFragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
			}
		}
	}
}
