package com.arduia.exchangerates.ui.common.ext

import android.content.Context

fun Context.getApplicationVersionName(): String{
    return packageManager.getPackageInfo(packageName, 0).versionName
}