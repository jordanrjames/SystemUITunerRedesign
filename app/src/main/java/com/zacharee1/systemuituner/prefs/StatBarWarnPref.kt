package com.zacharee1.systemuituner.prefs

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.zacharee1.systemuituner.R
import com.zacharee1.systemuituner.util.blacklistManager
import com.zacharee1.systemuituner.util.writeSecure

class StatBarWarnPref : RedTextPref {
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    init {
        setTitle(android.R.string.dialog_alert_title)
        setSummary(R.string.statbar_rotation_lock_notif)
        setIcon(R.drawable.ic_smartphone_black_24dp)
        isSelectable = true
    }

    override fun onClick() {
        val dialog = FixRotationDialog(context)
        dialog.performConfirm()
    }

    class FixRotationDialog(context: Context) : AlertDialog(context) {
        fun performConfirm() {
            setTitle(context.resources.getString(R.string.fix_rotation_icon_dialog_title))
            setMessage(context.resources.getString(R.string.fix_rotation_icon_dialog_desc))
            setButton(AlertDialog.BUTTON_POSITIVE, context.resources.getText(R.string.yes_im_uninstalling)) { _, _ -> }
            setButton(AlertDialog.BUTTON_NEGATIVE, context.resources.getText(R.string.no_im_staying)) { _, _ -> }

            setOnShowListener {
                getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { performUninstall() }
                getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener { performStay() }
            }

            show()
        }

        private fun performUninstall() {
            context.writeSecure("sysui_tuner_version", 0)
            context.blacklistManager.setCurrentBlacklist(null)

            setTitle(context.resources.getString(R.string.done))
            setMessage(context.resources.getString(R.string.fix_rotation_done_uninstall))

            val pos = getButton(AlertDialog.BUTTON_POSITIVE)
            val neg = getButton(AlertDialog.BUTTON_NEGATIVE)

            pos.text = context.resources.getText(R.string.ok)
            pos.setOnClickListener { dismiss() }
            neg.visibility = View.GONE
        }

        private fun performStay() {
            context.blacklistManager.addItem("rotate")

            setTitle(context.resources.getString(R.string.done))
            setMessage(context.resources.getString(R.string.fix_rotation_done_stay))

            val pos = getButton(AlertDialog.BUTTON_POSITIVE)
            val neg = getButton(AlertDialog.BUTTON_NEGATIVE)

            pos.text = context.resources.getText(R.string.ok)
            pos.setOnClickListener { dismiss() }
            neg.visibility = View.GONE
        }
    }
}
