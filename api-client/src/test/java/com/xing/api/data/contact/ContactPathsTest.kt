package com.xing.api.data.contact

import com.xing.api.MoshiRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test

class ContactPathsTest {

    @JvmField @Rule
    val moshiRule = MoshiRule()

    private val contactPathsJson = """
            {
                "paths": [],
                "distance": 0,
                "total": 0
            }
        """

    @Test
    fun `converts json response to the expected contact paths model`() {
        val expectedContactPaths = ContactPaths(
                paths = emptyList(),
                distance = 0,
                total = 0
        )
        with(moshiRule.moshi.adapter(ContactPaths::class.java)) {
            assertThat(fromJson(contactPathsJson)).isEqualTo(expectedContactPaths)
        }
    }
}