package com.xing.api.internal.json;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cristian.monforte
 */
@SuppressWarnings("ALL")
public class NullDoubleJsonAdapterTest {
    private static final Set<Annotation> WITH_NO_ANNOTATIONS = Collections.emptySet();
    private static final Set<Annotation> WITH_NI_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_NI_ANNOTATION = new HashSet<>(1);
    private static final Set<Annotation> WITH_NO_NI_ANNOTATION2 = new HashSet<>(2);

    static {
        WITH_NI_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return NullDouble.class;
            }
        });

        WITH_NO_NI_ANNOTATION.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });

        WITH_NO_NI_ANNOTATION2.addAll(WITH_NO_NI_ANNOTATION);
        WITH_NO_NI_ANNOTATION2.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
    }

    private final Moshi moshi = new Moshi.Builder().build();

    @Test
    public void ignoresUnsupportedTypes() throws Exception {
        assertThat(nullDoubleAdapter(double.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(nullDoubleAdapter(Double.class, WITH_NO_ANNOTATIONS)).isNull();
        assertThat(nullDoubleAdapter(double.class, WITH_NO_NI_ANNOTATION)).isNull();
        assertThat(nullDoubleAdapter(double.class, WITH_NO_NI_ANNOTATION2)).isNull();
        assertThat(nullDoubleAdapter(String.class, WITH_NI_ANNOTATION)).isNull();
    }


    @Test
    public void intValues() throws Exception {
        assertThat(fromJson("0.0")).isEqualTo(0D);
        assertThat(fromJson("3.0")).isEqualTo(3D);
        assertThat(fromJson("-56.0")).isEqualTo(-56D);

        assertThat(toJson(0D)).isEqualTo("0.0");
        assertThat(toJson(2D)).isEqualTo("2.0");
        assertThat(toJson(-42D)).isEqualTo("-42.0");
    }

    @Test
    public void nullValues() throws Exception {
        assertThat(fromJson("null")).isEqualTo(-1);
        assertThat(toJson(-1D)).isEqualTo("null");
        assertThat(toJson(null)).isEqualTo("null");
    }

    protected String toJson(Double value) throws IOException {
        JsonAdapter<Double> adapter = nullDoubleAdapter(double.class, WITH_NI_ANNOTATION);
        return adapter.lenient().toJson(value);
    }

    private Double fromJson(String json) throws IOException {
        JsonAdapter<Double> adapter = nullDoubleAdapter(double.class, WITH_NI_ANNOTATION);
        return adapter.lenient().fromJson(json);
    }

    @SuppressWarnings("unchecked")
    private <T> JsonAdapter<T> nullDoubleAdapter(Type type,
          Set<? extends Annotation> annotations) {
        return (JsonAdapter<T>) NullDoubleJsonAdapter.FACTORY.create(type, annotations, moshi);
    }
}
