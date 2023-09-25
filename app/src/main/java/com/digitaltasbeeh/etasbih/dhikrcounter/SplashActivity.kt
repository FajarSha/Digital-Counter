package com.digitaltasbeeh.etasbih.dhikrcounter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.digitaltasbeeh.etasbih.dhikrcounter.Ad_Utils.InAppBilling
import com.digitaltasbeeh.etasbih.dhikrcounter.ads.InterstitialActivity
import com.digitaltasbeeh.etasbih.dhikrcounter.databinding.ActivitySplashBinding
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    var binding : ActivitySplashBinding? = null
    var isInAppPurchase = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var inAppBilling: InAppBilling? = null
        inAppBilling = InAppBilling(this, this);
        isInAppPurchase = inAppBilling.hasUserBoughtInApp();

        if (isInAppPurchase) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        val intent = Intent(this@SplashActivity, TutorialActivity::class.java)
                        startActivity(intent)
                        finish()
//                    binding?.btnStart?.visibility = View.VISIBLE
                    }
                }
            }, 3000)
        } else {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        val intent = Intent(this@SplashActivity, TutorialActivity::class.java)
                        startActivity(intent)
                        finish()
//                    binding?.btnStart?.visibility = View.VISIBLE
                    }
                }
            }, 5000)
        }


    }
    protected override fun onResume() {
        super.onResume()
        var inAppBilling: InAppBilling? = null
        inAppBilling = InAppBilling(this, this)
        inAppBilling.savePurchaseValueToPref(false)
        isInAppPurchase = inAppBilling.hasUserBoughtInApp()
        if (isInAppPurchase) {
            binding?.adLayout?.visibility = View.GONE
            binding?.loadingtxt?.text = "Loading. "
        } else {
            binding?.adLayout?.visibility = View.VISIBLE
            binding?.loadingtxt?.text = "Loading Ad "
            loadBannerAd(getString(R.string.splash_id))
        }
    }
    private fun loadBannerAd(unitId: String) {
        val adView = AdView(this)
        adView.setAdUnitId(unitId)
        adView.setAdSize(AdSize.BANNER)
        //        adView.loadAd(adRequest);
        val extras = Bundle()
        extras.putString("collapsible", "bottom")
        var adRequest: AdRequest? = null
        adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        adView.loadAd(adRequest)
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        binding?.adLayout?.addView(adView, params)
        //       this.adView.loadAd(adRequest);
        adView.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {

                }
            }
        })
    }
    override fun onBackPressed() {
//        super.onBackPressed();
    }

    override fun onPause() {
        super.onPause()
    }

}