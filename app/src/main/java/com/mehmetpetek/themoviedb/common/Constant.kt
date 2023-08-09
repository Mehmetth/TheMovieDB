package com.mehmetpetek.themoviedb.common

import com.google.android.exoplayer2.C

object Constant {
    const val MOVIE_ID = "movieId"

    object DrmMovie {
        const val URL = "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd"
        const val DRM_LICENSE_URL =
            "https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test"
        val DRM_SCHEME = C.WIDEVINE_UUID
        const val USER_AGENT = "ExoPlayer-Drm"
    }
}