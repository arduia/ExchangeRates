package com.arduia.exchangerates.ui.common.ext

import android.content.Context

/**
 * Created by Aung Ye Htet at 17/01/2021 1:48PM.
 */
fun Context.getApplicationVersionName(): String{
    return packageManager.getPackageInfo(packageName, 0).versionName
}