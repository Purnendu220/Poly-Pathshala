package com.abpl.polypathshala.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abpl.polypathshala.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.buttonLogin
import kotlinx.android.synthetic.main.activity_forgot_password.editTextEmail
import kotlinx.android.synthetic.main.activity_forgot_password.textInputLayoutEmail
import kotlinx.android.synthetic.main.activity_login2.*


class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {
    var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        buttonLogin.setOnClickListener(this);

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.buttonLogin->{
                forgotPassword()
            }
        }
    }
    fun forgotPassword(){
        if(this.editTextEmail.text.toString().isEmpty()){
            textInputLayoutEmail.error="Please provide email"
            return
        }
        showLoading()
        var email=this.editTextEmail.text.toString();

        auth?.sendPasswordResetEmail(email)
            ?.addOnCompleteListener{
                hideLoading()
                    if (it.isSuccessful) {
                        textViewLinkSent.text="Email sent to reset password."
                    } else {
                        val error = it.exception!!.message
                        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    }


            }
    }
}