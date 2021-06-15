package com.abpl.polypathshala.activities

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.adapter.SubjectListAdapter
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.models.User
import com.abpl.polypathshala.services.SignupService
import com.abpl.polypathshala.services.SubjectService
import com.abpl.polypathshala.storage.StorePrefrence
import com.abpl.polypathshala.utils.CommonUtils
import com.abpl.polypathshala.utils.OnItemClick
import com.downloader.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_subject_list.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class SubjectListActivity : BaseActivity(), OnItemClick {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: SubjectListAdapter? = null
    var mList: MutableList<SubjectModel> = ArrayList()
    private var mStorageRef: StorageReference? = null
    val PERMISSIONS_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_list)
        mContext=this;
        mStorageRef = FirebaseStorage.getInstance().reference

        val config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build()
        PRDownloader.initialize(applicationContext, config)

        setUpRecyclerView()
        getSubjectList();
    }

    fun getSubjectList(){
        showLoading()
      val id =  SignupService.instance?.userAuthRefrence?.currentUser!!.uid
        SignupService.instance?.databasepRefrence?.document(id)?.addSnapshotListener { documentSnapshot, e ->
            try {
                val model: User? = documentSnapshot!!.data?.let { CommonUtils.getUserFromHaspMap(it) }
                StorePrefrence.user=model
                SubjectService.getDataBaseReference("${model?.semester}-${model?.trade}")?.addSnapshotListener { queryDocumentSnapshots, e ->
                    hideLoading()
                    mList = ArrayList()
                    for (snapShot: DocumentSnapshot in queryDocumentSnapshots!!.documents) {
                        var user:SubjectModel? = snapShot?.data?.let { CommonUtils.getSubjectFromHaspMap(it) }
                        user?.let { mList.add(it) }
                    }
                    if (adapter != null) {
                        adapter!!.clear()
                        adapter!!.addAllItem(mList)
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }catch (e: Exception){
                hideLoading()
            }


        }

    }
    fun setUpRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        subjectList.layoutManager = layoutManager

        adapter = SubjectListAdapter(this, this)
        subjectList.adapter = adapter

    }

    override fun onItemClick(data: SubjectModel, position: Int) {
        showLoading("Downloading File ...")

        if(position==0){
            mStorageRef?.child(data.hinFile)?.downloadUrl?.addOnSuccessListener {
                // Got the download URL for 'users/me/profile.png'
                it.path?.let { it1 -> downlodPermission(it.toString(), mContext) }
            }?.addOnFailureListener {
hideLoading()
            }
        }else{
            mStorageRef?.child(data.engFile)?.downloadUrl?.addOnSuccessListener {
                it.path?.let { it1 -> downlodPermission(it.toString(), mContext) }
            }?.addOnFailureListener {
hideLoading()
            }
        }

       // downloadFile()
    }
    @AfterPermissionGranted(RC_FILE_PICKER_PERM_)
    private fun downlodPermission(url: String, context: Context) {
        if (EasyPermissions.hasPermissions(this, FilePickerConst.PERMISSIONS_FILE_PICKER,PERMISSIONS_WRITE)) {
            downloadFile(url,context)
        } else {
            hideLoading()
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_doc_picker),
                    RC_FILE_PICKER_PERM_, FilePickerConst.PERMISSIONS_FILE_PICKER,PERMISSIONS_WRITE)
        }
    }

    fun downloadFile(url: String, context: Context){
        val dirPath=CommonUtils.getRootDirPath(context)
        val fileName=CommonUtils.getFileNameFromURL(url)
        PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener { }
                .setOnPauseListener { }
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel() {
                        hideLoading()
                    }
                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {}
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        hideLoading()

                        //showNotification(context, "Download complete", "File downloaded successfully");
                        dirPath?.let { fileName?.let { it1 -> open(it, it1) } }
                    }

                    override fun onError(error: Error?) {
                        hideLoading()

                    }


                })

    }
    private fun open(dir:String,filename:String){
        val root = File(dir+"/"+filename)
         var data:Uri = FileProvider.getUriForFile(mContext, "com.abpl.polypathshala.myprovider", root);
        grantUriPermission(mContext.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
         var intent :Intent=  Intent(Intent.ACTION_VIEW)
                .setDataAndType(data, "*/*")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "p5m_" +
                    "channel_" + System.currentTimeMillis() // The id of the channel.
            val name: CharSequence = "p5m" // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationManager.createNotificationChannel(mChannel)
            val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_app_round)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.ic_launcher_app_round))
                    .setChannelId(CHANNEL_ID)
                    .setContentText(message)
                    .build()
            notificationManager.notify((System.currentTimeMillis() - 10000000).toInt(), notification)
        } else {
            val bigTextStyle = Notification.BigTextStyle()
            bigTextStyle.bigText(message)
            val notification: Notification = Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher_app_round)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.ic_launcher_app_round))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentText(message)
                    .build()
            notificationManager.notify((System.currentTimeMillis() - 10000000).toInt(), notification)
        }
    }

    companion object {
        const val RC_FILE_PICKER_PERM_=1000;

    }
}