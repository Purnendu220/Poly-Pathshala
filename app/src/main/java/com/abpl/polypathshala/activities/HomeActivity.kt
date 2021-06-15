package com.abpl.polypathshala.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.CommonUtils
import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class HomeActivity : BaseActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var user: User?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mContext=this;
        user= StorePrefrence.user
        subjectList.setOnClickListener(this)
        setupNavigationView()
        setUpAvatar()
    }
    fun setupNavigationView(){
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toolbar?.setNavigationIcon(R.drawable.ic_outline_dehaze)


        nav_view.setNavigationItemSelectedListener(this)
        nav_view.bringToFront();

    }

    fun setUpAvatar(){
        val imageInfo: NameInitialsCircleImageView.ImageInfo = NameInitialsCircleImageView.ImageInfo
            .Builder(CommonUtils.getInitials("${user?.fisrtName} ${user?.lastName}"))
            .setTextColor(android.R.color.primary_text_dark)
            .setCircleBackgroundColorRes(android.R.color.holo_orange_dark)
            .build()

        nav_view.getHeaderView(0).nav_header_imageView.setImageInfo(imageInfo)
        nav_view.getHeaderView(0).nav_header_textView.text="${user?.fisrtName} ${user?.lastName}"
        nav_view.getHeaderView(0).nav_sub_header_textView.text="${user?.semester} / ${user?.trade}"


    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.subjectList -> {
                startNewActivityWithoutFinish(SubjectListActivity::class.java)
            }

        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_item_one -> startNewActivityWithoutFinish(SubjectListActivity::class.java)
            R.id.nav_item_four -> {
                SignupService.instance?.userAuthRefrence?.signOut()
                startNewActivity(LoginActivity::class.java)
            }
            R.id.nav_item_two->{
                startNewActivity(AboutActivity::class.java)

            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}