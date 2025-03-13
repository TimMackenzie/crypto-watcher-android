package com.simplifynow.cryptowatcher

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This application definition is needed to support DI with Hilt
 */
@HiltAndroidApp
class CryptoWatcherApp : Application()