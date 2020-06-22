package com.example.drinkwithme.ui.drawing

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.drinkwithme.R
import com.example.drinkwithme.model.TestResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*
import kotlin.math.max
import kotlin.math.pow

const val frameTime = 1000 / 60

class DrawCircleView(context: Context) : View(context) {

    private var isStarted = false
    private var isFinished = false

    // How many times user touched the circle
    private var mCount = 0

    private var mPreviousTouchTime: Long = System.currentTimeMillis()
    private var mPreviousCircleTime: Long = System.currentTimeMillis()
    private var mPreviousFrameTime: Long = System.currentTimeMillis()
    private var mCurrentInterval: Long = 1000 / (mCount.toLong() + 1)

    private var mTimeLeft: Long = 10000

    // Generate random coordinates for circle
    private var rand = Random()

    private var circleRadius: Int = 0
    private var dR: Int = 0
    private var circleX: Int = 0
    private var circleY: Int = 0

    // What color is used
    private var mCirclePaint = Paint()
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mCirclePaint.color = Color.argb(80, 0, 0, 0)

        mTextPaint.style = Paint.Style.STROKE
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {

        if (!isStarted) {
            mTextPaint.textSize = 50f
            canvas.drawText(
                context.getString(R.string.start_text),
                (measuredWidth / 2).toFloat(),
                (3 * measuredHeight / 4).toFloat(),
                mTextPaint
            )
            return
        }

        mTextPaint.textSize = 200f
        canvas.drawText(
            mCount.toString(),
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 4).toFloat(),
            mTextPaint
        )

        if (isFinished) {
            mTextPaint.textSize = 50f

            var text = "You are OK"

            when {
                mCount < 2 -> {
                    text = "You're really drunk"
                }
                mCount in 2..3 -> {
                    text = "You're drunk"
                }
                mCount in 4..5 -> {
                    text = "You're almost fine"
                }
            }
            canvas.drawText(
                text,
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2).toFloat(),
                mTextPaint
            )

            val drunk = "Judging by test, you are ${max(0, 6 - mCount)}%% drunk"
            canvas.drawText(
                drunk,
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2 + 100).toFloat(),
                mTextPaint
            )

            canvas.drawText(
                context.getString(R.string.end_text),
                (measuredWidth / 2).toFloat(),
                (3 * measuredHeight / 4).toFloat(),
                mTextPaint
            )
            return
        }

        mTextPaint.textSize = 50f
        val timeLeft =
            max(0f, ((mTimeLeft - (System.currentTimeMillis() - mPreviousTouchTime)) / 1000f))
        if (timeLeft == 0f) {
            isFinished = true
        }
        canvas.drawText(
            "$timeLeft sec",
            (measuredWidth / 2).toFloat(),
            (measuredHeight / 4).toFloat() + 100,
            mTextPaint
        )

        if (circleRadius <= 0 || System.currentTimeMillis() - mPreviousCircleTime > mCurrentInterval) {
            createCircle()
        }

        canvas.drawCircle(
            circleX.toFloat(),
            circleY.toFloat(),
            circleRadius.toFloat(),
            mCirclePaint
        )

        circleRadius -= dR


        val interval = System.currentTimeMillis() - mPreviousFrameTime

        if (interval > frameTime) {
            sleep(max(1, frameTime - interval))
        }

        mPreviousFrameTime = System.currentTimeMillis()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val result = super.onTouchEvent(event!!)
        if (!isStarted) {
            isStarted = true
            mPreviousTouchTime = System.currentTimeMillis()
            mPreviousCircleTime = System.currentTimeMillis()
            mPreviousFrameTime = System.currentTimeMillis()
            invalidate()
            return result
        }
        if (isFinished) {
            saveResults()

            (context as AppCompatActivity).supportFragmentManager.popBackStack()
            (context as AppCompatActivity).supportFragmentManager.popBackStack()
        }

        val x = event.x
        val y = event.y

        if ((circleX - x).pow(2) + (circleY - y).pow(2) <= circleRadius.toFloat().pow(2)) {

            mCount++
            createCircle()
            mPreviousTouchTime = System.currentTimeMillis()
            mCurrentInterval = 3000 / (mCount.toLong() + 1)
        }
        return result
    }

    private fun saveResults() {
        val database = DrinkWithMeRoomDatabase.getDatabase(context)
        val drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(
                database.drinkDao(),
                database.drinkResultDao(),
                database.testResultDao()
            )

        val testResult = TestResult(mCount, max(0, 6 - mCount))
        GlobalScope.launch(Dispatchers.IO) {
            drinkWithMeRepository.insertTestResult(testResult)
        }
    }

    private fun createCircle() {
        mPreviousCircleTime = System.currentTimeMillis()

        circleRadius = rand.nextInt(measuredWidth / (3 + mCount)) + 100
        circleX = rand.nextInt(measuredWidth - 2 * circleRadius) + circleRadius
        circleY = rand.nextInt(measuredHeight - 2 * circleRadius) + circleRadius
        dR = circleRadius / (mCurrentInterval.toInt() / frameTime) + 1
    }

}
