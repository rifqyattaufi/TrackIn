package com.example.trackin.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.trackin.MainActivity
import com.example.trackin.R

private const val VIBRATE_PERMISSION_REQUEST_CODE = 123

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val notificationId = intent?.getIntExtra(NOTIFICATION_ID_EXTRA.toString(), 0)
            if (notificationId != null && notificationId != 0) {
                showNotification(context!!, notificationId, "Schedule Name")
            }
        }
    }

    private fun showNotification(context: Context, notificationId: Int, scheduleName: String) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.VIBRATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createNotificationChannel(context)

            val channelId = "$CHANNEL_ID-$notificationId"

            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            val pendingIntent = PendingIntent.getActivity(
                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(scheduleName)
                .setContentText("Your Class Will Start in 30 Minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.VIBRATE),
                VIBRATE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Your Channel Name"
            val descriptionText = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }

    companion object {
        const val CHANNEL_ID = "schedule_id"
        const val NOTIFICATION_ID_EXTRA = 0
    }
}