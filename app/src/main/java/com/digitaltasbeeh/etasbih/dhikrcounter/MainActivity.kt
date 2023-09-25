package com.digitaltasbeeh.etasbih.dhikrcounter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import com.digitaltasbeeh.etasbih.dhikrcounter.Ad_Utils.InAppBilling
import com.digitaltasbeeh.etasbih.dhikrcounter.ads.Banner
import com.digitaltasbeeh.etasbih.dhikrcounter.ads.InterstitialActivity
import com.digitaltasbeeh.etasbih.dhikrcounter.databinding.ActivityMainBinding
import com.digitaltasbeeh.etasbih.dhikrcounter.utils.SPRepository
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    private val mp = MediaPlayer()
    private var tasbiCounter = 0
    private lateinit var subBtn: CardView
    private lateinit var inAppBilling: InAppBilling
    var isInAppPurched = false
    private lateinit var inAppDialog: Dialog
    var count = 1;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val animation1 = AnimationUtils.loadAnimation(applicationContext, R.anim.button_scale)
        animation1.repeatCount = Animation.INFINITE
        animation1.repeatMode = Animation.REVERSE
        inAppBilling = InAppBilling(this, this);
        isInAppPurched = inAppBilling.hasUserBoughtInApp()

        CoroutineScope(Dispatchers.IO).launch {
            binding?.adView?.let { Banner.show(this@MainActivity, it, this@MainActivity) }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        if (isInAppPurched) {
            binding?.ivAdBtn?.visibility = View.GONE;
            binding?.adView?.visibility = View.GONE;
        } else {
            binding?.ivAdBtn?.visibility = View.VISIBLE;
            binding?.adView?.visibility = View.VISIBLE;
            setInAppDialog();
            inAppDialog.show();
        }
        binding?.ivAdBtn?.setOnClickListener {
            setInAppDialog();
            if (inAppDialog != null) {
                if (!isInAppPurched) {
                    inAppDialog.show();
                }

            }
        }
        initUtils()
        getValueFromDB()
        setListener()

    }

    private fun initUtils() {
        val font = Typeface.createFromAsset(this.assets, "Font/digital_font.TTF")
        binding!!.counterTv.typeface = font
        binding!!.counterTv2.typeface = font
    }

    private fun getValueFromDB() {

        tasbiCounter = SPRepository.getTasbihCounter(this)
        if (SPRepository.getTasbihLayout(this)) {
            binding?.counterLayout?.visibility = View.INVISIBLE
            binding?.counterLayoutGlow?.visibility = View.VISIBLE
        } else {
            binding?.counterLayout?.visibility = View.VISIBLE
            binding?.counterLayoutGlow?.visibility = View.INVISIBLE
        }
        if (SPRepository.getTasbihSound(this)) {
            binding?.soundImage?.setImageResource(R.drawable.ic_sound_on)
        } else {
            binding?.soundImage?.setImageResource(R.drawable.ic_sound_off)
        }
        if (SPRepository.getVibrate(this)) {
            binding?.vibrationImage?.setImageResource(R.drawable.ic_vibrate_on)
        } else {
            binding?.vibrationImage?.setImageResource(R.drawable.ic_vibrate_off)
        }
        if (SPRepository.getLock(this)) {
            binding?.lockImage?.setImageResource(R.drawable.ic_open)
        } else {
            binding?.lockImage?.setImageResource(R.drawable.ic_close)
        }
        setCounterValue()
    }

    @SuppressLint("SetTextI18n")
    private fun setCounterValue() {
        binding!!.counterTv.text = "" + tasbiCounter
        binding!!.counterTv2.text = "" + tasbiCounter
        SPRepository.setTasbihCounter(this, tasbiCounter)
    }

    private fun setListener() {
        binding?.resetBtn?.setOnClickListener {

            tasbiCounter = 0
            setCounterValue()
            buttonClick()
        }
        binding?.reset?.setOnClickListener {

            tasbiCounter = 0
            setCounterValue()
            buttonClick()
        }
        binding?.ledBtn?.setOnClickListener {

            setLedLayout()

        }
        binding?.led?.setOnClickListener {

            setLedLayout()
            buttonClick()

        }
        binding?.counter?.setOnClickListener {

            tasbiCounter++
            setCounterValue()
            buttonClick()
            vibrate()
        }
        binding?.ivMenu?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(
                GravityCompat.START
            )
        }

        binding?.soundImage?.setOnClickListener {
            Log.d("ABCD","Sound image"+count+"///////"+isInAppPurched)
            if(count==1 && !isInAppPurched){
                val intent = Intent(this, InterstitialActivity::class.java)
                resultLauncher.launch(intent)

            }
            if (SPRepository.getTasbihSound(this)) {
                binding?.soundImage?.setImageResource(R.drawable.ic_sound_off)
                SPRepository.setTasbihSound(this, false)
            } else {
                binding?.soundImage?.setImageResource(R.drawable.ic_sound_on)
                SPRepository.setTasbihSound(this, true)
                buttonClick()
            }
            count++;

        }
        binding?.vibrationImage?.setOnClickListener {
            if (SPRepository.getVibrate(this)) {
                binding?.vibrationImage?.setImageResource(R.drawable.ic_vibrate_off)
                SPRepository.setVibrate(this, false)
            } else {
                binding?.vibrationImage?.setImageResource(R.drawable.ic_vibrate_on)
                SPRepository.setVibrate(this, true)
                vibrate()
            }
        }
        binding?.lockImage?.setOnClickListener {
            if (SPRepository.getLock(this)) {
                binding?.lockImage?.setImageResource(R.drawable.ic_close)
                binding?.led?.isEnabled = true
                binding?.reset?.isEnabled = true
                binding?.counter?.isEnabled = true
                binding?.vibrationImage?.isEnabled = true
                binding?.soundImage?.isEnabled = true
                SPRepository.setLock(this, false)
            } else {
                binding?.lockImage?.setImageResource(R.drawable.ic_open)
                binding?.led?.isEnabled = false
                binding?.reset?.isEnabled = false
                binding?.counter?.isEnabled = false
                binding?.vibrationImage?.isEnabled = false
                binding?.soundImage?.isEnabled = false
                SPRepository.setLock(this, true)
            }
        }
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }
    private fun vibrate() {
        if (SPRepository.getVibrate(this)) {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        100, VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }
    }

    private fun buttonClick() {
        if (SPRepository.getTasbihSound(this)) {
            if (mp.isPlaying) {
                mp.stop()
            }
            try {
                mp.reset()
                val afd: AssetFileDescriptor = assets.openFd("button_sound.wav")
                mp.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                mp.prepare()
                mp.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setLedLayout() {
        if (!SPRepository.getTasbihLayout(this)) {
            binding?.counterLayout?.visibility = View.INVISIBLE
            binding?.counterLayoutGlow?.visibility = View.VISIBLE
            SPRepository.setTasbihLayout(this, true)
        } else {
            binding?.counterLayout?.visibility = View.VISIBLE
            binding?.counterLayoutGlow?.visibility = View.INVISIBLE
            SPRepository.setTasbihLayout(this, false)
        }
    }

    fun vibratePhone() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.privacy_policy -> {

                kotlin.runCatching {
                    closeDrawer()
                    val i = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/peri-studio-privacy-policy/privacy-policy?pli=1")
                    )
                    if (i.resolveActivity(packageManager) != null) startActivity(i)
                }

            }

            R.id.terms_conditions -> {

                kotlin.runCatching {
                    closeDrawer()
                    val i = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/view/peri-studio-privacy-policy/privacy-policy?pli=1")
                    )
                    if (i.resolveActivity(packageManager) != null) startActivity(i)
                }
            }

            R.id.rate_us -> {
                rateApp()
                closeDrawer()
            }

            R.id.share -> {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
                sendIntent.type = "text/plain"
                if (sendIntent.resolveActivity(packageManager) != null) {
                    startActivity(sendIntent)
                }
                closeDrawer()
            }
        }
        return true
    }

    private fun closeDrawer() {
        if (binding?.drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }
    }

    private fun rateIntentForUrl(url: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url + packageName))
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        intent.addFlags(flags)
        return intent
    }

    private fun rateApp() {
        try {
            val rateIntent = rateIntentForUrl("market://details?id=")
            if (rateIntent.resolveActivity(packageManager) != null) {
                startActivity(rateIntent)
            }
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details?id=")
            if (rateIntent.resolveActivity(packageManager) != null) {
                startActivity(rateIntent)
            }
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                exitDialog()
                showNativeAdExitDialog();
            }
        }

    /* private fun exitDialog() {
         val bottomSheetExitDialog = BottomSheetDialog(this)
         val exitDialogLayout = layoutInflater.inflate(R.layout.exit_dialog, null)
         bottomSheetExitDialog.setContentView(exitDialogLayout)
         bottomSheetExitDialog.findViewById<FrameLayout>(R.id.adFrame)?.let {
             CoroutineScope(Dispatchers.IO).launch {
                 NativeAd.showNativeAdvancedAd(this@MainActivity, this@MainActivity, it, false)
             }
         }
         bottomSheetExitDialog.show()
         closeDrawer()
         bottomSheetExitDialog.findViewById<TextView>(R.id.exitBtn)?.setOnClickListener {
             finishAffinity()
         }
     }*/
    fun showNativeAdExitDialog() {
        val exitDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        val view1 = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null)
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        exitDialog.setContentView(view1)
        val inAppBilling = InAppBilling(this, this)
        val isInAppPurchased = inAppBilling.hasUserBoughtInApp()

        val window = exitDialog.window
        val wlp = window?.attributes
        wlp?.gravity = Gravity.CENTER
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv())
        window?.attributes = wlp
        exitDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        val adnativeLayout = view1.findViewById<ConstraintLayout>(R.id.adlayoutNative)


        val is_In_App_Purchased = inAppBilling.hasUserBoughtInApp()

        if (is_In_App_Purchased) {
            adnativeLayout?.visibility = View.GONE
        } else {
            showBannerAd(adnativeLayout, this)
            adnativeLayout?.visibility = View.VISIBLE
        }

        val yesBtn = view1.findViewById<CardView>(R.id.yesBtn)
        val NoBtn = view1.findViewById<CardView>(R.id.Nobtn)

        yesBtn.setOnClickListener {
            if (exitDialog.isShowing) {
                exitDialog.dismiss()
            }
            finishAffinity()
        }

        NoBtn.setOnClickListener {
            exitDialog.dismiss()
        }
        exitDialog.show()
    }


    private fun showBannerAd(adLayout: ConstraintLayout, context: Context) {
        val adRequest = AdRequest.Builder().build()
        val adView = AdView(context)
        adView.adUnitId = context.getString(R.string.banner_low_id)
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
        adView.loadAd(adRequest)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        adLayout.addView(adView, params)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                if (loadAdError != null && loadAdError.code == AdRequest.ERROR_CODE_NO_FILL) {

                }
            }
        }
    }

    private fun setInAppDialog() {
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_scale)
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        inAppDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        val view1 = layoutInflater.inflate(R.layout.inapp_layout, null)
        inAppDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        inAppDialog?.setContentView(view1)
        val window = inAppDialog.window
        val wlp = window?.attributes
        val closebtn = view1.findViewById<ImageView>(R.id.cross_btn)
        subBtn = view1.findViewById(R.id.subscribeNow)
        subBtn.startAnimation(animation)
        closebtn.setOnClickListener {
            if (inAppDialog.isShowing) {
                inAppDialog.dismiss()
            }
        }
        subBtn.setOnClickListener {
            inAppBilling.purchase(view1)
        }
        wlp?.gravity = Gravity.CENTER
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv())
        window?.attributes = wlp
        inAppDialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onResume() {
        super.onResume()
        inAppBilling = InAppBilling(this, this);
        var isPurchase = inAppBilling.isCurrentpurchased();
        if (isPurchase) {
            val intent = Intent(this@MainActivity, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            isInAppPurched = inAppBilling.hasUserBoughtInApp();
            if (isInAppPurched) {
                binding?.adView?.visibility = View.GONE
            } else {
                binding?.adView?.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    binding?.adView?.let { Banner.show(this@MainActivity, it, this@MainActivity) }
                }
            }
        }

    }
}