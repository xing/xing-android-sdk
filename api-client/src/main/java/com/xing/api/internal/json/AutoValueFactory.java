package com.xing.api.internal.json;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

@MoshiAdapterFactory
public abstract class AutoValueFactory implements JsonAdapter.Factory {
    // Static factory method to access the package private generated implementation
    public static JsonAdapter.Factory create() {
        return nullSafe(new AutoValueMoshi_AutoValueFactory());
    }

    /**
     * Ensures that the generated adapter is a null safe one.
     * <p>
     * Temporary fix! See https://github.com/rharter/auto-value-moshi/issues/25
     */
    public static JsonAdapter.Factory nullSafe(final JsonAdapter.Factory wrappedFactory) {
        return new JsonAdapter.Factory() {
            @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
                JsonAdapter<?> jsonAdapter = wrappedFactory.create(type, annotations, moshi);
                if (jsonAdapter != null) {
                    return jsonAdapter.nullSafe();
                }
                return null;
            }
        };
    }
}
