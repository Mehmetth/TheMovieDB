package com.mehmetpetek.themoviedb.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding>(
    private val inflate: Inflate<T>,
) : Fragment() {

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

}