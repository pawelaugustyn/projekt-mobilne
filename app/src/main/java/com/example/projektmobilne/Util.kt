package com.example.projektmobilne

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.projektmobilne.database.Note
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

fun formatNotes(notes: List<Note>, resources: Resources): Spanned {
    val sb = StringBuilder()
    sb.apply {
        append(resources.getString(R.string.title))
        notes.forEach {
            append("<br>")
            append("\t${convertLongToDateString(it.timeCreated)}<br>")
            if (it.noteText.length > 50) {
                append("${it.noteText.substring(0, 50)}...<br>")
            } else {
                append("${it.noteText.replace("\\n", "<br>")}<br>")
            }
            append("<br>")
        }
        if (notes.isEmpty()) {
            append("<br>You don't have any notes")
        }
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}