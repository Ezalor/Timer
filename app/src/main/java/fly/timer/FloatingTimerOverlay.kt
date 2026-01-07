package fly.timer

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import fly.timer.R

class FloatingTimerOverlay(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val handler = Handler()
    private var startTime = 0L
    private lateinit var timerTextView: TextView
    private lateinit var floatingView: android.view.View

    private val updateInterval = 100L

    private val updateRunnable = object : Runnable {
        override fun run() {
            val elapsed = System.currentTimeMillis() - startTime
            val seconds = elapsed / 1000
            val millis = (elapsed % 1000) / 10
            timerTextView.text = String.format("%d.%02d s", seconds, millis)
            handler.postDelayed(this, updateInterval)
        }
    }

    fun show() {
        if (::floatingView.isInitialized) return

        val inflater = LayoutInflater.from(context)
        floatingView = inflater.inflate(R.layout.layout_floating_timer, null)
        timerTextView = floatingView.findViewById(R.id.timerTextView)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 100
        params.y = 100

        windowManager.addView(floatingView, params)
        resetTimer()
    }

    fun resetTimer() {
        startTime = System.currentTimeMillis()
        handler.removeCallbacks(updateRunnable)
        handler.post(updateRunnable)
    }

    fun hide() {
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
        handler.removeCallbacks(updateRunnable)
    }
}
