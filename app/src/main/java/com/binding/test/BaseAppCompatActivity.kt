package com.binding.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseAppCompatActivity<VB: ViewBinding> : AppCompatActivity() {
    abstract fun getViewBinding(): VB
    protected lateinit var binding: VB

    abstract fun initEvent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        initEvent()
    }
}
