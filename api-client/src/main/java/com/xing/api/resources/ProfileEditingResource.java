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
package com.xing.api.resources;

import com.squareup.okhttp.RequestBody;
import com.xing.api.CallSpec;
import com.xing.api.HttpError;
import com.xing.api.Resource;
import com.xing.api.XingApi;
import com.xing.api.data.edit.PictureUpload;
import com.xing.api.data.edit.UploadProgress;
import com.xing.api.data.profile.Address;
import com.xing.api.data.profile.Award;
import com.xing.api.data.profile.Company;
import com.xing.api.data.profile.FormOfEmployment;
import com.xing.api.data.profile.Language;
import com.xing.api.data.profile.LanguageSkill;
import com.xing.api.data.profile.MessagingAccount;
import com.xing.api.data.profile.School;
import com.xing.api.data.profile.WebProfile;
import com.xing.api.data.profile.XingUser;
import com.xing.api.internal.Experimental;

import java.util.Calendar;
import java.util.List;

/**
 * Represent the <a href="https://dev.xing.com/docs/resources#profile-editing">'Profile Editing'</a> resource.
 * <p>
 * Provides methods which allow to edit the authorizing {@linkplain XingUser user's} profile information.
 *
 * @author daniel.hartwich
 * @author serj.lotutovici
 */
public final class ProfileEditingResource extends Resource {
    /** Creates a resource instance. This should be the only constructor declared by child classes. */
    ProfileEditingResource(XingApi api) {
        super(api);
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} general profile information.
     * <p>
     * A successful request will return the {@linkplain XingUser user's} updated profile.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>academic_title</b></td>
     * <td>Updates the users academic title. Must be one of "Dr.", "Prof.", "Prof. Dr.", "Ing.", "Dipl.-Ing.", "Mag."
     * or an empty string. Use empty string to delete value.</td>
     * </tr>
     * <tr>
     * <td><b>employment_status</b></td>
     * <td>Updates the users employment status. Must be one of ‘ENTREPRENEUR’, ‘FREELANCER’, ‘EMPLOYEE’, ‘EXECUTIVE’,
     * ‘RECRUITER’, ‘PUBLIC_SERVANT’, ‘UNEMPLOYED’, ‘RETIRED’. See: {@link com.xing.api.data.profile.EmploymentStatus}
     * .</td>
     * </tr>
     * <tr>
     * <td><b>haves</b></td>
     * <td>List of things you have separated by comma (must not be longer than 2048 characters, each item must not
     * be longer than 200 characters, each item must contain a non-whitespace, not more than 200 items)</td>
     * </tr>
     * <tr>
     * <td><b>interests</b></td>
     * <td>List of things you are interested in separated by comma (must not be longer than 2048 characters,
     * each item must not be longer than 200 characters, each item must contain a non-whitespace, not more than 200
     * items).</td>
     * </tr>
     * <tr>
     * <td><b>organisation_member</b></td>
     * <td>List of organizations you are a member of separated by comma (must not be longer than 2048
     * characters, each item must not be longer than 200 characters, each item must contain a non-whitespace, not more
     * than 200 items).</td>
     * </tr>
     * <tr>
     * <td><b>wants</b></td>
     * <td>List of things you want separated by comma (must not be longer than 2048 characters, each item must not
     * be longer than 200 characters, each item must contain a non-whitespace, not more than 200 items).</td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me">'Update users general information' resource page</a>
     */
    public CallSpec<XingUser, HttpError> updateGeneralInformation() {
        return Resource.<XingUser, HttpError>newPutSpec(api, "v1/users/me", false)
              .responseAs(first(XingUser.class, "users"))
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} profile picture.
     * <p>
     * Uploads a new photo and defines it as the users profile picture.
     * Pictures have to be less than 20 MB large. The supported image formats are image/jpeg and image/png.
     * <p>
     * It is sent as application/json with the image data Base64 encoded in the body:
     * { "photo": { "file_name": "test.jpg", "mime_type": "image/jpeg", "content": "Base64 encoded image data" } }
     * <p>
     * The required PictureUpload Object can be generated by using {@link PictureUpload#pictureUploadJPEG(String,
     * byte[])} or {@link PictureUpload#pictureUploadPNG(String, byte[])}
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/photo">'Update users profile picture' resource page</a>
     */
    public CallSpec<Void, HttpError> updateProfilePicture(PictureUpload pictureUpload) {
        return Resource.<Void, HttpError>newPutSpec(api, "v1/users/me/photo", false)
              .responseAs(Void.class)
              .body(single(PictureUpload.class, "photo"), pictureUpload)
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} profile picture. This method is for a more
     * <b>advanced</b> use (like setting a multipart body).
     * <p>
     * Uploads a new photo and defines it as the users profile picture.
     * Pictures have to be less than 20 MB large. The supported image formats are image/jpeg and image/png.
     *
     * @param body Request body to be sent. If {@code null} an empty body will be sent.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/photo">'Update users profile picture' resource page</a>
     */
    public CallSpec<Void, HttpError> updateProfilePicture(RequestBody body) {
        return Resource.<Void, HttpError>newPutSpec(api, "v1/users/me/photo", false)
              .responseAs(Void.class)
              .body(body)
              .build();
    }

    /**
     * Deletes the authorizing {@linkplain XingUser user's} current profile picture.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/photo">'Delete the users current profile picture'
     * resource page</a>
     */
    public CallSpec<Void, HttpError> deleteProfilePicture() {
        return Resource.<Void, HttpError>newDeleteSpec(api, "v1/users/me/photo", false)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Returns the profile picture upload progress with percentage.
     * <p>
     * After a photo is uploaded via {@link #updateProfilePicture(PictureUpload)} call, the current upload
     * progress can be obtained by executing <strong>this</strong> call. Possible values for the status field are:
     * IN_PROGRESS, DONE, FAILED.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/get/users/me/photo/progress">'Get profile picture upload progress'
     * resource page</a>
     */
    @Experimental
    public CallSpec<UploadProgress, HttpError> getPictureUploadProgress() {
        return Resource.<UploadProgress, HttpError>newGetSpec(api, "/v1/users/me/photo/progress")
              .responseAs(single(UploadProgress.class, "progress"))
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} private address.
     * <p>
     * If a field is not passed, it will not be updated or will be removed. To delete fields you should provide an
     * empty value along with the field.
     * <p>
     * Possible optional <i>query/form</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>city</b></td>
     * <td>This field can <strong>not</strong> be unset and may contain up to 49 characters and must have at least 2
     * characters</td>
     * </tr>
     * <tr>
     * <td><b>country</b></td>
     * <td>This field can <strong>not</strong> be unset and must be an ISO-3166 country code (some countries require a
     * province to be set)</td>
     * </tr>
     * <tr>
     * <td><b>email</b></td>
     * <td>This field can be empty or must be a valid email address (foo@example.com)</td>
     * </tr>
     * <tr>
     * <td><b>fax</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>mobile_phone</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>phone</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>province</b></td>
     * <td>This field can be empty or may contain up to 79 characters</td>
     * </tr>
     * <tr>
     * <td><b>street</b></td>
     * <td>This field can be empty or may contain up to 49 characters</td>
     * </tr>
     * <tr>
     * <td><b>zip_code</b></td>
     * <td>This field can be empty or may contain up to 19 characters</td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">'Update the users private address'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> updatePrivateAddress() {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/private_address", true)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} private address.
     * <p>
     * A simpler implementation of {@link #updatePrivateAddress()}.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/private_address">'Update the users private address'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> updatePrivateAddress(Address address) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/private_address", false)
              .responseAs(Void.class)
              .body(Address.class, address)
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} business address.
     * <p>
     * If a field is not passed, it will not be updated or will be removed. To delete fields you should provide an
     * empty value along with the field.
     * <p>
     * Possible optional <i>query/form</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>city</b></td>
     * <td>This field can not be unset and may contain up to 49 characters and must have at least 2 characters</td>
     * </tr>
     * <tr>
     * <td><b>country</b></td>
     * <td>This field can not be unset and must be an ISO-3166 country code (some countries require a province to be
     * set)</td>
     * </tr>
     * <tr>
     * <td><b>email</b></td>
     * <td>This field can be empty or must be a valid email address (foo@example.com)</td>
     * </tr>
     * <tr>
     * <td><b>fax</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>mobile_phone</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>phone</b></td>
     * <td>This field can be empty or must be the country code, area code and number separated by a pipe character
     * (|). Use {@link com.xing.api.data.profile.Phone}.</td>
     * </tr>
     * <tr>
     * <td><b>province</b></td>
     * <td>This field can be empty or may contain up to 79 characters</td>
     * </tr>
     * <tr>
     * <td><b>street</b></td>
     * <td>This field can be empty or may contain up to 49 characters</td>
     * </tr>
     * <tr>
     * <td><b>zip_code</b></td>
     * <td>This field can be empty or may contain up to 19 characters</td>
     * </tr>
     * </table>
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/business_address">'Update the users business address'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> updateBusinessAddress() {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/business_address", true)
              .responseAs(Void.class)
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} business address.
     * <p>
     * A simpler implementation of {@link #updatePrivateAddress()}.
     *
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/business_address">'Update the users business address'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> updateBusinessAddress(Address address) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/business_address", false)
              .responseAs(Void.class)
              .body(Address.class, address)
              .build();
    }

    /**
     * Adds a new {@linkplain School school} to the authorizing {@linkplain XingUser user's} education background.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>begin_date</b></td>
     * <td>Start date in format YYYY or YYYY-MM. Use {@link com.xing.api.data.SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>degree</b></td>
     * <td>The (future) degree. Must be less than 80 characters long.</td>
     * </tr>
     * <tr>
     * <td><b>end_date</b></td>
     * <td>End date in format YYYY or YYYY-MM, must be greater than begin_date. Use {@link
     * com.xing.api.data.SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>notes</b></td>
     * <td>Additional notes such as specialized subjects. Must be less than 80 characters long.</td>
     * </tr>
     * <tr>
     * <td><b>subject</b></td>
     * <td>Describes the field of study. For primary school this field must be supplied. Must be less than 80
     * characters long.</td>
     * </tr>
     * </table>
     *
     * @param name Name of the school (must NOT be {@code null}).
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/schools">'Create a school'
     * resource page.</a>
     */
    public CallSpec<School, HttpError> addSchool(String name) {
        return Resource.<School, HttpError>newPostSpec(api, "/v1/users/me/educational_background/schools", true)
              .responseAs(single(School.class, "school"))
              .formField("name", name)
              .build();
    }

    /**
     * Adds a new {@linkplain School school} to the authorizing {@linkplain XingUser user's} education background.
     * <p>
     * A simpler implementation of {@link #addSchool(String)}.
     *
     * @param school New school to add. <strong>name</strong> must NOT be {@code null}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/schools">'Create a school'
     * resource page.</a>
     */
    public CallSpec<School, HttpError> addSchool(School school) {
        return Resource.<School, HttpError>newPostSpec(api, "/v1/users/me/educational_background/schools", false)
              .responseAs(single(School.class, "school"))
              .body(School.class, school)
              .build();
    }

    /**
     * Updates the specified {@linkplain School school} from the authorizing {@linkplain XingUser user's} educational
     * background.
     * <p>
     * Fields that are not provided are considered as not to be updated. Sending an empty field will unset it.
     * The <strong>subject</strong> field can’t be empty when the specified school is marked as primary school.
     * <p>
     * Possible optional <i>query</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>name</b></td>
     * <td>Name of the school. Must be less than 80 characters long.</td>
     * </tr>
     * <tr>
     * <td><b>begin_date</b></td>
     * <td>Start date in format YYYY or YYYY-MM. Use {@link com.xing.api.data.SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>degree</b></td>
     * <td>The (future) degree. Must be less than 80 characters long.</td>
     * </tr>
     * <tr>
     * <td><b>end_date</b></td>
     * <td>End date in format YYYY or YYYY-MM, must be greater than begin_date.  Use {@link
     * com.xing.api.data.SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>notes</b></td>
     * <td>Additional notes such as specialized subjects. Must be less than 80 characters long.</td>
     * </tr>
     * <tr>
     * <td><b>subject</b></td>
     * <td>Describes the field of study. For primary school this field must be supplied. Must be less than 80
     * characters long.</td>
     * </tr>
     * </table>
     *
     * @param schoolId Id of the {@link School} to update. Must NOT be {@code null} or empty.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/educational_background/schools/:id">'Update a school'
     * resource page.</a>
     */
    public CallSpec<School, HttpError> updateSchool(String schoolId) {
        return Resource.<School, HttpError>newPutSpec(api, "/v1/users/me/educational_background/schools/{id}", true)
              .responseAs(single(School.class, "school"))
              .pathParam("id", schoolId)
              .build();
    }

    /**
     * Deletes the specified school from the authorizing {@linkplain XingUser user's} educational
     * background.
     * <p>
     * A school that is marked as primary can’t be deleted.
     *
     * @param schoolId Id of the {@link School} to delete. Must NOT be {@code null} or empty.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/educational_background/schools/:id">'Delete a school'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> deleteSchool(String schoolId) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/educational_background/schools/{id}", false)
              .responseAs(Void.class)
              .pathParam("id", schoolId)
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} primary {@link School}.
     * <p>
     * The specified school must have a subject.
     * <p>
     * The {@code schoolId} must be existing. If it does not exists you’ll receive INVALID_PARAMETERS with an
     * UNKNOWN_VALUE for the
     * field school_id.
     *
     * @param schoolId Id of the {@link School} to set. Must NOT be {@code null} or empty and must be existing
     * (otherwise INVALID_PARAMETERS will be returned as an {@link HttpError}).
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/educational_background/primary_school">'Update the users
     * primary school' resource page.</a>
     */
    public CallSpec<Void, HttpError> setSchoolAsPrimary(String schoolId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/educational_background/primary_school", false)
              .responseAs(Void.class)
              .queryParam("school_id", schoolId)
              .build();
    }

    /**
     * Adds a qualification to the authorizing {@linkplain XingUser user's} educational background.
     *
     * @param description Description of the qualification. Max length is 80 characters.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/me/educational_background/qualifications">'Add a
     * qualification' resource page.</a>
     */
    public CallSpec<List<String>, HttpError> addQualification(String description) {
        return Resource.<List<String>, HttpError>newPostSpec(
              api, "/v1/users/me/educational_background/qualifications", false)
              .responseAs(list(String.class, "qualifications"))
              .queryParam("description", description)
              .build();
    }

    /**
     * Adds a new {@linkplain Company company} to the authorizing {@linkplain XingUser user's} professional experience.
     * <p>
     * Possible optional <i>query/form</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>begin_date</b></td>
     * <td>Start date in format YYYY or YYYY-MM. Use {@linkplain com.xing.api.data.SafeCalendar SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>career_level</b></td>
     * <td>Describes the job level. See {@linkplain com.xing.api.data.profile.CareerLevel CareerLevel} for possible
     * values.</td>
     * </tr>
     * <tr>
     * <td><b>company_size</b></td>
     * <td>Describes the company size. See {@linkplain com.xing.api.data.profile.CompanySize CompanySize} for possible
     * values.</td>
     * </tr>
     * <tr>
     * <td><b>description</b></td>
     * <td>Description of the company. Must not be longer than 512 characters.</td>
     * </tr>
     * <tr>
     * <td><b>discipline</b></td>
     * <td>Describes the discipline of the role. See {@linkplain com.xing.api.data.profile.Discipline Discipline} for
     * possible values.</td>
     * </tr>
     * <tr>
     * <td><b>end_date</b></td>
     * <td>End date in format YYYY or YYYY-MM, must be greater than begin_date or {@code null}. Use
     * {@linkplain com.xing.api.data.SafeCalendar SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>industries</b></td>
     * <td>Industries of the company as a comma separated list of ids. See resource web page for more info.</td>
     * </tr>
     * <tr>
     * <td><b>until_now</b></td>
     * <td>If {@code true}, no <strong>end_date</strong> should be given. Instead of an end_date, this job will be
     * displayed as ‘to present’ on the profile.</td>
     * </tr>
     * <tr>
     * <td><b>url</b></td>
     * <td>URL to the company homepage. Must not be longer than 128 characters.</td>
     * </tr>
     * </table>
     *
     * @param name Name of the company. Must not be longer than 80 characters.
     * @param title Job title. Must not be longer than 80 characters.
     * @param formOfEmployment Form of employment. For possible values see {@link FormOfEmployment}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/post/users/me/professional_experience/companies">'Add Company'
     * resource page.</a>
     */
    public CallSpec<Company, HttpError> addCompany(String name, String title, FormOfEmployment formOfEmployment) {
        return Resource.<Company, HttpError>newPostSpec(api, "/v1/users/me/professional_experience/companies", true)
              .responseAs(single(Company.class, "company"))
              .formField("name", name)
              .formField("title", title)
              .formField("form_of_employment", formOfEmployment)
              .build();
    }

    /**
     * Updates the specified {@linkplain Company company} in the authorizing {@linkplain XingUser user's} professional
     * experience.
     * <p>
     * Fields that are not provided are considered as not to be updated. Fields set as {@code null} or emtpy will be
     * unset.
     * <p>
     * Possible optional <i>query/form</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>name</b></td>
     * <td>Name of the company. Must not be longer than 80 characters.</td>
     * </tr>
     * <tr>
     * <td><b>title</b></td>
     * <td>The job title. Must not be longer than 80 characters.</td>
     * </tr>
     * <tr>
     * <td><b>begin_date</b></td>
     * <td>Start date in format YYYY or YYYY-MM. Use {@linkplain com.xing.api.data.SafeCalendar SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>career_level</b></td>
     * <td>Describes the job level. See {@linkplain com.xing.api.data.profile.CareerLevel CareerLevel} for possible
     * values.</td>
     * </tr>
     * <tr>
     * <tr>
     * <td><b>form_of_employment</b></td>
     * <td>Describes the form of employment. See {@linkplain com.xing.api.data.profile.FormOfEmployment
     * FormOfEmployment} for possible values.</td>
     * </tr>
     * <tr>
     * <td><b>company_size</b></td>
     * <td>Describes the company size. See {@linkplain com.xing.api.data.profile.CompanySize CompanySize} for possible
     * values.</td>
     * </tr>
     * <tr>
     * <td><b>description</b></td>
     * <td>Description of the company. Must not be longer than 512 characters.</td>
     * </tr>
     * <tr>
     * <td><b>discipline</b></td>
     * <td>Describes the discipline of the role. See {@linkplain com.xing.api.data.profile.Discipline Discipline} for
     * possible values.</td>
     * </tr>
     * <tr>
     * <td><b>end_date</b></td>
     * <td>End date in format YYYY or YYYY-MM, must be greater than begin_date or {@code null}. Use
     * {@linkplain com.xing.api.data.SafeCalendar SafeCalendar}.</td>
     * </tr>
     * <tr>
     * <td><b>industries</b></td>
     * <td>Industries of the company as a comma separated list of ids. See resource web page for more info.</td>
     * </tr>
     * <tr>
     * <td><b>until_now</b></td>
     * <td>If {@code true}, no <strong>end_date</strong> should be given. Instead of an end_date, this job will be
     * displayed as ‘to present’ on the profile.</td>
     * </tr>
     * <tr>
     * <td><b>url</b></td>
     * <td>URL to the company homepage. Must not be longer than 128 characters.</td>
     * </tr>
     * </table>
     *
     * @param companyId Id of the {@linkplain Company company} to update. Must NOT be {@code null} or empty.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/professional_experience/companies/:id">'Update Company'
     * resource page.</a>
     */
    public CallSpec<Company, HttpError> updateCompany(String companyId) {
        return Resource.<Company, HttpError>newPutSpec(api, "/v1/users/me/professional_experience/companies/{id}", true)
              .responseAs(single(Company.class, "company"))
              .pathParam("id", companyId)
              .build();
    }

    /**
     * Delete the specified {@linkplain Company company} from the authorized {@linkplain XingUser user's}
     * professional experience.
     * <p>
     * A company that is marked as <strong>primary</strong> can NOT be deleted.
     *
     * @param companyId Id of the {@linkplain Company company} to delete. Must NOT be {@code null} or empty.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/professional_experience/companies/:id">'Delete Company'
     * resource page.</a>
     */
    public CallSpec<Void, HttpError> deleteCompany(String companyId) {
        return Resource.<Void, HttpError>newDeleteSpec(api,
              "/v1/users/me/professional_experience/companies/{id}", false)
              .responseAs(Void.class)
              .pathParam("id", companyId)
              .build();
    }

    /**
     * Sets the specified {@linkplain Company company} as the authorised {@linkplain XingUser user's} primary company.
     *
     * @param companyId Id of the {@linkplain Company company} to set as primary. Must NOT be {@code null} or empty.
     * The {@linkplain Company company} must be part of the {@linkplain XingUser user} professional experience.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/professional_experience/primary_company">'Update the users
     * primary company' resource page.</a>
     */
    public CallSpec<Void, HttpError> setCompanyAsPrimary(String companyId) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/professional_experience/primary_company", false)
              .responseAs(Void.class)
              .queryParam("company_id", companyId)
              .build();
    }

    /**
     * Updates the authorising {@linkplain XingUser user's} award list.
     * <p>
     * Note on input: The submitted award list will overwrite any existing awards the {@linkplain XingUser user}
     * already has. Order of the submitted awards is irrelevant.
     *
     * @param awards The list of awards to set.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/professional_experience/awards">'Update the users award
     * list' resource page.</a>
     */
    @Experimental
    public CallSpec<Void, HttpError> updateAwards(List<Award> awards) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/professional_experience/awards", false)
              .responseAs(Void.class)
              .body(list(Award.class, "awards"), awards)
              .build();
    }

    /**
     * Updates the {@linkplain LanguageSkill language skill} for the specified {@linkplain Language language} in the
     * authorized {@linkplain XingUser user's} profile.
     *
     * @param language The language to update. Possible values in {@linkplain Language}. Must NOT be {@code null}.
     * @param skill The language skill to set. Possible values in {@linkplain LanguageSkill}. May be {@code null}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/languages/:language">'Update Language' resource page.</a>
     */
    public CallSpec<Void, HttpError> updateLanguageSkill(Language language, LanguageSkill skill) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/languages/{language}", false)
              .responseAs(Void.class)
              .pathParam("language", language.toString())
              .queryParam("skill", skill)
              .build();
    }

    /**
     * Deletes the specified {@linkplain Language language} form the authorized {@linkplain XingUser user's} profile.
     *
     * @param language The language to delete. Must NOT be {@code null}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/languages/:language">'Delete Language' resource page.</a>
     */
    public CallSpec<Void, HttpError> deleteLanguage(Language language) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/languages/{language}", false)
              .responseAs(Void.class)
              .pathParam("language", language.toString())
              .build();
    }

    /**
     * Updates the authorizing {@linkplain XingUser user's} birth date.
     *
     * @param calendar A calendar instance with 'year', 'month' and 'day' set. It is safer to use
     * {@linkplain com.xing.api.data.SafeCalendar SafeCalendar}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/birth_date">'Update Birth Date' resource page.</a>
     */
    public CallSpec<Void, HttpError> updateBirthDate(Calendar calendar) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/birth_date", true)
              .responseAs(Void.class)
              .formField("day", calendar.get(Calendar.DAY_OF_MONTH))
              .formField("month", calendar.get(Calendar.MONTH))
              .formField("year", calendar.get(Calendar.YEAR))
              .build();
    }

    /**
     * Updates the specified {@linkplain WebProfile web profile} in the authorized {@linkplain XingUser user's}
     * profile.
     *
     * @param profile The web profile to update. Possible values in {@linkplain WebProfile}.
     * @param urls List of urls to set to the web profile. May be empty.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/web_profiles/:profile">'Update Web Profiel' resource
     * page.</a>
     */
    @Experimental
    public CallSpec<Void, HttpError> updateWebProfile(WebProfile profile, List<String> urls) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/web_profiles/{profile}", false)
              .responseAs(Void.class)
              .pathParam("profile", profile.toString())
              .queryParam("url", urls)
              .build();
    }

    /**
     * Deletes the specified {@linkplain WebProfile web profile} from the authorized {@linkplain XingUser user's}
     * profile.
     *
     * @param profile The web profile to delete. Possible values in {@linkplain WebProfile}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/web_profiles/:profile">'Delete Web Profiel' resource
     * page.</a>
     */
    public CallSpec<Void, HttpError> deleteWebProfile(WebProfile profile) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/web_profiles/{profile}", false)
              .responseAs(Void.class)
              .pathParam("profile", profile.toString())
              .build();
    }

    /**
     * Updates the specified {@linkplain MessagingAccount messaging account} of the authorized {@linkplain XingUser
     * user}.
     *
     * @param account The account to update. Possible values in {@linkplain MessagingAccount}. Must not be null.
     * @param name Name of the given account. Max length is 80 characters. Must not be null.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/instant_messaging_accounts/:account">'Update Messaging
     * Account' resource page.</a>
     */
    @Experimental
    public CallSpec<Void, HttpError> updateMessagingAccount(MessagingAccount account, String name) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/instant_messaging_accounts/{account}", false)
              .responseAs(Void.class)
              .pathParam("account", account.toString())
              .queryParam("name", name)
              .build();
    }

    /**
     * Deletes the specified {@linkplain MessagingAccount account} from the authorized {@linkplain XingUser user's}
     * profile.
     *
     * @param account The account to be deleted. Must NOT be {@code null}.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/delete/users/me/instant_messaging_accounts/:account">'Delete Messagin
     * Account' resource page.</a>
     */
    @Experimental
    public CallSpec<Void, HttpError> deleteMessagingAccount(MessagingAccount account) {
        return Resource.<Void, HttpError>newDeleteSpec(api, "/v1/users/me/instant_messaging_accounts/{account}", false)
              .responseAs(Void.class)
              .pathParam("account", account.toString())
              .build();
    }

    /**
     * Updates the legal information (notice) of the authorizing {@linkplain XingUser user}.
     *
     * @param legalInfo Legal information text. Maximum length is 5000 characters.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/me/legal_information">'Update Legal Information' resource
     * page.</a>
     */
    @Experimental
    public CallSpec<Void, HttpError> updateLegalInfo(String legalInfo) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/me/legal_information", false)
              .responseAs(Void.class)
              .queryParam("content", legalInfo)
              .build();
    }

    /**
     * Updates the <strong>specified</strong> {@linkplain XingUser user's} profile message.
     * <p>
     * Please observe the <a href="https://dev.xing.com/docs/policies#increase-virality">policies</a> when
     * posting profile messages.
     * <p>
     * Possible optional <i>query/form</i> parameters:
     * <table>
     * <tr>
     * <th>Parameter Name</th>
     * <th><b>Description</b></th>
     * </tr>
     * <tr>
     * <td><b>public</b></td>
     * <td>Specifies whether the profile message should be visible to everyone (true) or just the user’s direct
     * contacts
     * (false). The default is true. This parameter corresponds to the “only visible to direct contacts” checkbox on a
     * user’s profile page.</td>
     * </tr>
     * </table>
     *
     * @param userId The user id. Must NOT be {@code null} or empty.
     * @param message Profile message to set. Maximum length is 140 characters. If <strong>empty</strong> the current
     * profile message will be deleted.
     * @return A {@linkplain CallSpec callSpec object} ready to execute the request.
     *
     * @see <a href="https://dev.xing.com/docs/put/users/:user_id/profile_message">'Update users profile massage'
     * respurce
     * page.</a>
     */
    public CallSpec<Void, HttpError> updateUsersProfileMessage(String userId, String message) {
        return Resource.<Void, HttpError>newPutSpec(api, "/v1/users/{user_id}/profile_message", true)
              .responseAs(Void.class)
              .pathParam("user_id", userId)
              .queryParam("message", message)
              .build();
    }
}
