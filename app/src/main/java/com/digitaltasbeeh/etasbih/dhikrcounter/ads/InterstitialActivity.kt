package com.digitaltasbeeh.etasbih.dhikrcounter.ads

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.digitaltasbeeh.etasbih.dhikrcounter.R
import com.digitaltasbeeh.etasbih.dhikrcounter.databinding.ActivityInterstitialBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialActivity : AppCompatActivity() {

    private var binding: ActivityInterstitialBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInterstitialBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        load()
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun load() {
        kotlin.runCatching {
            val adRequest = AdRequest.Builder().build()
            val unitId = getString(R.string.interstitial_high_id)
            InterstitialAd.load(
                this,
                unitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("TAG", "onAdFailedToLoad: high  ")
                      Mediumload();
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        kotlin.runCatching {
                            interstitialAd.show(this@InterstitialActivity)
                            interstitialAd.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                        super.onAdFailedToShowFullScreenContent(p0)
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }
                                }
                        }.onFailure {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                })
        }.onFailure {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun Mediumload() {
        kotlin.runCatching {
            val adRequest = AdRequest.Builder().build()
            val unitId = getString(R.string.interstitial_medium_id)
            InterstitialAd.load(
                this,
                unitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Log.d("TAG", "onAdFailedToLoad: medium  ")
                        lowload()
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        kotlin.runCatching {
                            interstitialAd.show(this@InterstitialActivity)
                            interstitialAd.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                        super.onAdFailedToShowFullScreenContent(p0)
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }
                                }
                        }.onFailure {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                })
        }.onFailure {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun lowload() {
        kotlin.runCatching {
            val adRequest = AdRequest.Builder().build()
            val unitId = getString(R.string.interstitial_low_id)
            InterstitialAd.load(
                this,
                unitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        kotlin.runCatching {
                            interstitialAd.show(this@InterstitialActivity)
                            interstitialAd.fullScreenContentCallback =
                                object : FullScreenContentCallback() {
                                    override fun onAdDismissedFullScreenContent() {
                                        super.onAdDismissedFullScreenContent()
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }

                                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                        super.onAdFailedToShowFullScreenContent(p0)
                                        setResult(Activity.RESULT_OK)
                                        finish()
                                    }
                                }
                        }.onFailure {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                })
        }.onFailure {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }

    override fun onDestroy() {
        super.onDestroy()
        finishAndRemoveTask()
    }
}