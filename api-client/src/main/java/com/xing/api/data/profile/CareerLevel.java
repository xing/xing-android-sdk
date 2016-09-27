/*
 * Copyright (С) 2016 XING AG (http://xing.com/)
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
package com.xing.api.data.profile;

/**
 * Possible values for the user's career level at a {@link Company} or a {@linkplain com.xing.api.data.jobs.Job Job}.
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile Resource</a>
 */
public enum CareerLevel {
    STUDENT_INTERN,
    ENTRY_LEVEL,
    PROFESSIONAL_EXPERIENCED,
    MANAGER_SUPERVISOR,
    EXECUTIVE,
    SENIOR_EXECUTIVE
}
