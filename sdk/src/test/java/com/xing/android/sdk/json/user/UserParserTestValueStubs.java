/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.json.user;

/**
 * @author serj.lotutovici
 */
public final class UserParserTestValueStubs {

    private UserParserTestValueStubs() {
    }

    static final String LIST_OF_FULL_USERS = "{\n" +
            "   \"users\" :\n" +
            "      [\n" +
            "         {\n" +
            "            \"id\" : \"123456_abcdef\",\n" +
            "            \"first_name\" : \"Max\",\n" +
            "            \"last_name\" : \"Mustermann\",\n" +
            "            \"display_name\" : \"Max Mustermann\",\n" +
            "            \"page_name\" : \"Max_Mustermann\",\n" +
            "            \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "            \"employment_status\" : \"EMPLOYEE\",\n" +
            "            \"gender\" : \"m\",\n" +
            "            \"birth_date\" :\n" +
            "               {\n" +
            "                  \"day\" : 12,\n" +
            "                  \"month\" : 8,\n" +
            "                  \"year\" : 1963\n" +
            "               },\n" +
            "            \"active_email\" : \"max.mustermann@xing.com\",\n" +
            "            \"time_zone\" :\n" +
            "               {\n" +
            "                  \"name\" : \"Europe/Copenhagen\",\n" +
            "                  \"utc_offset\" : 2.0\n" +
            "               },\n" +
            "            \"premium_services\" :\n" +
            "               [\n" +
            "                  \"SEARCH\",\n" +
            "                  \"PRIVATEMESSAGES\"\n" +
            "               ],\n" +
            "            \"badges\" :\n" +
            "               [\n" +
            "                  \"PREMIUM\",\n" +
            "                  \"MODERATOR\"\n" +
            "               ],\n" +
            "            \"wants\" : \"einen neuen Job\",\n" +
            "            \"haves\" : \"viele tolle Skills\",\n" +
            "            \"interests\" : \"Flitzebogen schießen and so on\",\n" +
            "            \"organisation_member\" : \"ACM, GI\",\n" +
            "            \"languages\" :\n" +
            "               {\n" +
            "                  \"de\" : \"NATIVE\",\n" +
            "                  \"en\" : \"FLUENT\",\n" +
            "                  \"fr\" : null,\n" +
            "                  \"zh\" : \"BASIC\"\n" +
            "               },\n" +
            "            \"private_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Privatstraße 1\",\n" +
            "                  \"phone\" : \"49|40|1234560\",\n" +
            "                  \"fax\" : \"||\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max@mustermann.de\",\n" +
            "                  \"mobile_phone\" : \"49|0155|1234567\"\n" +
            "               },\n" +
            "            \"business_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Geschäftsstraße 1a\",\n" +
            "                  \"phone\" : \"49|40|1234569\",\n" +
            "                  \"fax\" : \"49|40|1234561\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max.mustermann@xing.com\",\n" +
            "                  \"mobile_phone\" : \"49|160|66666661\"\n" +
            "               },\n" +
            "            \"web_profiles\" :\n" +
            "               {\n" +
            "                  \"qype\" :\n" +
            "                     [\n" +
            "                        \"http://qype.de/users/foo\"\n" +
            "                     ],\n" +
            "                  \"google+\" :\n" +
            "                     [\n" +
            "                        \"http://plus.google.com/foo\"\n" +
            "                     ],\n" +
            "                  \"other\" :\n" +
            "                     [\n" +
            "                        \"http://blog.example.org\"\n" +
            "                     ],\n" +
            "                  \"homepage\" :\n" +
            "                     [\n" +
            "                        \"http://example.org\",\n" +
            "                        \"http://other-example.org\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"instant_messaging_accounts\" :\n" +
            "               {\n" +
            "                  \"skype\" : \"1122334455\",\n" +
            "                  \"googletalk\" : \"max.mustermann\"\n" +
            "               },\n" +
            "            \"professional_experience\" :\n" +
            "               {\n" +
            "                  \"primary_company\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"1_abcdef\",\n" +
            "                        \"name\" : \"XING AG\",\n" +
            "                        \"title\" : \"Softwareentwickler\",\n" +
            "                        \"company_size\" : \"201-500\",\n" +
            "                        \"tag\" : null,\n" +
            "                        \"url\" : \"http://www.xing.com\",\n" +
            "                        \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                        \"begin_date\" : \"2010-01\",\n" +
            "                        \"description\" : null,\n" +
            "                        \"end_date\" : null,\n" +
            "                        \"industry\" : \"AEROSPACE\",\n" +
            "                        \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                        \"until_now\" : true\n" +
            "                     },\n" +
            "                  \"companies\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"1_abcdef\",\n" +
            "                           \"name\" : \"XING AG\",\n" +
            "                           \"title\" : \"Softwareentwickler\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : null,\n" +
            "                           \"url\" : \"http://www.xing.com\",\n" +
            "                           \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                           \"begin_date\" : \"2010-01\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : null,\n" +
            "                           \"industry\" : \"AEROSPACE\",\n" +
            "                           \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : true\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"24_abcdef\",\n" +
            "                           \"name\" : \"Ninja Ltd.\",\n" +
            "                           \"title\" : \"DevOps\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"NINJA\",\n" +
            "                           \"url\" : \"http://www.ninja-ltd.co.uk\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2009-04\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2010-07\",\n" +
            "                           \"industry\" : \"ALTERNATIVE_MEDICINE\",\n" +
            "                           \"form_of_employment\" : \"OWNER\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"45_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"Wiss. Mitarbeiter\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"OFFIS\",\n" +
            "                           \"url\" : \"http://www.uni.de\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2007\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2008\",\n" +
            "                           \"industry\" : \"APPAREL_AND_FASHION\",\n" +
            "                           \"form_of_employment\" : \"PART_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"176_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"TEST NINJA\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : \"TESTCOMPANY\",\n" +
            "                           \"url\" : null,\n" +
            "                           \"career_level\" : \"ENTRY_LEVEL\",\n" +
            "                           \"begin_date\" : \"1998-12\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"1999-05\",\n" +
            "                           \"industry\" : \"ARTS_AND_CRAFTS\",\n" +
            "                           \"form_of_employment\" : \"INTERN\",\n" +
            "                           \"until_now\" : false\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"awards\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"name\" : \"Awesome Dude Of The Year\",\n" +
            "                           \"date_awarded\" : 2007,\n" +
            "                           \"url\" : null\n" +
            "                        }\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"educational_background\" :\n" +
            "               {\n" +
            "                  \"degree\" : \"MSc CE/CS\",\n" +
            "                  \"primary_school\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"42_abcdef\",\n" +
            "                        \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                        \"degree\" : \"MSc CE/CS\",\n" +
            "                        \"notes\" : null,\n" +
            "                        \"subject\" : null,\n" +
            "                        \"begin_date\" : \"1998-08\",\n" +
            "                        \"end_date\" : \"2005-02\"\n" +
            "                     },\n" +
            "                  \"schools\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"42_abcdef\",\n" +
            "                           \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                           \"degree\" : \"MSc CE/CS\",\n" +
            "                           \"notes\" : null,\n" +
            "                           \"subject\" : null,\n" +
            "                           \"begin_date\" : \"1998-08\",\n" +
            "                           \"end_date\" : \"2005-02\"\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"qualifications\" :\n" +
            "                     [\n" +
            "                        \"TOEFLS\",\n" +
            "                        \"PADI AOWD\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"photo_urls\" :\n" +
            "               {\n" +
            "                  \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "                  \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "                  \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "                  \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "                  \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "                  \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "                  \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "                  \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "                  \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "                  \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "                  \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "                  \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "                  \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "                  \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "               }\n" +
            "         },\n" +
            "         {\n" +
            "            \"id\" : \"123456_abcdef\",\n" +
            "            \"first_name\" : \"Max\",\n" +
            "            \"last_name\" : \"Mustermann\",\n" +
            "            \"display_name\" : \"Max Mustermann\",\n" +
            "            \"page_name\" : \"Max_Mustermann\",\n" +
            "            \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "            \"employment_status\" : \"EMPLOYEE\",\n" +
            "            \"gender\" : \"m\",\n" +
            "            \"birth_date\" :\n" +
            "               {\n" +
            "                  \"day\" : 12,\n" +
            "                  \"month\" : 8,\n" +
            "                  \"year\" : 1963\n" +
            "               },\n" +
            "            \"active_email\" : \"max.mustermann@xing.com\",\n" +
            "            \"time_zone\" :\n" +
            "               {\n" +
            "                  \"name\" : \"Europe/Copenhagen\",\n" +
            "                  \"utc_offset\" : 2.0\n" +
            "               },\n" +
            "            \"premium_services\" :\n" +
            "               [\n" +
            "                  \"SEARCH\",\n" +
            "                  \"PRIVATEMESSAGES\"\n" +
            "               ],\n" +
            "            \"badges\" :\n" +
            "               [\n" +
            "                  \"PREMIUM\",\n" +
            "                  \"MODERATOR\"\n" +
            "               ],\n" +
            "            \"wants\" : \"einen neuen Job\",\n" +
            "            \"haves\" : \"viele tolle Skills\",\n" +
            "            \"interests\" : \"Flitzebogen schießen and so on\",\n" +
            "            \"organisation_member\" : \"ACM, GI\",\n" +
            "            \"languages\" :\n" +
            "               {\n" +
            "                  \"de\" : \"NATIVE\",\n" +
            "                  \"en\" : \"FLUENT\",\n" +
            "                  \"fr\" : null,\n" +
            "                  \"zh\" : \"BASIC\"\n" +
            "               },\n" +
            "            \"private_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Privatstraße 1\",\n" +
            "                  \"phone\" : \"49|40|1234560\",\n" +
            "                  \"fax\" : \"||\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max@mustermann.de\",\n" +
            "                  \"mobile_phone\" : \"49|0155|1234567\"\n" +
            "               },\n" +
            "            \"business_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Geschäftsstraße 1a\",\n" +
            "                  \"phone\" : \"49|40|1234569\",\n" +
            "                  \"fax\" : \"49|40|1234561\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max.mustermann@xing.com\",\n" +
            "                  \"mobile_phone\" : \"49|160|66666661\"\n" +
            "               },\n" +
            "            \"web_profiles\" :\n" +
            "               {\n" +
            "                  \"qype\" :\n" +
            "                     [\n" +
            "                        \"http://qype.de/users/foo\"\n" +
            "                     ],\n" +
            "                  \"google+\" :\n" +
            "                     [\n" +
            "                        \"http://plus.google.com/foo\"\n" +
            "                     ],\n" +
            "                  \"other\" :\n" +
            "                     [\n" +
            "                        \"http://blog.example.org\"\n" +
            "                     ],\n" +
            "                  \"homepage\" :\n" +
            "                     [\n" +
            "                        \"http://example.org\",\n" +
            "                        \"http://other-example.org\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"instant_messaging_accounts\" :\n" +
            "               {\n" +
            "                  \"skype\" : \"1122334455\",\n" +
            "                  \"googletalk\" : \"max.mustermann\"\n" +
            "               },\n" +
            "            \"professional_experience\" :\n" +
            "               {\n" +
            "                  \"primary_company\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"1_abcdef\",\n" +
            "                        \"name\" : \"XING AG\",\n" +
            "                        \"title\" : \"Softwareentwickler\",\n" +
            "                        \"company_size\" : \"201-500\",\n" +
            "                        \"tag\" : null,\n" +
            "                        \"url\" : \"http://www.xing.com\",\n" +
            "                        \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                        \"begin_date\" : \"2010-01\",\n" +
            "                        \"description\" : null,\n" +
            "                        \"end_date\" : null,\n" +
            "                        \"industry\" : \"AEROSPACE\",\n" +
            "                        \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                        \"until_now\" : true\n" +
            "                     },\n" +
            "                  \"companies\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"1_abcdef\",\n" +
            "                           \"name\" : \"XING AG\",\n" +
            "                           \"title\" : \"Softwareentwickler\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : null,\n" +
            "                           \"url\" : \"http://www.xing.com\",\n" +
            "                           \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                           \"begin_date\" : \"2010-01\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : null,\n" +
            "                           \"industry\" : \"AEROSPACE\",\n" +
            "                           \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : true\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"24_abcdef\",\n" +
            "                           \"name\" : \"Ninja Ltd.\",\n" +
            "                           \"title\" : \"DevOps\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"NINJA\",\n" +
            "                           \"url\" : \"http://www.ninja-ltd.co.uk\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2009-04\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2010-07\",\n" +
            "                           \"industry\" : \"ALTERNATIVE_MEDICINE\",\n" +
            "                           \"form_of_employment\" : \"OWNER\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"45_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"Wiss. Mitarbeiter\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"OFFIS\",\n" +
            "                           \"url\" : \"http://www.uni.de\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2007\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2008\",\n" +
            "                           \"industry\" : \"APPAREL_AND_FASHION\",\n" +
            "                           \"form_of_employment\" : \"PART_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"176_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"TEST NINJA\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : \"TESTCOMPANY\",\n" +
            "                           \"url\" : null,\n" +
            "                           \"career_level\" : \"ENTRY_LEVEL\",\n" +
            "                           \"begin_date\" : \"1998-12\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"1999-05\",\n" +
            "                           \"industry\" : \"ARTS_AND_CRAFTS\",\n" +
            "                           \"form_of_employment\" : \"INTERN\",\n" +
            "                           \"until_now\" : false\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"awards\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"name\" : \"Awesome Dude Of The Year\",\n" +
            "                           \"date_awarded\" : 2007,\n" +
            "                           \"url\" : null\n" +
            "                        }\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"educational_background\" :\n" +
            "               {\n" +
            "                  \"degree\" : \"MSc CE/CS\",\n" +
            "                  \"primary_school\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"42_abcdef\",\n" +
            "                        \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                        \"degree\" : \"MSc CE/CS\",\n" +
            "                        \"notes\" : null,\n" +
            "                        \"subject\" : null,\n" +
            "                        \"begin_date\" : \"1998-08\",\n" +
            "                        \"end_date\" : \"2005-02\"\n" +
            "                     },\n" +
            "                  \"schools\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"42_abcdef\",\n" +
            "                           \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                           \"degree\" : \"MSc CE/CS\",\n" +
            "                           \"notes\" : null,\n" +
            "                           \"subject\" : null,\n" +
            "                           \"begin_date\" : \"1998-08\",\n" +
            "                           \"end_date\" : \"2005-02\"\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"qualifications\" :\n" +
            "                     [\n" +
            "                        \"TOEFLS\",\n" +
            "                        \"PADI AOWD\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"photo_urls\" :\n" +
            "               {\n" +
            "                  \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "                  \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "                  \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "                  \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "                  \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "                  \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "                  \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "                  \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "                  \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "                  \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "                  \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "                  \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "                  \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "                  \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "               }\n" +
            "         },\n" +
            "         {\n" +
            "            \"id\" : \"123456_abcdef\",\n" +
            "            \"first_name\" : \"Max\",\n" +
            "            \"last_name\" : \"Mustermann\",\n" +
            "            \"display_name\" : \"Max Mustermann\",\n" +
            "            \"page_name\" : \"Max_Mustermann\",\n" +
            "            \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "            \"employment_status\" : \"EMPLOYEE\",\n" +
            "            \"gender\" : \"m\",\n" +
            "            \"birth_date\" :\n" +
            "               {\n" +
            "                  \"day\" : 12,\n" +
            "                  \"month\" : 8,\n" +
            "                  \"year\" : 1963\n" +
            "               },\n" +
            "            \"active_email\" : \"max.mustermann@xing.com\",\n" +
            "            \"time_zone\" :\n" +
            "               {\n" +
            "                  \"name\" : \"Europe/Copenhagen\",\n" +
            "                  \"utc_offset\" : 2.0\n" +
            "               },\n" +
            "            \"premium_services\" :\n" +
            "               [\n" +
            "                  \"SEARCH\",\n" +
            "                  \"PRIVATEMESSAGES\"\n" +
            "               ],\n" +
            "            \"badges\" :\n" +
            "               [\n" +
            "                  \"PREMIUM\",\n" +
            "                  \"MODERATOR\"\n" +
            "               ],\n" +
            "            \"wants\" : \"einen neuen Job\",\n" +
            "            \"haves\" : \"viele tolle Skills\",\n" +
            "            \"interests\" : \"Flitzebogen schießen and so on\",\n" +
            "            \"organisation_member\" : \"ACM, GI\",\n" +
            "            \"languages\" :\n" +
            "               {\n" +
            "                  \"de\" : \"NATIVE\",\n" +
            "                  \"en\" : \"FLUENT\",\n" +
            "                  \"fr\" : null,\n" +
            "                  \"zh\" : \"BASIC\"\n" +
            "               },\n" +
            "            \"private_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Privatstraße 1\",\n" +
            "                  \"phone\" : \"49|40|1234560\",\n" +
            "                  \"fax\" : \"||\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max@mustermann.de\",\n" +
            "                  \"mobile_phone\" : \"49|0155|1234567\"\n" +
            "               },\n" +
            "            \"business_address\" :\n" +
            "               {\n" +
            "                  \"city\" : \"Hamburg\",\n" +
            "                  \"country\" : \"DE\",\n" +
            "                  \"zip_code\" : \"20357\",\n" +
            "                  \"street\" : \"Geschäftsstraße 1a\",\n" +
            "                  \"phone\" : \"49|40|1234569\",\n" +
            "                  \"fax\" : \"49|40|1234561\",\n" +
            "                  \"province\" : \"Hamburg\",\n" +
            "                  \"email\" : \"max.mustermann@xing.com\",\n" +
            "                  \"mobile_phone\" : \"49|160|66666661\"\n" +
            "               },\n" +
            "            \"web_profiles\" :\n" +
            "               {\n" +
            "                  \"qype\" :\n" +
            "                     [\n" +
            "                        \"http://qype.de/users/foo\"\n" +
            "                     ],\n" +
            "                  \"google+\" :\n" +
            "                     [\n" +
            "                        \"http://plus.google.com/foo\"\n" +
            "                     ],\n" +
            "                  \"other\" :\n" +
            "                     [\n" +
            "                        \"http://blog.example.org\"\n" +
            "                     ],\n" +
            "                  \"homepage\" :\n" +
            "                     [\n" +
            "                        \"http://example.org\",\n" +
            "                        \"http://other-example.org\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"instant_messaging_accounts\" :\n" +
            "               {\n" +
            "                  \"skype\" : \"1122334455\",\n" +
            "                  \"googletalk\" : \"max.mustermann\"\n" +
            "               },\n" +
            "            \"professional_experience\" :\n" +
            "               {\n" +
            "                  \"primary_company\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"1_abcdef\",\n" +
            "                        \"name\" : \"XING AG\",\n" +
            "                        \"title\" : \"Softwareentwickler\",\n" +
            "                        \"company_size\" : \"201-500\",\n" +
            "                        \"tag\" : null,\n" +
            "                        \"url\" : \"http://www.xing.com\",\n" +
            "                        \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                        \"begin_date\" : \"2010-01\",\n" +
            "                        \"description\" : null,\n" +
            "                        \"end_date\" : null,\n" +
            "                        \"industry\" : \"AEROSPACE\",\n" +
            "                        \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                        \"until_now\" : true\n" +
            "                     },\n" +
            "                  \"companies\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"1_abcdef\",\n" +
            "                           \"name\" : \"XING AG\",\n" +
            "                           \"title\" : \"Softwareentwickler\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : null,\n" +
            "                           \"url\" : \"http://www.xing.com\",\n" +
            "                           \"career_level\" : \"PROFESSIONAL_EXPERIENCED\",\n" +
            "                           \"begin_date\" : \"2010-01\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : null,\n" +
            "                           \"industry\" : \"AEROSPACE\",\n" +
            "                           \"form_of_employment\" : \"FULL_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : true\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"24_abcdef\",\n" +
            "                           \"name\" : \"Ninja Ltd.\",\n" +
            "                           \"title\" : \"DevOps\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"NINJA\",\n" +
            "                           \"url\" : \"http://www.ninja-ltd.co.uk\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2009-04\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2010-07\",\n" +
            "                           \"industry\" : \"ALTERNATIVE_MEDICINE\",\n" +
            "                           \"form_of_employment\" : \"OWNER\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"45_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"Wiss. Mitarbeiter\",\n" +
            "                           \"company_size\" : null,\n" +
            "                           \"tag\" : \"OFFIS\",\n" +
            "                           \"url\" : \"http://www.uni.de\",\n" +
            "                           \"career_level\" : null,\n" +
            "                           \"begin_date\" : \"2007\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"2008\",\n" +
            "                           \"industry\" : \"APPAREL_AND_FASHION\",\n" +
            "                           \"form_of_employment\" : \"PART_TIME_EMPLOYEE\",\n" +
            "                           \"until_now\" : false\n" +
            "                        },\n" +
            "                        {\n" +
            "                           \"id\" : \"176_abcdef\",\n" +
            "                           \"name\" : null,\n" +
            "                           \"title\" : \"TEST NINJA\",\n" +
            "                           \"company_size\" : \"201-500\",\n" +
            "                           \"tag\" : \"TESTCOMPANY\",\n" +
            "                           \"url\" : null,\n" +
            "                           \"career_level\" : \"ENTRY_LEVEL\",\n" +
            "                           \"begin_date\" : \"1998-12\",\n" +
            "                           \"description\" : null,\n" +
            "                           \"end_date\" : \"1999-05\",\n" +
            "                           \"industry\" : \"ARTS_AND_CRAFTS\",\n" +
            "                           \"form_of_employment\" : \"INTERN\",\n" +
            "                           \"until_now\" : false\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"awards\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"name\" : \"Awesome Dude Of The Year\",\n" +
            "                           \"date_awarded\" : 2007,\n" +
            "                           \"url\" : null\n" +
            "                        }\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"educational_background\" :\n" +
            "               {\n" +
            "                  \"degree\" : \"MSc CE/CS\",\n" +
            "                  \"primary_school\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"42_abcdef\",\n" +
            "                        \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                        \"degree\" : \"MSc CE/CS\",\n" +
            "                        \"notes\" : null,\n" +
            "                        \"subject\" : null,\n" +
            "                        \"begin_date\" : \"1998-08\",\n" +
            "                        \"end_date\" : \"2005-02\"\n" +
            "                     },\n" +
            "                  \"schools\" :\n" +
            "                     [\n" +
            "                        {\n" +
            "                           \"id\" : \"42_abcdef\",\n" +
            "                           \"name\" : \"Carl-von-Ossietzky Universtät Schellenburg\",\n" +
            "                           \"degree\" : \"MSc CE/CS\",\n" +
            "                           \"notes\" : null,\n" +
            "                           \"subject\" : null,\n" +
            "                           \"begin_date\" : \"1998-08\",\n" +
            "                           \"end_date\" : \"2005-02\"\n" +
            "                        }\n" +
            "                     ],\n" +
            "                  \"qualifications\" :\n" +
            "                     [\n" +
            "                        \"TOEFLS\",\n" +
            "                        \"PADI AOWD\"\n" +
            "                     ]\n" +
            "               },\n" +
            "            \"photo_urls\" :\n" +
            "               {\n" +
            "                  \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "                  \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "                  \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "                  \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "                  \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "                  \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "                  \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "                  \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "                  \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "                  \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "                  \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "                  \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "                  \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "                  \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "               }\n" +
            "         }\n" +
            "      ]\n" +
            "}";

    static final String LIST_OF_USERS_FROM_EMAIL_SEARCH_EMAIL = "{\n" +
            "   \"results\" :\n" +
            "      {\n" +
            "         \"items\" :\n" +
            "            [\n" +
            "               {\n" +
            "                  \"email\" : \"existing_user@xing.com\",\n" +
            "                  \"hash\" : null,\n" +
            "                  \"user\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"10368_ddec16\"\n" +
            "                     }\n" +
            "               },\n" +
            "               null,\n" +
            "               {\n" +
            "                  \"email\" : \"unknown_user@xing.com\",\n" +
            "                  \"hash\" : null,\n" +
            "                  \"user\" : null\n" +
            "               }\n" +
            "            ],\n" +
            "         \"total\" : 2\n" +
            "      }\n" +
            "}";

    static final String LIST_OF_USERS_FROM_EMAIL_SEARCH_HASH = "{\n" +
            "   \"results\" :\n" +
            "      {\n" +
            "         \"items\" :\n" +
            "            [\n" +
            "               {\n" +
            "                  \"email\" : null,\n" +
            "                  \"hash\" : \"1a83d7e553085e340f0f08887182d49a\",\n" +
            "                  \"user\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"10368_ddec16\"\n" +
            "                     }\n" +
            "               },\n" +
            "               {\n" +
            "                  \"email\" : null,\n" +
            "                  \"hash\" : \"e2854c86a4d2064e77f0ae3567cad297\",\n" +
            "                  \"user\" : null\n" +
            "               }\n" +
            "            ],\n" +
            "         \"total\" : 2\n" +
            "      }\n" +
            "}";

    static final String LIST_OF_USERS_FROM_FIND = "{\n" +
            "   \"users\" :\n" +
            "      {\n" +
            "         \"items\" :\n" +
            "            [\n" +
            "               {\n" +
            "                  \"user\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"123456_abcdef\",\n" +
            "                        \"display_name\" : \"Max Mustermann\",\n" +
            "                        \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "                        \"photo_urls\" :\n" +
            "                           {\n" +
            "                              \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "                              \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "                              \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "                              \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "                              \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "                              \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "                              \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "                              \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "                              \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "                              \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "                              \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "                              \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "                              \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "                              \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "                           }\n" +
            "                     }\n" +
            "               },\n" +
            "               {\n" +
            "                  \"user\" :\n" +
            "                     {\n" +
            "                        \"id\" : \"123sd456_ab793ef\",\n" +
            "                        \"display_name\" : \"Max Freeman\",\n" +
            "                        \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "                        \"photo_urls\" :\n" +
            "                           {\n" +
            "                              \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "                              \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "                              \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "                              \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "                              \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "                              \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "                              \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "                              \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "                              \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "                              \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "                              \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "                              \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "                              \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "                              \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "                           }\n" +
            "                     }\n" +
            "               }\n" +
            "            ],\n" +
            "         \"total\" : 2\n" +
            "      }\n" +
            "}";

    static final String USER_FROM_ID_CARD = "{\n" +
            "   \"id_card\" :\n" +
            "      {\n" +
            "         \"id\" : \"123456_abcdef\",\n" +
            "         \"display_name\" : \"Max Mustermann\",\n" +
            "         \"permalink\" : \"https://www.xing.com/profile/Max_Mustermann\",\n" +
            "         \"photo_urls\" :\n" +
            "            {\n" +
            "               \"large\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.140x185.jpg\",\n" +
            "               \"maxi_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.70x93.jpg\",\n" +
            "               \"medium_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.57x75.jpg\",\n" +
            "               \"mini_thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.18x24.jpg\",\n" +
            "               \"thumb\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.30x40.jpg\",\n" +
            "               \"size_32x32\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.32x32.jpg\",\n" +
            "               \"size_48x48\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.48x48.jpg\",\n" +
            "               \"size_64x64\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.64x64.jpg\",\n" +
            "               \"size_96x96\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.96x96.jpg\",\n" +
            "               \"size_128x128\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.128x128.jpg\",\n" +
            "               \"size_192x192\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.192x192.jpg\",\n" +
            "               \"size_256x256\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.256x256.jpg\",\n" +
            "               \"size_1024x1024\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.1024x1024.jpg\",\n" +
            "               \"size_original\" : \"http://www.xing.com/img/users/e/3/d/f94ef165a.123456,1.original.jpg\"\n" +
            "            }\n" +
            "      }\n" +
            "}";
}
