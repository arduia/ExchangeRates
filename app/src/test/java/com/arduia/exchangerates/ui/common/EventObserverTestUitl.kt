package com.arduia.exchangerates.ui.common


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry

/**
 * Created by Aung Ye Htet on 20/01/2021.
 */
//Also available in MVVM-Core: https://github.com/arduia/mvvm-core
fun LifecycleRegistry.makeFakeConfigurationChanges() {
    handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    handleLifecycleEvent(Lifecycle.Event.ON_START)
    handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
}