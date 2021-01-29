package com.binding.nicedialog

import androidx.fragment.app.DialogFragment

/**
 * Created by Mikes at 1/29/21 6:19 PM
 * ================================================
 * ================================================
 */
interface DismissCallback {
    /**
     * @param dialog
     * @param dismissManually true, 用户关闭弹窗, false 弹窗管理器关闭弹窗
     */
    fun onDismiss(dialog: DialogFragment, dismissManually: Boolean)
}