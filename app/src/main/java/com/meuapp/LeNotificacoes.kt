package com.meuapp

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class LeNotificacoes : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val n = sbn.notification
        val titulo = n.extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val texto = n.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        if (titulo.isNotEmpty()) {
            val i = Intent("NOVA_MENSAGEM")
            i.putExtra("de", titulo)
            i.putExtra("texto", texto)
            sendBroadcast(i)
        }
    }
}
