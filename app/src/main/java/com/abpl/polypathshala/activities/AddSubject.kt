package com.abpl.polypathshala.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.ConfigModel
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.services.SubjectService
import com.abpl.polypathshala.utils.RemoteConfigUtils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.FilePickerConst.PERMISSIONS_FILE_PICKER
import kotlinx.android.synthetic.main.activity_add_subject.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class AddSubject : BaseActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private var trade = arrayListOf<String>()
    var semester = arrayListOf<String>()
    var selectedSemester: String? = ""
    var selectedTrade: String? = ""
    private lateinit var semesterList: ConfigModel
    private lateinit var tradelist: ConfigModel

    private val FILE_REQUEST_CODE_ENG=1;
    private val FILE_REQUEST_CODE_HIN=2;

    private var mStorageRef: StorageReference? = null

    lateinit var filesEngUploadUrl: String
    lateinit var filesHinUploadUrl: String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject)
        mContext=this
        addSubject.setOnClickListener(this)
        picFileEnglish.setOnClickListener(this)
        picFileHindi.setOnClickListener(this)
        mStorageRef = FirebaseStorage.getInstance().reference

        getRemoteConfigValues()


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
    private fun setSpinnerViewSemester() {
        semester?.clear()
        semesterList.forEach {
            semester.add(it.dispalyName)
        }

        spinnerSemester.onItemSelectedListener = this
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                this, R.layout.view_spinner_item, semester!! as List<String?>
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }


        }
        spinnerSemester.adapter = spinnerArrayAdapter
        spinnerSemester.setSelection(0)
    }
    private fun setSpinnerViewTrade() {
        trade?.clear()
        tradelist.forEach {
            trade.add(it.dispalyName)
        }
        spinnerTrade.onItemSelectedListener = this
        val spinnerArrayAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                this, R.layout.view_spinner_item, trade!! as List<String?>
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }


        }
        spinnerTrade.adapter = spinnerArrayAdapter
        spinnerTrade.setSelection(0)
    }
    fun addSubject(){
        var subjectName = editTextSubjectName.text.toString();

        if(selectedSemester?.isEmpty()!!){
            Toast.makeText(mContext, "Please select semester", Toast.LENGTH_LONG).show()
            return
        }
        if(selectedTrade?.isEmpty()!!){
            Toast.makeText(mContext, "Please select trade", Toast.LENGTH_LONG).show()
            return
        }
        if(subjectName?.isEmpty()!!){
            Toast.makeText(mContext, "Please enter subject name", Toast.LENGTH_LONG).show()
        return
        }

        if(!this::filesEngUploadUrl.isInitialized||filesEngUploadUrl?.isEmpty()!!){
            Toast.makeText(mContext, "Please select english file", Toast.LENGTH_LONG).show()
      return
        }
        if(!this::filesHinUploadUrl.isInitialized||filesHinUploadUrl?.isEmpty()!!){
            Toast.makeText(mContext, "Please select hindi file", Toast.LENGTH_LONG).show()
       return
        }
        showLoading()

        SubjectService.getDataBaseReference("$selectedSemester-$selectedTrade").document().set(SubjectModel(
                selectedTrade!!, selectedSemester!!, subjectName, filesEngUploadUrl, filesHinUploadUrl)).addOnCompleteListener {
                hideLoading()
                if(it.isSuccessful){
                    Toast.makeText(this, "Subject added successfully", Toast.LENGTH_LONG).show()
                    finish()

                }else{
                    val error = it.exception!!.message
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                }
        }

    }

    override fun onClick(p0: View?) {
    when(p0?.id){
        R.id.addSubject -> {
            addSubject()
        }
        R.id.picFileEnglish -> {
            pickEngClicked()
        }
        R.id.picFileHindi -> {

            pickHinClicked()
        }
    }
    }
    fun onPickHindi(){
        FilePickerBuilder.instance
                .setMaxCount(1) //optional
                .pickFile(this, FILE_REQUEST_CODE_HIN);
    }
    fun onPickEnglish(){
        FilePickerBuilder.instance
                .setMaxCount(1) //optional
                .pickFile(this, FILE_REQUEST_CODE_ENG);
    }

    @AfterPermissionGranted(RC_PHOTO_PICKER_PERM_ENG)
    private fun pickEngClicked() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS_FILE_PICKER)) {
            onPickEnglish()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_PHOTO_PICKER_PERM_ENG, PERMISSIONS_FILE_PICKER)
        }
    }

    @AfterPermissionGranted(RC_PHOTO_PICKER_PERM_HIN)
    private fun pickHinClicked() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS_FILE_PICKER)) {
            onPickHindi()
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_PHOTO_PICKER_PERM_HIN, PERMISSIONS_FILE_PICKER)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p0?.id){
            R.id.spinnerTrade -> {
                if(p2==0){
                    return

                }
                selectedTrade = tradelist[p2].id
            }
            R.id.spinnerSemester -> {
                if(p2==0){
                    return

                }
                selectedSemester = semesterList[p2].id
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    fun uploadFile(file: Uri, type: Int){
        showLoading("Uploading file ...")
        val pathTostore = "files/" + System.currentTimeMillis() + "_poly"
        val riversRef = mStorageRef!!.child(pathTostore)
        if(type==FILE_REQUEST_CODE_HIN){
            fileHindi.text="Uploading file ..."

        }
        if(type==FILE_REQUEST_CODE_ENG){
            fileEnglish.text="Uploading file ..."

        }
        riversRef.putFile(file).addOnSuccessListener {
            hideLoading()
            if(type==FILE_REQUEST_CODE_HIN){
                filesHinUploadUrl=pathTostore;
                fileHindi.text="Uploaded"

            }
            if(type==FILE_REQUEST_CODE_ENG){
                filesEngUploadUrl=pathTostore;
                fileEnglish.text="Uploaded"

            }
        }
            .addOnFailureListener{
                hideLoading()
                Toast.makeText(mContext, it.message, Toast.LENGTH_LONG).show()

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode!=RESULT_OK||data==null){
            return
        }
        when(requestCode){
            FILE_REQUEST_CODE_HIN -> {

                var selectedFile = data?.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)

                selectedFile?.get(0)?.let { uploadFile(it, FILE_REQUEST_CODE_HIN) };

            }
            FILE_REQUEST_CODE_ENG -> {
                var selectedFile = data?.getParcelableArrayListExtra<Uri>(FilePickerConst.KEY_SELECTED_DOCS)

                selectedFile?.get(0)?.let { uploadFile(it, FILE_REQUEST_CODE_ENG) };

            }
        }
    }

    companion object {
         const val RC_PHOTO_PICKER_PERM_ENG=1000;
         const val RC_PHOTO_PICKER_PERM_HIN=1001;

    }
}