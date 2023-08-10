package com.mehmetpetek.themoviedb.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.mehmetpetek.themoviedb.R
import com.mehmetpetek.themoviedb.common.extensions.gone
import com.mehmetpetek.themoviedb.common.extensions.showFullPagePopup
import com.mehmetpetek.themoviedb.common.extensions.visible
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseFragment<T : ViewBinding>(
    private val inflate: Inflate<T>,
) : Fragment() {

    private var progress: View? = null
    protected lateinit var binding: T
    open val saveBinding: Boolean = false
    abstract fun bindScreen()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (this::binding.isInitialized && saveBinding) {
            binding
        } else {
            binding = inflate(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindScreen()
    }

    fun onBackClicked() {
        if (!findNavController().popBackStack()) {
            requireActivity().finish()
        }
    }

    fun setLoadingState(
        visible: Boolean,
    ) {
        if (visible) showLoading() else hideLoading()
    }

    private fun showLoading() {
        if (progress == null) {
            progress = layoutInflater.inflate(R.layout.dialog_progress, null)
            val layoutParams = ViewGroup.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            progress?.layoutParams = layoutParams
            (binding.root as ViewGroup).addView(progress)
        }
        progress?.visible()
    }

    private fun hideLoading() {
        progress?.gone()
        progress = null
    }

    fun handleError(throwable: Throwable?, primaryListener: (() -> Unit)? = null) {
        when (throwable) {
            is ConnectException,
            is UnknownHostException,
            -> {
                showFullPagePopup(
                    R.drawable.ic_info,
                    title = getString(R.string.error),
                    desc = getString(R.string.no_internet),
                    buttonPrimary = getString(R.string.ok),
                    primaryListener = primaryListener
                )
            }

            else -> {
                showFullPagePopup(
                    R.drawable.ic_info,
                    title = getString(R.string.general_error),
                    desc = throwable?.localizedMessage,
                    buttonPrimary = getString(R.string.ok),
                    primaryListener = primaryListener
                )
            }
        }
    }
}