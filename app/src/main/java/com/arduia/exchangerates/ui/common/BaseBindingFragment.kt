package com.arduia.exchangerates.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by Aung Ye Htet at 16/1/2021 5:54 PM.
 */
abstract class BaseBindingFragment<V : ViewBinding> : Fragment() {

    private var _binding: V? = null
    protected val binding: V
        get() = _binding ?: throw Exception("ViewBinding is not initialized yet!")

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
    }

    abstract fun createBinding(layoutInflater: LayoutInflater, parent: ViewGroup?): V

    open fun onViewCreated(savedInstanceState: Bundle?) {}

    open fun onBeforeBindingDestroyed() {}

    open fun onAfterBindingDestroyed() {}

    final override fun onDestroyView() {
        super.onDestroyView()
        onBeforeBindingDestroyed()
        _binding = null
        onAfterBindingDestroyed()
    }

}