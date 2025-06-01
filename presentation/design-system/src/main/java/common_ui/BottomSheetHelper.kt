import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.wear.compose.material.MaterialTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface DimController {
    fun showDim()
    fun hideDim()
}

object BottomSheetHelper {
    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    private var contentView: ComposeView? = null
    private var inputBarView: ComposeView? = null
    private var dimController: DimController? = null

    fun init(
        container: FrameLayout,
        dimController: DimController,
        inputBarView: ComposeView,
        maxHeightDp: Int = 650
    ) {
        val context = container.context
        val maxHeightPx = maxHeightDp.toPx(context)
        this.dimController = dimController
        this.inputBarView = inputBarView

        contentView = ComposeView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }

        container.layoutParams = container.layoutParams?.apply {
            height = maxHeightPx
        }

        container.addView(contentView)

        bottomSheetBehavior = BottomSheetBehavior.from(container).apply {
            isDraggable = true
            isHideable = true
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dimController?.hideDim()
                        inputBarView.visibility = View.GONE
                    }
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // optional
                }
            })
        }
    }

    fun setContent(content: @Composable () -> Unit) {
        contentView?.setContent {
            MaterialTheme() {
                content()
            }
        }
    }

    fun setInputBar(content: @Composable () -> Unit) {
        inputBarView?.let { view ->
            view.setContent {
                MaterialTheme {
                    content()
                }
            }
            view.visibility = View.VISIBLE
        }
    }

    fun show(peekHeightDp: Int = 100, context: Context) {
        bottomSheetBehavior?.apply {
            peekHeight = peekHeightDp.toPx(context)
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        dimController?.showDim()
        inputBarView?.visibility = View.VISIBLE
    }

    fun expand() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        inputBarView?.visibility = View.GONE
    }

    fun isVisible(): Boolean {
        return bottomSheetBehavior?.state != BottomSheetBehavior.STATE_HIDDEN
    }

    private fun Int.toPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).toInt()
}