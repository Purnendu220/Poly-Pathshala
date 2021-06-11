package com.abpl.polypathshala.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.ConfigModel
import com.abpl.polypathshala.models.ConfigModelItem
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.ApiConstants
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.RemoteConfigUtils
import com.google.android.gms.tasks.OnCompleteListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import kotlin.collections.ArrayList


class SignUpActivity : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    var mSignupService: SignupService? = null
   lateinit var userType: String ;
    private var trade = ArrayList(Arrays.asList("DCE", "DM", "DECE", "DCE", "DEE"))
    var semester = ArrayList(Arrays.asList("Semester 1", "Semester 2", "Semester 3", "Semester 4", "Semester 5", "Semester 6", "Semester 7", "Semester 8"))
    var selectedSemester: String? = null
    var selectedTrade: String? = null
    private lateinit var semesterList: ConfigModel
    private lateinit var tradelist: ConfigModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        mSignupService = SignupService.instance
        RemoteConfigUtils.fetchRemoteConfig(this)


        buttonNormalUser.setOnClickListener(this)
        buttonAdminUser.setOnClickListener(this)
        buttonNext.setOnClickListener(this)
        textViewLogin.setOnClickListener(this)
        getRemoteConfigValues()
        if(mSignupService!!.getauthRefrence().currentUser!=null){
            startNewActivity(HomeActivity::class.java)

        }



    }
    fun getRemoteConfigValues(){
        val semString = RemoteConfigUtils.getRemoteConfigValue(RemoteConfigUtils.SEMESTER);
        val tradeString = RemoteConfigUtils.getRemoteConfigValue(RemoteConfigUtils.TRADES);
        val g = Gson()
       tradelist = g.fromJson(tradeString, object : TypeToken<ConfigModel?>() {}.type)
        semesterList = g.fromJson(semString, object : TypeToken<ConfigModel?>() {}.type)
        setSpinnerViewSemester()
        setSpinnerViewTrade()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.buttonNext -> {
                registerUser()
            }
            R.id.textViewLogin -> {
                startNewActivity(LoginActivity::class.java)

            }
            R.id.buttonNormalUser -> {
                userType = ApiConstants.Refrences.USER_NORMAL
                buttonNormalUser.setBackgroundResource(R.drawable.button_blue)
                buttonAdminUser.setBackgroundResource(R.drawable.button_white)
                buttonNormalUser.setTextColor(resources.getColor(R.color.white))
                buttonAdminUser.setTextColor(resources.getColor(R.color.black))

                textViewUserError.visibility = GONE

            }
            R.id.buttonAdminUser -> {
                userType = ApiConstants.Refrences.USER_ADMIN
                buttonAdminUser.setBackgroundResource(R.drawable.button_blue)
                buttonNormalUser.setBackgroundResource(R.drawable.button_white)
                buttonNormalUser.setTextColor(resources.getColor(R.color.black))
                buttonAdminUser.setTextColor(resources.getColor(R.color.white))

                textViewUserError.visibility = GONE

            }
        }
    }

    private fun registerUser() {
        if(this.editTextName.text.toString().isEmpty()){
            textInputLayoutName.error="Please provide first name"
            return
        }
        if(this.editTextLastName.text.toString().isEmpty()){
            textInputLayoutLastName.error="Please provide last name"
            return
        }
        if(this.editTextEmail.text.toString().isEmpty()){
            textInputLayoutEmail.error="Please provide email"
            return
        }
        if(this.editTextMobile.text.toString().isEmpty()){
            textInputLayoutMobile.error="Please provide mobile"
            return
        }
        if(this.editTextPass.text.toString().isEmpty()){
            textInputLayoutPass.error="Please provide password"
            return
        }
        if(this.editTextConfirmPass.text.toString().isEmpty()){
            textInputLayoutConfirmPass.error="Please provide confirm password"
            return
        }
        if(!this.editTextPass.text.toString().equals(this.editTextConfirmPass.text.toString(), false)){
            textInputLayoutConfirmPass.error="Confirm Password must be same"
        return
        }
        if(userType.isNullOrEmpty()){
            textViewUserError.visibility=VISIBLE
            return
        }
        if(selectedSemester?.isNullOrEmpty() == true){
            Toast.makeText(this, "Please Select Semester", Toast.LENGTH_LONG).show()
       return
        }
        if(selectedTrade?.isNullOrEmpty() == true){
            Toast.makeText(this, "Please Select Trade", Toast.LENGTH_LONG).show()
        return
        }

        var fisrtName=this.editTextName.text.toString();
        var lastName=this.editTextLastName.text.toString();
        var email=this.editTextEmail.text.toString();
        var mobile=this.editTextMobile.text.toString();
        var password=this.editTextPass.text.toString();
        showLoading()

        mSignupService!!.getauthRefrence().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = mSignupService!!.getauthRefrence().currentUser!!.uid
                val data = User(id, fisrtName, lastName, email, mobile, password, selectedSemester, selectedTrade, userType)
                mSignupService!!.databasepRefrence.document(id).set(data).addOnCompleteListener(OnCompleteListener<Void?> { task ->
                    hideLoading()

                    if (task.isSuccessful) {
                        StorePrefrence.user = data;
                        if (data.userType == ApiConstants.Refrences.USER_ADMIN) {
                            startNewActivity(AdminActivity::class.java)
                        } else {
                            startNewActivity(HomeActivity::class.java)
                        }
                        finish()
                    } else {
                        val error = task.exception!!.message
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                hideLoading()

                val error = task.exception!!.message
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setSpinnerViewSemester() {
        semester?.clear()
       semesterList.forEach {
           semester.add(it.dispalyName)
       }

        spinnerSemesterSignup.onItemSelectedListener = this
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                this, R.layout.view_spinner_item, semester!! as List<String?>) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }


        }
        spinnerSemesterSignup.adapter = spinnerArrayAdapter
        spinnerSemesterSignup.setSelection(0)
    }
    private fun setSpinnerViewTrade() {
        trade?.clear()
        tradelist.forEach {
            trade.add(it.dispalyName)
        }
        spinnerTradeSignup.onItemSelectedListener = this
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                this, R.layout.view_spinner_item, trade!! as List<String?>) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }


        }
        spinnerTradeSignup.adapter = spinnerArrayAdapter
        spinnerTradeSignup.setSelection(0)
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when (p0?.id) {
            R.id.spinnerSemesterSignup -> {
                if (p2 ==0) {
                  return
                }
                    selectedSemester = semesterList[p2].id

            }
            R.id.spinnerTradeSignup -> {
                if (p2 ==0) {
                    return
                }
                selectedTrade = tradelist[p2].id

            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}