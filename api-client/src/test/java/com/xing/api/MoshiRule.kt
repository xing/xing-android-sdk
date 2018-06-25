package com.xing.api

import com.serjltt.moshi.adapters.FallbackEnum
import com.squareup.moshi.Moshi
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import com.serjltt.moshi.adapters.FallbackOnNull
import com.xing.api.internal.json.*

class MoshiRule : TestRule {

    lateinit var moshi: Moshi

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            moshi = buildMoshi()
            base.evaluate()
        }
    }

    internal fun buildMoshi(): Moshi {
        return Moshi.Builder()
                .add(FallbackOnNull.ADAPTER_FACTORY)
                .add(FallbackEnum.ADAPTER_FACTORY)
                .add(SafeEnumJsonAdapter.FACTORY)
                .add(ContactPathJsonAdapter.FACTORY)
                .add(BirthDateJsonAdapter.FACTORY)
                .add(SafeCalendarJsonAdapter.FACTORY)
                .add(PhoneJsonAdapter.FACTORY)
                .add(CsvCollectionJsonAdapter.FACTORY)
                .add(GeoCodeJsonAdapter.FACTORY)
                .add(TimeZoneJsonAdapter.FACTORY)
                .add(ContactPathJsonAdapter.FACTORY)
                .add(SafeCalendarJsonAdapter.FACTORY)
                .build()
    }
}
