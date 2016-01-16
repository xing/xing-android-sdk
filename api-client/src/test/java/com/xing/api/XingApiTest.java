/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xing.api;

import com.squareup.moshi.Moshi;
import com.squareup.okhttp.HttpUrl;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class XingApiTest {
    private XingApi api;

    @Before
    public void setUp() {
        api = new XingApi.Builder().loggedOut().build();
    }

    @Test
    public void getRightResourceImpl() throws Exception {
        Resource resource = api.resource(LegalTestResource.class);
        assertThat(resource).isInstanceOf(LegalTestResource.class);

        LegalTestResource testResource = api.resource(LegalTestResource.class);
        assertThat(testResource).isSameAs(resource);
    }

    @Test
    public void throwsForNonFinalResourceClass() throws Exception {
        try {
            api.resource(IllegalTestResource1.class);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared final.");
        }
    }

    @Test
    public void throwsForNonStaticResourceClass() throws Exception {
        try {
            api.resource(IllegalTestResource2.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared static.");
        }
    }

    @Test
    public void throwsForMethodInnerResourceClass() throws Exception {
        final class InnerTestResource extends Resource {
            InnerTestResource(XingApi api) {
                super(api);
            }
        }

        try {
            api.resource(InnerTestResource.class);
            fail("Should fail on non-static class.");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class must be declared static.");
        }
    }

    @Test
    public void throwsForResourceClassOverridingConstructor() throws Exception {
        try {
            api.resource(LegalTestResourceOverridesConstructor.class);
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(IllegalArgumentException.class)
                  .hasMessage("Resource class malformed.")
                  .hasCauseInstanceOf(NoSuchMethodException.class);
        }
    }

    @SuppressWarnings("ConstantConditions") // Passes nulls intentionally.
    @Test
    public void builder() throws Exception {
        XingApi.Builder builder = new XingApi.Builder();

        try {
            builder.apiEndpoint((String) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint((HttpUrl) null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("apiEndpoint == null");
        }

        try {
            builder.apiEndpoint("http/invalid-url.org");
            fail("Builder should throw on invalid endpoints.");
        } catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage()).isEqualTo("Illegal endpoint URL: http/invalid-url.org");
        }

        try {
            builder.accessSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessSecret == null");
        }

        try {
            builder.accessToken(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("accessToken == null");
        }

        try {
            builder.consumerKey(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerKey == null");
        }

        try {
            builder.consumerSecret(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("consumerSecret == null");
        }

        try {
            builder.client(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("client == null");
        }

        try {
            builder.moshi(null);
            fail("Builder should throw on null values.");
        } catch (NullPointerException expected) {
            assertThat(expected.getMessage()).isEqualTo("moshi == null");
        }

        builder.moshi(new Moshi.Builder().build());
        try {
            builder.moshi(new Moshi.Builder().build());
            fail("Only one Moshi should be allowed.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).isEqualTo("Only one instance of Moshi is allowed");
        }

        try {
            builder.build();
            fail("Build should throw if oauth is not set.");
        } catch (IllegalStateException expected) {
            assertThat(expected.getMessage()).contains("not set");
        }
    }

    static final class LegalTestResource extends Resource {
        LegalTestResource(XingApi api) {
            super(api);
        }
    }

    @SuppressWarnings("ConstantConditions") // Intentional.
    static final class LegalTestResourceOverridesConstructor extends Resource {
        LegalTestResourceOverridesConstructor() {
            super(null);
        }
    }

    static class IllegalTestResource1 extends Resource {
        protected IllegalTestResource1(XingApi api) {
            super(api);
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic") // Intentional.
    final class IllegalTestResource2 extends Resource {
        IllegalTestResource2(XingApi api) {
            super(api);
        }
    }
}
