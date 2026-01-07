package fly.timer

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class FloatingTimerAccessibilityService : AccessibilityService() {

    private lateinit var floatingOverlay: FloatingTimerOverlay

    override fun onServiceConnected() {
        super.onServiceConnected()
        floatingOverlay = FloatingTimerOverlay(this)
        floatingOverlay.show()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // 监听触摸开始事件，重置计时器
        if (event.eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START) {
            floatingOverlay.resetTimer()
        }
    }

    override fun onInterrupt() {}

    override fun onUnbind(intent: android.content.Intent?): Boolean {
        floatingOverlay.hide()
        return super.onUnbind(intent)
    }
}
