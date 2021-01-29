package com.binding.nicedialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Created by Mikes at 1/29/21 4:44 PM
 * ================================================
 * ================================================
 */
interface ViewConvertListener<VB : ViewBinding> {
    fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?) : VB
    fun convertView(vb: VB, dialog: BaseNiceDialog<VB>)
}