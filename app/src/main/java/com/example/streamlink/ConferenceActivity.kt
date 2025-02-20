package com.example.streamlink

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment
import com.zegocloud.uikit.prebuilt.videoconference.config.ZegoLeaveConfirmDialogInfo
import com.zegocloud.uikit.prebuilt.videoconference.config.ZegoMenuBarButtonName
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.constants.ZegoANSMode
import im.zego.zegoexpress.constants.ZegoVideoConfigPreset
import im.zego.zegoexpress.constants.ZegoVideoMirrorMode
import im.zego.zegoexpress.entity.ZegoVideoConfig
import java.util.Arrays

class ConferenceActivity : AppCompatActivity() {
    private lateinit var meetingID: String
    private lateinit var username: String
    private lateinit var meetingIDTextView: TextView
    private lateinit var shareButtonImageView: ImageView
    private lateinit var copyButtonImageView: ImageView
    private lateinit var meetingIDCard: MaterialCardView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conference)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        meetingIDTextView = findViewById(R.id.meeting_id_textview)
        shareButtonImageView = findViewById(R.id.share_button_imageview)
        copyButtonImageView = findViewById(R.id.copy_button_imageview)
        meetingIDCard = findViewById(R.id.meeting_id_card)

        meetingID = intent.getStringExtra("MEETING_ID").orEmpty()
        username = intent.getStringExtra("USERNAME").orEmpty()
        meetingIDTextView.text = "Meeting ID: $meetingID"

        addFragment()
        hideMeetingIDCardAfterDelay()

        shareButtonImageView.setOnClickListener { shareMeetingID() }
        copyButtonImageView.setOnClickListener { copyToClipboard(meetingID) }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Meeting ID", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "Meeting ID copied!", Toast.LENGTH_SHORT).show()

        // Show tick icon temporarily
        copyButtonImageView.setImageResource(R.drawable.tick)
        handler.postDelayed({ copyButtonImageView.setImageResource(R.drawable.copy) }, 2000)
    }

    private fun shareMeetingID() {
        val shareMessage = """
            🚀 Join my StreamLink meeting now!
            
            📌 Meeting ID: $meetingID
            
            🔗 Download the app: https://github.com/ahansardar/StreamLink/releases/tag/v1.0.0
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share to:"))
    }

    private fun addFragment() {
        val appID: Long = AppConstants.appId
        val appSign: String = AppConstants.appSign

        val config = ZegoUIKitPrebuiltVideoConferenceConfig().apply {
            leaveConfirmDialogInfo = ZegoLeaveConfirmDialogInfo().apply {
                title = "Leave the conference"
                message = "Are you sure to leave the conference?"
                cancelButtonName = "Cancel"
                confirmButtonName = "Confirm"
            }
            turnOnCameraWhenJoining = false
            turnOnMicrophoneWhenJoining = false
            useSpeakerWhenJoining = true
            audioVideoViewConfig.showCameraStateOnView = true

            bottomMenuBarConfig.buttons = listOf(
                ZegoMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
                ZegoMenuBarButtonName.LEAVE_BUTTON,
                ZegoMenuBarButtonName.TOGGLE_CAMERA_BUTTON,
                ZegoMenuBarButtonName.SWITCH_CAMERA_BUTTON,
                ZegoMenuBarButtonName.SWITCH_AUDIO_OUTPUT_BUTTON,

                ZegoMenuBarButtonName.CHAT_BUTTON,

                ZegoMenuBarButtonName.SHOW_MEMBER_LIST_BUTTON,

            )
        }



        val fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
            appID, appSign, username, username, meetingID, config
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
    }

    private fun showMeetingIDCardTemporarily() {
        meetingIDCard.visibility = View.VISIBLE
        hideMeetingIDCardAfterDelay()
    }

    private fun hideMeetingIDCardAfterDelay() {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ meetingIDCard.visibility = View.GONE }, 5100)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            showMeetingIDCardTemporarily()
        }
        return super.dispatchTouchEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Prevent memory leaks
    }
}
