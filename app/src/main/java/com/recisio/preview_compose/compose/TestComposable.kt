import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.recisio.compose_mock_preview.annotations.ComposeMockPreviewProvider
import com.recisio.compose_mock_preview.annotations.LongMessagePreview

@ComposeMockPreviewProvider
data class TestComposable(
    @LongMessagePreview val title: String,
    val enabled: Boolean,
    val description: String,
    val position: Int,
    @ColorRes val color: Int,
    @DrawableRes val drawable: Int,
    val child: ChildTestComposable
)

@ComposeMockPreviewProvider
data class ChildTestComposable(
    val title: String,
    val description: String
)