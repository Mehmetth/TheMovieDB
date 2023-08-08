package com.mehmetpetek.themoviedb.presentation.player

import androidx.fragment.app.viewModels
import com.mehmetpetek.themoviedb.databinding.FragmentPlayerBinding
import com.mehmetpetek.themoviedb.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate) {

    private val viewModel by viewModels<PlayerViewModel>()

    override fun bindScreen() {
        TODO("Not yet implemented")
    }
}