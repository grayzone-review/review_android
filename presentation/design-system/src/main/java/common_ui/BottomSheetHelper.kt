
import android.content.Context
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

interface BottomSheetStateListener {
    fun onStateChanged(newState: Int)
}

object BottomSheetHelper {
    private var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    private var contentView: ComposeView? = null
    private var inputBarView: ComposeView? = null
    private var dimController: DimController? = null
    private var externalStateListener: BottomSheetStateListener? = null
    private var onDismissRequest: () -> Unit = {}

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
            isFitToContents = false
            expandedOffset = 1
            halfExpandedRatio = 0.7f
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dimController?.hideDim()
                        inputBarView.visibility = View.GONE
                        onDismissRequest()
                    }
                    externalStateListener?.onStateChanged(newState = newState)
                }
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // optional
                }
            })
        }
    }

    fun setBottomSheetStateListener(listener: BottomSheetStateListener) {
        externalStateListener = listener
    }

    fun setDismissHandler(handler: () -> Unit) {
        onDismissRequest = handler
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

    fun show(context: Context) {
        bottomSheetBehavior?.apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
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