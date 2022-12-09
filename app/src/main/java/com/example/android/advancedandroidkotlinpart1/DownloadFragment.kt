package com.example.android.advancedandroidkotlinpart1

import android.animation.*
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.android.advancedandroidkotlinpart1.databinding.FragmentDownloadBinding

class DownloadFragment : Fragment() {
    lateinit var binding: FragmentDownloadBinding
    lateinit var animators: AnimatorSet
    lateinit var downloadManager: DownloadManager
    lateinit var downloadRequest: DownloadManager.Request
    var downloadId = -1L
    var downloadTitle = ""

    val new = object: BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var status = "Failed"
            if (id == downloadId) status = "Success"
            arguments = Bundle().apply {
                putString(FILE_NAME, downloadTitle)
                putString(STATUS, status)
            }

            val notificationManager = getSystemService(
                context, NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendDownloadNotification(
                "The download  has completed!",
                arguments,
                context
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDownloadBinding.inflate(inflater)

        binding.radioGroup.checkedRadioButtonId

        binding.btnDownload.setOnClickListener {
            val notificationManager = ContextCompat.getSystemService(
                requireContext(),
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.cancelNotifications()

           when(binding.radioGroup.checkedRadioButtonId){
               binding.radioGlide.id -> download("https://github.com/bumptech/glide",  getString(R.string.glid_description))
               binding.radioRepo.id -> download("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter", getString(R.string.current_repo_description))
               binding.radioRetrofit.id -> download("https://github.com/square/retrofit", getString(R.string.retrofit_description))
               else -> Toast.makeText(context, "Please select something to download", Toast.LENGTH_SHORT).show()
            }
        }

        setUpAnimators()
        downloadManager = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        requireActivity().registerReceiver(new, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(getString(R.string.download_channel_id), getString(R.string.download_channel))

        return binding.root
    }

    fun setUpAnimators() {
        val buttonAnimator = ObjectAnimator.ofArgb(binding.btnDownload,
            "backgroundColor", Color.GREEN, Color.DKGRAY)
        buttonAnimator.let {
            it.duration = 1000
            disableDownloadButtonDuringDownload(binding.btnDownload, buttonAnimator)
        }

        val circleAnimator = ValueAnimator.ofFloat(0F, 1F)
            .apply {
                duration = 1000
                addUpdateListener { animator ->
                    binding.btnDownload.arcProportion = animator.animatedValue as Float
                    binding.btnDownload.invalidate()
                }
            }

        animators = AnimatorSet()
        animators.playTogether( buttonAnimator, circleAnimator)
        animators.duration = 1000
    }

    fun download(url: String, title: String){
        downloadRequest = DownloadManager.Request(Uri.parse(url))
        downloadTitle = title
        downloadId = downloadManager.enqueue(downloadRequest)
        animators.start()
    }

    private fun disableDownloadButtonDuringDownload(view: View, animator: Animator) {
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                binding.btnDownload.isWaiting = false
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.btnDownload.isWaiting = true
                view.isEnabled = true
            }
        })
    }

    private fun createChannel(channelId: String, channelName: String) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply { setShowBadge(false) }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
}

