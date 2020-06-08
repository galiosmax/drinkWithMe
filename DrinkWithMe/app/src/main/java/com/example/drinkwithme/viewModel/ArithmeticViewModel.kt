package com.example.drinkwithme.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.example.drinkwithme.R
import com.example.drinkwithme.model.TestResult
import com.example.drinkwithme.repository.DrinkWithMeRepository
import com.example.drinkwithme.room.DrinkWithMeRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.max

const val timeLimit = 10000f
const val defaultValue = "Press any button to continue..."

class ArithmeticViewModel(application: Application, var onFinished: () -> Unit) :
    AndroidViewModel(application) {

    enum class Option {
        A, B, C, D, NONE;

        companion object {
            fun getByValue(number: Int): Option {
                return when (number) {
                    1 -> A
                    2 -> B
                    3 -> C
                    4 -> D
                    else -> NONE
                }
            }
        }
    }

    enum class Operation {
        PLUS, MULTIPLY, DIVIDE, DOUBLE_PLUS, PLUS_MULTIPLY, MULTIPLY_PLUS, DIVIDE_PLUS, PLUS_DIVIDE, DOUBLE_MULTIPLY, BRACES_PLUS_MULTIPLY;

        companion object {
            fun getByValue(number: Int): Operation {
                return when (number) {
                    1 -> PLUS
                    2 -> MULTIPLY
                    3 -> DIVIDE
                    4 -> DOUBLE_PLUS
                    5 -> PLUS_MULTIPLY
                    6 -> MULTIPLY_PLUS
                    7 -> DIVIDE_PLUS
                    8 -> PLUS_DIVIDE
                    9 -> DOUBLE_MULTIPLY
                    10 -> BRACES_PLUS_MULTIPLY
                    else -> BRACES_PLUS_MULTIPLY
                }
            }
        }
    }

    private var isStarted = false
    private var isFinished = false

    private lateinit var thread: TimeLimitThread

    var mStartTime: Long = 0
    var mTimeLeft = ObservableField(0f)

    var mScore = ObservableField(0)

    private val rand = Random()

    var mExpression = ObservableField(defaultValue)

    var optionA = ObservableField("OptionA")
    var optionB = ObservableField("OptionB")
    var optionC = ObservableField("OptionC")
    var optionD = ObservableField("OptionD")

    private var mCorrect = Option.NONE

    var colorA = ObservableField(R.color.black)
    var colorB = ObservableField(R.color.black)
    var colorC = ObservableField(R.color.black)
    var colorD = ObservableField(R.color.black)

    fun onClickedOption(chosen: Option) {
        if (isFinished) {
            saveResults()
            onFinished()
            return
        }
        if (!isStarted) {

            createExpression()

            colorA.set(R.color.black)
            colorB.set(R.color.black)
            colorC.set(R.color.black)
            colorD.set(R.color.black)

            mTimeLeft.set(timeLimit)
            mStartTime = System.currentTimeMillis()
            thread = TimeLimitThread()
            thread.isRunning = true
            thread.start()
            isStarted = true
            return
        }
        thread.isRunning = false
        isStarted = false

        val isCorrect = chosen == mCorrect

        if (isCorrect) {
            mScore.set(mScore.get()?.plus(1))
        }
        if (!isCorrect || mScore.get() == 10) {
            isFinished = true
        }

        colorA.set(if (mCorrect == Option.A) R.color.green else R.color.red)
        colorB.set(if (mCorrect == Option.B) R.color.green else R.color.red)
        colorC.set(if (mCorrect == Option.C) R.color.green else R.color.red)
        colorD.set(if (mCorrect == Option.D) R.color.green else R.color.red)

        mExpression.set(
            if (isFinished) "Judging by test, you are ${max(
                0,
                6 - mScore.get()!!
            )}%% drunk" else defaultValue
        )
    }

    private fun saveResults() {
        val database = DrinkWithMeRoomDatabase.getDatabase(getApplication())
        val drinkWithMeRepository =
            DrinkWithMeRepository.getRepository(
                database.drinkDao(),
                database.drinkResultDao(),
                database.testResultDao()
            )

        val score = mScore.get() ?: 6
        val testResult = TestResult(score, max(0, 6 - score))
        GlobalScope.launch(Dispatchers.IO) {
            drinkWithMeRepository.insertTestResult(testResult)
        }
    }

    private fun createExpression() {

        val currentScore = mScore.get()!!.toInt() + 1
        val bound = 10 + currentScore

        val a = rand.nextInt(bound) + 1
        val b = rand.nextInt(bound) + 1
        val c = rand.nextInt(bound) + 1

        val correctValue = rand.nextInt(4) + 1

        mCorrect = Option.getByValue(correctValue)

        val operationVal = rand.nextInt(currentScore) + 1

        when (Operation.getByValue(operationVal)) {
            Operation.PLUS -> showOptions("$a + $b", a + b)
            Operation.MULTIPLY -> showOptions("$a * $b", a * b)
            Operation.DIVIDE -> showOptions("${a * b} / $b", a)
            Operation.DOUBLE_PLUS -> showOptions("$a + $b + $c", a + b + c)
            Operation.PLUS_MULTIPLY -> showOptions("$a + $b * $c", a + b * c)
            Operation.MULTIPLY_PLUS -> showOptions("$a * $b + $c", a * b + c)
            Operation.DIVIDE_PLUS -> showOptions("${a * b} / $b + $c", a + c)
            Operation.PLUS_DIVIDE -> showOptions("$a + ${b * c} / $c", a + b)
            Operation.DOUBLE_MULTIPLY -> showOptions("$a * $b * $c", a * b * c)
            Operation.BRACES_PLUS_MULTIPLY -> showOptions("($a + $b) * $c", (a + b) * c)
        }
    }

    private fun showOptions(expression: String, result: Int) {
        mExpression.set(expression)

        var bound = result

        var randomFirst = rand.nextInt(bound) + result / 2
        while (randomFirst == result) {
            bound++
            randomFirst = rand.nextInt(bound) + result / 2
        }

        var randomSecond = rand.nextInt(bound) + result / 2
        while (randomSecond == result || randomSecond == randomFirst) {
            bound++
            randomSecond = rand.nextInt(bound) + result / 2
        }

        var randomThird = rand.nextInt(bound) + result / 2
        while (randomThird == result || randomThird == randomFirst || randomThird == randomSecond) {
            bound++
            randomThird = rand.nextInt(bound) + result / 2
        }

        when (mCorrect) {
            Option.A -> {
                optionA.set(result.toString())
                optionB.set(randomFirst.toString())
                optionC.set(randomSecond.toString())
                optionD.set(randomThird.toString())
            }
            Option.B -> {
                optionA.set(randomFirst.toString())
                optionB.set(result.toString())
                optionC.set(randomSecond.toString())
                optionD.set(randomThird.toString())
            }
            Option.C -> {
                optionA.set(randomFirst.toString())
                optionB.set(randomSecond.toString())
                optionC.set(result.toString())
                optionD.set(randomThird.toString())
            }
            Option.D -> {
                optionA.set(randomFirst.toString())
                optionB.set(randomSecond.toString())
                optionC.set(randomThird.toString())
                optionD.set(result.toString())
            }
            Option.NONE -> {
                // Do nothing here
            }
        }
    }

    inner class TimeLimitThread : Thread() {

        var isRunning = false

        override fun run() {
            while (isRunning) {
                mTimeLeft.set(
                    max(
                        0f,
                        (timeLimit - (System.currentTimeMillis() - mStartTime)) / 1000f
                    )
                )
                if (mTimeLeft.get() == 0f) {
                    onClickedOption(Option.NONE)
                }
            }
        }

    }

}

