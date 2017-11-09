/*
 * Copyright (С) 2016 XING SE (http://xing.com/)
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
package com.xing.api.resources;

import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.profile.Industry;
import com.xing.api.data.profile.Language;

import java.util.List;
import java.util.Map;

import static com.squareup.moshi.Types.newParameterizedType;

/**
 * Represents the <a href="https://dev.xing.com/docs/resources#miscellaneous">Miscellaneous</a> resource.
 *
 * @author daniel.hartwich
 */
public class MiscellaneousResource extends Resource {
    public static final Resource.Factory FACTORY = new Resource.Factory(MiscellaneousResource.class) {
        @Override public Resource create(XingApi api) {
            return new MiscellaneousResource(api);
        }
    };

    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    MiscellaneousResource(XingApi api) {
        super(api);
    }

    /**
     * Get translated industry information. May be used throughout various XING APIs.
     * <p>
     * <b>Available languages:</b> {@code Language.EN, Language.DE, Language.ES, Language.FR, Language.IT,
     * Language.PT, Language.NL, Language.ZH, Language.RU, Language.PL, Language.TR}
     * <p>
     * For more information, please see the <a href="https://dev.xing.com/docs/industries">industries
     * documentation</a>.
     *
     * @param language The requested language.
     * @return A {@linkplain CallSpec callSpec} object ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/misc/industries">'Get translated industry information' resource
     * page.</a>
     */
    public CallSpec<Map<Language, List<Industry>>, HttpError> getTranslatedIndustries(Language language) {
        return Resource.<Map<Language, List<Industry>>, HttpError>newGetSpec(api, "/v1/misc/industries")
              .responseAs(newParameterizedType(Map.class, Language.class,
                    newParameterizedType(List.class, Industry.class)))
              .queryParam("languages", language)
              .build();
    }
}
