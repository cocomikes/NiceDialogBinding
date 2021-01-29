package com.binding.test

import android.view.LayoutInflater
import android.view.ViewGroup
import com.binding.nicedialog.BaseNiceDialog
import com.binding.nicedialog.NiceDialog
import com.binding.nicedialog.ViewConvertListener
import com.binding.test.databinding.ActivityMainBinding
import com.binding.test.databinding.DialogTestBinding

class MainActivity : BaseAppCompatActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding= ActivityMainBinding.inflate(layoutInflater)

    override fun initEvent() {
        binding.tv.setOnClickListener {
            NiceDialog<DialogTestBinding>()
                .setConvertListener(object : ViewConvertListener<DialogTestBinding>{
                    override fun provideViewBinding(
                        inflater: LayoutInflater,
                        container: ViewGroup?
                    ): DialogTestBinding = DialogTestBinding.inflate(inflater, container, false)

                    override fun convertView(
                        vb: DialogTestBinding,
                        dialog: BaseNiceDialog<DialogTestBinding>
                    ) {
                        vb.clickMe.setOnClickListener {
                            dialog.dismiss()
                        }
                    }
                })
                .setHeight(250)
                .setMargin(20)
                .setOutCancel(false)
                .show(supportFragmentManager)
        }

        binding.tv2.setOnClickListener {
            TipsDialog.generate {
                setOutCancel(true)
                setMargin(20)
                setHeight(300) as TipsDialog
            }.show(supportFragmentManager)
        }
    }
}