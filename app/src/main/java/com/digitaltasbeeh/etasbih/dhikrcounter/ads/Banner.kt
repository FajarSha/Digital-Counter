package com.digitaltasbeeh.etasbih.dhikrcounter.ads

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.digitaltasbeeh.etasbih.dhikrcounter.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

object Banner {

    fun show(context: Context, layout: FrameLayout, activity: Activity) {
        kotlin.runCatching {
            if (!activity.isFinishing) {
                MobileAds.initialize(context) {}
                val adView = AdView(context)
                adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                adView.setAdSize(AdSize(AdSize.FULL_WIDTH, 52))
                adView.setAdSize(AdSize.BANNER)

                adView.adUnitId = context.getString(R.string.banner_high_id)
                adView.adListener = object : AdListener() {

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        showMedium(context, layout, activity);
                    }
                }
                val adRequest = AdRequest.Builder().build()
                activity.runOnUiThread {
                    layout.addView(adView)
                    adView.loadAd(adRequest)
                }
            }
        }
    }
    fun showMedium(context: Context, layout: FrameLayout, activity: Activity) {
        kotlin.runCatching {
            if (!activity.isFinishing) {
                MobileAds.initialize(context) {}
                val adView = AdView(context)
                adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                adView.setAdSize(AdSize(AdSize.FULL_WIDTH, 52))
                adView.setAdSize(AdSize.BANNER)

                adView.adUnitId = context.getString(R.string.banner_medium_id)

                adView.adListener = object : AdListener() {

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        showLow(context, layout, activity);
                    }
                }
                val adRequest = AdRequest.Builder().build()
                activity.runOnUiThread {
                    layout.addView(adView)
                    adView.loadAd(adRequest)
                }
            }
        }
    }
    fun showLow(context: Context, layout: FrameLayout, activity: Activity) {
        kotlin.runCatching {
            if (!activity.isFinishing) {
                MobileAds.initialize(context) {}
                val adView = AdView(context)
                adView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
//                adView.setAdSize(AdSize(AdSize.FULL_WIDTH, 52))
                adView.setAdSize(AdSize.BANNER)
                adView.adUnitId = context.getString(R.string.banner_low_id)

                val adRequest = AdRequest.Builder().build()
                activity.runOnUiThread {
                    layout.addView(adView)
                    adView.loadAd(adRequest)
                }
            }
        }
    }
}