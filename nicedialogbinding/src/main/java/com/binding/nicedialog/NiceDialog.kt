package com.binding.nicedialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Created by Mikes at 1/29/21 5:13 PM
 * ================================================
 * ================================================
 */
class NiceDialog<VB : ViewBinding> : BaseNiceDialog<VB>(){
    private var convertListener: ViewConvertListener<VB>? = null

    fun setConvertListener(listener : ViewConvertListener<VB>): NiceDialog<*> {
        this.convertListener = listener
        return this
    }

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? = convertListener?.provideViewBinding(inflater, container)

    override fun convertView(vb: VB, dialog: BaseNiceDialog<VB>) {
        convertListener?.convertView(vb, dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        convertListener = null
    }
}