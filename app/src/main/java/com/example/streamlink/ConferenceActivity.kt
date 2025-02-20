package com.example.streamlink


import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment


class ConferenceActivity : AppCompatActivity() {
    lateinit var meetingID:String
    lateinit var username:String
    lateinit var meetingIDTextView: TextView
    lateinit var shareButtonImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_conference)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        meetingIDTextView=findViewById(R.id.meeting_id_textview)
        shareButtonImageView=findViewById(R.id.share_button_imageview)

        meetingID=intent.getStringExtra("MEETING_ID").toString()
        username=intent.getStringExtra("USERNAME").toString()
        meetingIDTextView.setText("MEETING ID"+meetingID)
        addFragment()
        shareButtonImageView.setOnClickListener {
            val shareMessage = "Join meeting with this Meeting ID on StreamLink: $meetingID"
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareMessage)
                type = "text/plain"
            }


        startActivity(Intent.createChooser(shareIntent,"Share to:"))
        }
    }
    fun shareMeetingID(){

    }

    fun addFragment() {
        val appID: Long = AppConstants.appId
        val appSign: String = AppConstants.appSign

        val conferenceID: String = meetingID
        val userID: String = username
        val userName: String = username

        val config = ZegoUIKitPrebuiltVideoConferenceConfig()
        val fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
            appID,
            appSign,
            userID,
            userName,
            conferenceID,
            config
        )

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
    }
}