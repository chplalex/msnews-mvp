package com.chpl.base

import com.chpl.base.data.CategoryType
import com.chpl.base.data.CountryType
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test() {
        val type = CategoryType.BUSINESS

        println("type = ${type.label}")
    }
}