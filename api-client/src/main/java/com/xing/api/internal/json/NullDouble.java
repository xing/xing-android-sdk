package com.xing.api.internal.json;

/**
 * @author cristian.monforte
 */

import com.squareup.moshi.JsonQualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** Represents a double value, that accepts {@code null}. */
@Documented
@JsonQualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface NullDouble {
}
