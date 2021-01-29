package com.binding.test

import android.view.LayoutInflater
import android.view.ViewGroup
import com.binding.nicedialog.BaseNiceDialog
import com.binding.test.databinding.DialogTipsBinding

class TipsDialog : BaseNiceDialog<DialogTipsBinding>(){
    companion object {
        fun generate(body: TipsDialog.() -> TipsDialog): TipsDialog {
            return with(TipsDialog()) {
                body()
            }
        }
    }

    override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogTipsBinding?
        = DialogTipsBinding.inflate(inflater, container, false)

    override fun convertView(vb: DialogTipsBinding, dialog: BaseNiceDialog<DialogTipsBinding>) {
        vb.clickMe.text = "Hahah Lalala"
    }
}