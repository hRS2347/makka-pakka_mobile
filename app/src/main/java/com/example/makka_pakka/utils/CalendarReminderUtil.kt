package com.example.makka_pakka.utils

import android.content.Context
import android.provider.CalendarContract
import androidx.core.content.contentValuesOf
import java.util.Calendar
import java.util.TimeZone

object CalendarReminderUtil {
    /**
     * 添加日历事件
     * 里面有个日历的重复规则，比较麻烦，可查看https://www.jianshu.com/p/8f8572292c58里面有详细的重复规则
     */
    fun addCalendarEvent(
        context: Context,//上下文
        name: String,//事件名称
        startTime: Long?,//开始时间
//        planWeek: String,//重复规则,不重复传空字符串，重复传"MO,TU,WE,TH,FR,SA,SU"
//        untilDate: String//重复规则结束时间，不重复传空字符串，重复传"yyyyMMdd'T'HHmmss'Z'"
    ) {
        val cr = context.contentResolver
        val timeZone = TimeZone.getDefault().id
        val beginTime = Calendar.getInstance().apply {
            timeInMillis = startTime!!
        }
        val endTime = Calendar.getInstance().apply {
            timeInMillis = if (startTime != null) {
                startTime + 60 * 60 * 1000
            } else {
                System.currentTimeMillis() + 60 * 60 * 1000
            }
        }
        val values = contentValuesOf(
            CalendarContract.Events.DTSTART to beginTime.timeInMillis,
            CalendarContract.Events.DTEND to endTime.timeInMillis,
            CalendarContract.Events.TITLE to name,
            CalendarContract.Events.DESCRIPTION to "描述",
            CalendarContract.Events.CALENDAR_ID to 1,
            CalendarContract.Events.EVENT_TIMEZONE to timeZone,
            CalendarContract.Events.RRULE to "FREQ=WEEKLY;WKST=SU;BYDAY=;UNTIL="
        )
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment?.toLong()
        val reminderUri = CalendarContract.Reminders.CONTENT_URI
        val reminderValues = contentValuesOf(
            CalendarContract.Reminders.EVENT_ID to eventId,
            CalendarContract.Reminders.MINUTES to 10,
            CalendarContract.Reminders.METHOD to CalendarContract.Reminders.METHOD_ALERT
        )
        cr.insert(reminderUri, reminderValues)
    }


}
