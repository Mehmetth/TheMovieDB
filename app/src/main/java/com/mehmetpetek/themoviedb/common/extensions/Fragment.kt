package com.mehmetpetek.themoviedb.common.extensions

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.databinding.DialogFullPopupBinding

fun Fragment.resColor(@ColorRes colorRes: Int) =
    ResourcesCompat.getColor(resources, colorRes, null)


fun Fragment.showFullPagePopup(
    @DrawableRes iconId: Int,
    title: String? = null,
    desc: String? = null,
    buttonPrimary: String? = null,
    isCancellable: Boolean = false,
    primaryListener: (() -> Unit)? = null,
    cancelListener: (() -> Unit)? = null,
) {
    val dialog = Dialog(requireContext(), R.style.BaseDialogStyle)
    val binding: DialogFullPopupBinding =
        DialogFullPopupBinding.inflate(dialog.layoutInflater, null, false)
    dialog.setContentView(binding.root)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    dialog.window?.statusBarColor = resColor(android.R.color.white)
    binding.ivIcon.setImageResource(iconId)
    binding.tvTitle.text = title
    binding.tvDescription.text = desc
    binding.btnPrimary.text = buttonPrimary
    dialog.setCancelable(isCancellable)
    dialog.setCanceledOnTouchOutside(false)
    binding.btnPrimary.setOnClickListener {
        dialog.dismiss()
        primaryListener?.invoke()
    }
    dialog.setOnCancelListener {
        cancelListener?.invoke()
    }
    dialog.show()
}