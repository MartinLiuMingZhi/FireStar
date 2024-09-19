package com.example.firestar.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource2
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.example.firestar.R
import com.example.firestar.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private var playerView:PlayerView ? = null
    private var player: ExoPlayer? = null // 声明 ExoPlayer 为成员变量

    private var _binding: FragmentVideoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(VideoViewModel::class.java)

        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        val root: View = binding.root

         playerView = binding.playerView

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer() // 释放播放器
        _binding = null
    }

    // 初始化播放器的方法
    @OptIn(UnstableApi::class)
    private fun initializePlayer() {
        // 检查播放器是否已经初始化
        if (player == null) {
            val trackSelector = DefaultTrackSelector(requireContext()).apply {
                setParameters(buildUponParameters().setMaxVideoSizeSd())
            }
            player = ExoPlayer.Builder(requireContext()).setTrackSelector(trackSelector).build()
//            player = ExoPlayer.Builder(requireContext()).build()
            playerView?.player = player

            // 创建媒体项
            val firstItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
            val secondItem = MediaItem.fromUri(getString(R.string.media_url_mp3))
            val thirdItem = MediaItem.Builder().setUri((getString(R.string.media_url_dash))).setMimeType(MimeTypes.APPLICATION_MPD).build()
//            val thirdItem = MediaItem.fromUri("https://upos-hz-mirrorakam.akamaized.net/upgcxcode/89/22/25805262289/25805262289-1-100026.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1726223338&gen=playurlv2&os=akam&oi=1022961578&trid=efc2f37fc2d34b37b725a7603034e1d0u&mid=1410474192&platform=pc&og=cos&upsig=9edbc3ea11efa863484061db15a60e52&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&hdnts=exp=1726223338~hmac=be0eca418dc21171bfa57649443a8334f536d5ee502acac6350bb274daeedc58&bvc=vod&nettype=0&orderid=0,1&buvid=25AE2F82-BE2A-AC3F-5A46-1A9DCAB9497781400infoc&build=0&f=u_0_0&agrr=1&bw=230351&logo=80000000")
//            val forthItem = MediaItem.fromUri("https://upos-hz-mirrorakam.akamaized.net/upgcxcode/89/22/25805262289/25805262289-1-30280.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1726223338&gen=playurlv2&os=akam&oi=1022961578&trid=efc2f37fc2d34b37b725a7603034e1d0u&mid=1410474192&platform=pc&og=hw&upsig=5fb5cdfabfc903671d853ecc34bd551e&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&hdnts=exp=1726223338~hmac=46baaa927b9e027023c05ffc98ba6a8123f522cc3c563f80bb224d724b9230ff&bvc=vod&nettype=0&orderid=0,1&buvid=25AE2F82-BE2A-AC3F-5A46-1A9DCAB9497781400infoc&build=0&f=u_0_0&agrr=1&bw=14165&logo=80000000")

            // 创建音频和视频的 MediaItem
            val videoItem = MediaItem.fromUri(getString(R.string.video_url_1))
            val audioItem = MediaItem.fromUri(getString(R.string.audio_url_1))

            val dataSourceFactory = DefaultDataSource.Factory(requireContext())
            // 创建媒体源
            val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoItem)
            val audioSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(audioItem)

            // 合并视频和音频源
            val mergingMediaSource = MergingMediaSource(videoSource, audioSource)

            // 添加媒体项到播放器
            player?.addMediaItem(firstItem)
            player?.addMediaItem(secondItem)
            player?.addMediaItem(thirdItem)
//            player?.addMediaItem(forthItem)

            // 添加合并后的媒体源到播放器
            player?.addMediaSource(mergingMediaSource)

            // 创建串联媒体源
//            val concatenatedSource = ConcatenatingMediaSource(
//                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(firstItem),
//                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(secondItem),
//                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(thirdItem),
//                mergingMediaSource
//            )

            // 设置媒体源到播放器
//            player?.setMediaSource(concatenatedSource)
            // 准备播放器
            player?.prepare()
            player?.playWhenReady = true // 自动播放
        }
    }

    // 释放播放器的方法
    private fun releasePlayer() {
        playerView?.player = null // 清除 PlayerView 的引用
        player?.release()
        player = null
    }

    // 暂停播放，防止内存泄漏
    override fun onPause() {
        super.onPause()
//        player?.pause()
        player?.playWhenReady = false // 暂停播放
    }

    // 恢复播放
    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true // 恢复播放
    }
}