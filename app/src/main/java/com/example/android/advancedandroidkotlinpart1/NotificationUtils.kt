package com.example.android.advancedandroidkotlinpart1

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder

fun NotificationManager.sendDownloadNotification(messageBody: String, args: Bundle?, applicationContext: Context) {

    val pendingIntent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.downloadDetailsFragment)
        .setArguments(args)
        .createPendingIntent()

    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.download_channel_id))
        .setContentTitle("Download Complete")
        .setContentText(messageBody)
        .setSmallIcon(R.drawable.ic_cloud_download)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_cloud_download, "Show Me", pendingIntent)

    notify(0, builder.build())
}

fun NotificationManager.cancelNotifications(){
    cancelAll()
}