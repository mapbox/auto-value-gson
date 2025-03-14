// modified by mapbox
/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mapbox.auto.value.gson.example;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.TestCase;

import java.lang.reflect.Field;

import static com.google.gson.FieldNamingPolicy.IDENTITY;
import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DASHES;
import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DOTS;
import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static com.google.gson.FieldNamingPolicy.UPPER_CAMEL_CASE;
import static com.google.gson.FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES;

public final class FieldNamingTest extends TestCase {
    public void testIdentity() {
        Gson gson = getGsonWithNamingPolicy(IDENTITY);
        assertEquals("{'lowerCamel':1,'UpperCamel':2,'_lowerCamelLeadingUnderscore':3," +
                        "'_UpperCamelLeadingUnderscore':4,'lower_words':5,'UPPER_WORDS':6," +
                        "'annotatedName':7,'lowerId':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testUpperCamelCase() {
        Gson gson = getGsonWithNamingPolicy(UPPER_CAMEL_CASE);
        assertEquals("{'LowerCamel':1,'UpperCamel':2,'_LowerCamelLeadingUnderscore':3," +
                        "'_UpperCamelLeadingUnderscore':4,'Lower_words':5,'UPPER_WORDS':6," +
                        "'annotatedName':7,'LowerId':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testUpperCamelCaseWithSpaces() {
        Gson gson = getGsonWithNamingPolicy(UPPER_CAMEL_CASE_WITH_SPACES);
        assertEquals("{'Lower Camel':1,'Upper Camel':2,'_Lower Camel Leading Underscore':3," +
                        "'_ Upper Camel Leading Underscore':4,'Lower_words':5,'U P P E R_ W O R D S':6," +
                        "'annotatedName':7,'Lower Id':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testLowerCaseWithUnderscores() {
        Gson gson = getGsonWithNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);
        assertEquals("{'lower_camel':1,'upper_camel':2,'_lower_camel_leading_underscore':3," +
                        "'__upper_camel_leading_underscore':4,'lower_words':5,'u_p_p_e_r__w_o_r_d_s':6," +
                        "'annotatedName':7,'lower_id':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testLowerCaseWithDashes() {
        Gson gson = getGsonWithNamingPolicy(LOWER_CASE_WITH_DASHES);
        assertEquals("{'lower-camel':1,'upper-camel':2,'_lower-camel-leading-underscore':3," +
                        "'_-upper-camel-leading-underscore':4,'lower_words':5,'u-p-p-e-r_-w-o-r-d-s':6," +
                        "'annotatedName':7,'lower-id':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testLowerCaseWithDots() {
        Gson gson = getGsonWithNamingPolicy(LOWER_CASE_WITH_DOTS);
        assertEquals("{'lower.camel':1,'upper.camel':2,'_lower.camel.leading.underscore':3," +
                        "'_.upper.camel.leading.underscore':4,'lower_words':5,'u.p.p.e.r_.w.o.r.d.s':6," +
                        "'annotatedName':7,'lower.id':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    public void testCustomFieldNamingStrategy() {
        Gson gson = new GsonBuilder()
                .setFieldNamingStrategy(new xPrefixStrategy())
                .registerTypeAdapterFactory(SampleAdapterFactory.create())
                .create();
        assertEquals("{'xlowerCamel':1,'xUpperCamel':2,'x_lowerCamelLeadingUnderscore':3," +
                        "'x_UpperCamelLeadingUnderscore':4,'xlower_words':5,'xUPPER_WORDS':6," +
                        "'annotatedName':7,'xlowerId':8}",
                gson.toJson(getTestNames()).replace('\"', '\''));
    }

    private Gson getGsonWithNamingPolicy(FieldNamingPolicy fieldNamingPolicy){
        return new GsonBuilder()
                .setFieldNamingPolicy(fieldNamingPolicy)
                .registerTypeAdapterFactory(SampleAdapterFactory.create())
                .create();
    }

    private TestNames getTestNames() {
        return TestNames.builder()
                .lowerCamel(1)
                .UpperCamel(2)
                ._lowerCamelLeadingUnderscore(3)
                ._UpperCamelLeadingUnderscore(4)
                .lower_words(5)
                .UPPER_WORDS(6)
                .annotated(7)
                .lowerId(8)
                .build();
    }

    private class xPrefixStrategy implements FieldNamingStrategy {
        @Override
        public String translateName(Field f) {
            return "x" + f.getName();
        }
    }
}
