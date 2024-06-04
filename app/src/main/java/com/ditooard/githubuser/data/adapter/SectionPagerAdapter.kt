package com.ditooard.githubuser.data.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ditooard.githubuser.R
import com.ditooard.githubuser.data.fragment.FollowersFragment
import com.ditooard.githubuser.data.fragment.FollowingFragment

class SectionPagerAdapter(private val mCtx: Context, fm: FragmentManager, data: Bundle) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var bundleFragment: Bundle

    init {
        bundleFragment = data
    }

    @StringRes
    private val TAB = intArrayOf(R.string.tab_1, R.string.tab_2)

    override fun getCount(): Int = TAB.size
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.bundleFragment
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mCtx.resources.getString(TAB[position])
    }
}