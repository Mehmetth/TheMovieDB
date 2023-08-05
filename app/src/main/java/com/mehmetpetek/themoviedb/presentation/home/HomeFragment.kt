package com.mehmetpetek.themoviedb.presentation.home

import androidx.fragment.app.viewModels
import com.mehmetpetek.themoviedb.databinding.FragmentHomeBinding
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun bindScreen() {
    }
}