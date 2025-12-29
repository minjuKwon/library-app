package com.example.library.common

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object TestUtil {

    fun waitForToast(text: Int, timeout: Long = 6000L) {
        val end = System.currentTimeMillis() + timeout
        var lastError: Throwable? = null

        while (System.currentTimeMillis() < end) {
            try {
                onView(withText(text))
                    .inRoot(withToast())
                    .check(matches(withText(text)))
                return // 성공하면 즉시 종료
            } catch (e: Throwable) {
                lastError = e
                //timeout 범위 안이면, 대기 후 재시도
                Thread.sleep(200)
            }
        }
        throw AssertionError("Toast with text <$text> not found within $timeout ms").apply {
            initCause(lastError)
        }
    }

    private fun withToast():Matcher<Root>{
        return object:TypeSafeMatcher<Root>(){
            override fun matchesSafely(item: Root): Boolean {
                return !item.decorView.hasWindowFocus()
            }
            override fun describeTo(description: Description?) {
                description?.appendText("is a toast")
            }
        }
    }

}