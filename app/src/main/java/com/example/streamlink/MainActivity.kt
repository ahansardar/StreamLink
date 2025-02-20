package com.example.streamlink

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.streamlink.ui.theme.StreamLinkTheme
import kotlinx.coroutines.delay
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import androidx.compose.animation.Crossfade
import androidx.media3.common.VideoSize
import androidx.media3.common.util.Util
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamLinkTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var showSplash by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

        if (originalVolume == 0) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 2, 0)
        }

        delay(2700) // Play video for 2.7s before fading

        showSplash = false
    }

    Crossfade(targetState = showSplash, animationSpec = tween(durationMillis = 700)) { isSplash ->
        if (isSplash) {
            VideoSplashScreen { showSplash = false }
        } else {
            LoginPage()
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoSplashScreen(onVideoEnd: () -> Unit) {
    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/splash_screen")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            repeatMode = Player.REPEAT_MODE_OFF
            playWhenReady = true
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING // FIXED SCALING MODE
        }
    }

    // Detect when the video ends
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    onVideoEnd()
                    exoPlayer.release() // Release when finished
                }
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { PlayerView(context).apply {
                player = exoPlayer
                useController = false // Hide video controls
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL // Full-screen fix
            }},
            modifier = Modifier.fillMaxSize()
        )
    }
}
