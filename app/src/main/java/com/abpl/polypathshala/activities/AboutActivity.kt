package com.abpl.polypathshala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.ConfigModel
import com.abpl.polypathshala.utils.RemoteConfigUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    var aboutUs:String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
getRemoteConfigValues()
    }
    fun getRemoteConfigValues(){
        aboutUs = RemoteConfigUtils.getRemoteConfigValue(RemoteConfigUtils.ABOUT_US);
      webview.settings.javaScriptEnabled=true;
        webview.loadData(aboutUs,"text/html","UTF-8")
    }
}