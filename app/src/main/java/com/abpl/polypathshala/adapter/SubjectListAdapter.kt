package com.abpl.polypathshala.adapter

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.recyclerview.widget.RecyclerView
import com.abpl.polypathshala.R
import com.abpl.polypathshala.models.SubjectModel
import com.abpl.polypathshala.utils.CommonUtils
import com.abpl.polypathshala.utils.OnItemClick
import com.downloader.*
import java.util.*


class SubjectListAdapter(mContext: Context,callback:OnItemClick) :
    RecyclerView.Adapter<SubjectListAdapter.MyViewHolder>() {
    var mLits: MutableList<SubjectModel> = ArrayList()
    private var mContext: Context
    var mCallback: OnItemClick

    var VIEW_TYPE_ITEM = 1
    val FOLDER_PATH="gs://poly-pathshala.appspot.com/"
    fun clear() {
        mLits.clear()
        notifyDataSetChanged()
    }

    fun addItem(model: SubjectModel) {
        mLits.add(model)
        notifyDataSetChanged()
    }

    fun removeItem(index: Int) {
        mLits.removeAt(index)
        notifyItemRemoved(index)
    }

    fun addAllItem(models: List<SubjectModel>?) {
        mLits.addAll(models!!)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_subject_layout, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, i: Int) {
        viewHolder.txtSubjectName.text = mLits[i].name
        viewHolder.txtHindiFile.text = "View";
        viewHolder.txtEngFile.text = "View"
        viewHolder.txtHindiFile.setOnClickListener {
            downloadFile("$FOLDER_PATH${mLits[i].hinFile}",viewHolder.itemView.context)
            mCallback.onItemClick(mLits[i],0);
        }
        viewHolder.txtEngFile.setOnClickListener {
            downloadFile("$FOLDER_PATH${mLits[i].engFile}",viewHolder.itemView.context)
            mCallback.onItemClick(mLits[i],1);

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
                    override fun onCancel() {}
                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {}
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
               showNotification(context,"Download complete","File downloaded successfully");
                    }

                    override fun onError(error: Error?) {
                    }


                })

    }

    private fun showNotification(context:Context, title: String, message: String) {
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


    override fun getItemCount(): Int {
        return mLits.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtSubjectName: TextView
        var txtHindiFile: TextView
        var txtEngFile: TextView

        init {
            txtSubjectName = itemView.findViewById(R.id.txtSubjectName)
            txtHindiFile = itemView.findViewById(R.id.txtHindiFile)
            txtEngFile = itemView.findViewById(R.id.txtEngFile)


        }
    }

    init {
        this.mContext = mContext
        this.mCallback = callback;
    }
}