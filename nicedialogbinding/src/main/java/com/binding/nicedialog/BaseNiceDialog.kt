package com.binding.nicedialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding

/**
 * Created by Mikes at 1/29/21 4:08 PM
 * ================================================
 * ================================================
 */
abstract class BaseNiceDialog<VB : ViewBinding> : DialogFragment() {
    companion object {
        private const val MARGIN = "margin"
        private const val WIDTH = "width"
        private const val HEIGHT = "height"
        private const val DIM = "dim_amount"
        private const val GRAVITY = "gravity"
        private const val CANCEL = "out_cancel"
        private const val THEME = "theme"
        private const val ANIM = "anim_style"

        private fun getScreenWidth(context: Context): Int =
            context.resources.displayMetrics.widthPixels

        private fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

    private lateinit var mHostContext: Context

    var dismissCallback : DismissCallback?=null
    // 默认true,认为由用户关闭的弹窗
    protected var dismissManually = true

    // 判断Dialog是否显示
    private var isPrepared = false
    private var isInVisible = false

    private var isDialogShowing = false

    fun isShowing(): Boolean = isDialogShowing

    protected var margin = 0 //左右边距
    protected var width = 0 //宽度
    protected var height = 0 //高度
    protected var dimAmount = 0.5f //灰度深浅
    protected var gravity = Gravity.CENTER //显示的位置
    protected var outCancel = true //是否点击外部取消

    @StyleRes
    protected var dialogTheme = R.style.NiceDialogStyle

    @StyleRes
    protected var animStyle = 0

    private var _binding: VB? = null

    abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB?

    abstract fun convertView(vb: VB, dialog: BaseNiceDialog<VB>)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mHostContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, dialogTheme)

        //恢复保存的数据
        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            dimAmount = savedInstanceState.getFloat(DIM)
            gravity = savedInstanceState.getInt(GRAVITY)
            outCancel = savedInstanceState.getBoolean(CANCEL)
            dialogTheme = savedInstanceState.getInt(THEME)
            animStyle = savedInstanceState.getInt(ANIM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding(inflater, container)
        if(_binding == null) throw IllegalArgumentException("ViewBinding MUST BE NULL")
        convertView(_binding!!, this)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isPrepared && userVisibleHint) {
            onDialogVisibleChange(true)
            isInVisible = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isPrepared = true
        if (isVisibleToUser) {
            onDialogVisibleChange(true)
            isInVisible = true
            return
        }
        if (isInVisible) {
            onDialogVisibleChange(false)
            isInVisible = false
        }
    }

    open fun onDialogVisibleChange(isVisible: Boolean) {
        isDialogShowing = isVisible
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissCallback?.onDismiss(this, dismissManually)
    }

    /**
     * dialog dismissed by user
     */
    fun dismissManually(manually: Boolean) {
        dismissManually = manually
        super.dismissAllowingStateLoss()
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun dismissAllowingStateLoss() {
        dismissManually = true
        super.dismissAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MARGIN, margin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putInt(GRAVITY, gravity)
        outState.putBoolean(CANCEL, outCancel)
        outState.putInt(THEME, theme)
        outState.putInt(ANIM, animStyle)
    }

    private fun initParams() {
        val nonNullDialog = dialog ?: return
        val window = nonNullDialog.window
        if (window != null) {
            val lp = window.attributes
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount
            if (gravity != 0) {
                lp.gravity = gravity
            }

            //设置dialog宽度
            when (width) {
                0 -> {
                    lp.width = getScreenWidth(mHostContext) - 2 * dp2px(mHostContext, margin.toFloat())
                }
                -1 -> {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                }
                else -> {
                    lp.width = dp2px(mHostContext, width.toFloat())
                }
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.height = dp2px(mHostContext, height.toFloat())
            }

            //设置dialog进入、退出的动画
            if(animStyle != 0){
                window.setWindowAnimations(animStyle)
            }
            window.attributes = lp
        }
        isCancelable = outCancel
    }

    open fun setDialogTheme(@StyleRes theme : Int): BaseNiceDialog<*> {
        this.dialogTheme = theme
        return this
    }

    open fun setMargin(margin: Int): BaseNiceDialog<*> {
        this.margin = margin
        return this
    }

    open fun setWidth(width: Int): BaseNiceDialog<*> {
        this.width = width
        return this
    }

    open fun setHeight(height: Int): BaseNiceDialog<*> {
        this.height = height
        return this
    }

    open fun setDimAmount(dimAmount: Float): BaseNiceDialog<*> {
        this.dimAmount = dimAmount
        return this
    }

    open fun setGravity(gravity: Int): BaseNiceDialog<*> {
        this.gravity = gravity
        return this
    }

    open fun setOutCancel(outCancel: Boolean): BaseNiceDialog<*> {
        this.outCancel = outCancel
        return this
    }

    open fun setAnimStyle(@StyleRes animStyle: Int): BaseNiceDialog<*> {
        this.animStyle = animStyle
        return this
    }

    open fun show(manager: FragmentManager): BaseNiceDialog<*> {
        val ft = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this).commit()
        }
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()

        return this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}