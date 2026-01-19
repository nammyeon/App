package com.example.step01

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM Log", "Refreshed token: $token")
    }


    private fun sendNotification(title: String?, body: String?, url: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("url", url)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // [중요 1] 채널 ID를 바꿉니다. (기존 설정 초기화 효과)
        val channelId = "my_channel_id_high_v1"

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            // [중요 2] 구버전 폰을 위해 '우선순위 높음' 설정
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 8.0 이상 채널 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "중요 알림 채널", // 채널 이름도 살짝 바꿈
                // [중요 3] 여기가 핵심! 중요도를 'HIGH'로 올려야 팝업이 뜹니다.
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        var notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val url = remoteMessage.data["url"]

        if (remoteMessage.notification != null) {
            sendNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                url
            )
        }
    }
}