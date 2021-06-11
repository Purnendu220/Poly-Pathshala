package com.abpl.polypathshala.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.ApiConstants
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.services.SubjectService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.CommonUtils
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_login2.editTextEmail
import kotlinx.android.synthetic.main.activity_login2.textInputLayoutEmail
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    var mSignupService: SignupService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        mSignupService = SignupService.instance

        textViewSignUp.setOnClickListener(this)
        textViewForgetPassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
          R.id.textViewSignUp->{
              startNewActivity(SignUpActivity::class.java)

          }
            R.id.textViewForgetPassword->{
                startNewActivityWithoutFinish(ForgotPasswordActivity::class.java)

            }
            R.id.buttonLogin->{
                loginUser()

            }
        }
    }

    private fun loginUser() {

        if(this.editTextEmail.text.toString().isEmpty()){
            textInputLayoutEmail.error="Please provide email"
            return
        }

        if(this.editTextPassword.text.toString().isEmpty()){
            textInputLayoutPassword.error="Please provide password"
            return
        }

        var email=this.editTextEmail.text.toString();
        var password=this.editTextPass.text.toString();
showLoading()
        mSignupService!!.getauthRefrence().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = mSignupService!!.getauthRefrence().currentUser!!.uid
                SignupService.instance?.databasepRefrence?.document(id)?.addSnapshotListener { documentSnapshot, e ->
                    hideLoading()

                    val model: User? = documentSnapshot!!.data?.let { CommonUtils.getUserFromHaspMap(it) }
                    StorePrefrence.user = model
                    if (model?.userType == ApiConstants.Refrences.USER_ADMIN) {
                        startNewActivity(AdminActivity::class.java)
                    } else {
                        startNewActivity(HomeActivity::class.java)
                    }
                    finish()

                }

            } else {
                hideLoading()

                val error = task.exception!!.message
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

    }
}