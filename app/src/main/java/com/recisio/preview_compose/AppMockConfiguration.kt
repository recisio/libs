package com.recisio.preview_compose

import com.recisio.compose_mock_preview.annotations.ComposeMockPreviewConfiguration
import com.recisio.compose_mock_preview.configuration.ComposeMockPreviewConfig

@ComposeMockPreviewConfiguration
class AppMockConfiguration : ComposeMockPreviewConfig {
    override val longMessageList: List<String> = listOf("Very very long message")
    override val shortMessageList: List<String> = listOf("short message")
    override val resMessageList: List<Int> = listOf(R.string.app_name)
    override val resDrawableList: List<Int> = listOf(R.drawable.ic_launcher_background)
    override val resColorList: List<Int> = listOf(R.color.purple_700)
}