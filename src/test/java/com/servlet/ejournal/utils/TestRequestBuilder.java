package com.servlet.ejournal.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.CommandNameConstants.*;
import static com.servlet.ejournal.utils.RequestBuilder.*;

public class TestRequestBuilder {
    private static Map<String, String[]> params;

    @Nested
    class TestBuildRequest {
        @BeforeEach
        void setup() {
            params = new HashMap<>();
        }

        @Test
        void testBuildRequestNoParams() {
            assertEquals(CONTROLLER_ATTR, buildRequest(CONTROLLER_ATTR, params));
        }

        @Test
        void testBuildRequestOneParam() {
            params.put(COURSE_ID, new String[]{"14"});
            assertEquals(String.format("%s?%s=%s&", CONTROLLER_ATTR, COURSE_ID, "14"), buildRequest(CONTROLLER_ATTR, params));
        }

        @Test
        void testBuildRequestTwoParams() {
            params.put(COURSE_ID, new String[]{"15"});
            params.put(SWITCH, new String[]{"on"});
            assertEquals(String.format("%s?%s=%s&%s=%s&", CONTROLLER_ATTR, COURSE_ID, "15", SWITCH, "on"),
                    buildRequest(CONTROLLER_ATTR, params));
        }

        @Test
        void testBuildRequestSameParameterTwoValues() {
            params.put(COURSE_ID, new String[]{"14", "15"});
            assertEquals(String.format("%s?%s=%s&%s=%s&", CONTROLLER_ATTR, COURSE_ID, "14", COURSE_ID, "15"),
                    buildRequest(CONTROLLER_ATTR, params));
        }
    }

    @Nested
    class TestBuildCommand {
        @BeforeEach
        void setup() {
            params = new HashMap<>();
        }

        @Test
        void testBuildCommandOneParam() {
            params.put(COURSE_ID, new String[]{"1"});
            String result = String.format("%s?command=%s&%s=%s&", CONTROLLER_ATTR, UPDATE_COURSE, COURSE_ID, "1");
            assertEquals(result, buildCommand(CONTROLLER_ATTR, UPDATE_COURSE, params));
        }

        @Test
        void testBuildCommandTwoParams() {
            params.put(COURSE_ID, new String[]{"1"});
            params.put(USER_ID, new String[]{"2"});
            String result = String.format("%s?command=%s&%s=2&%s=1&", CONTROLLER_ATTR, UPDATE_USER_COMMAND, USER_ID, COURSE_ID);
            assertEquals(result, buildCommand(CONTROLLER_ATTR, UPDATE_USER_COMMAND, params));
        }

        @Test
        void testBuildCommandTwoParamsOneParamIsNull() {
            params.put(COURSE_ID, new String[]{"1"});
            params.put(USER_ID, null);
            String result = String.format("%s?command=%s&%s=1&", CONTROLLER_ATTR, CREATE_COURSE, COURSE_ID);
            assertEquals(result, buildCommand(CONTROLLER_ATTR, CREATE_COURSE, params));
        }

        @Test
        void testBuildCommandSameParamTwoValues() {
            params.put(COURSE_ID, new String[]{"1", "2"});
            String result = String.format("%s?command=%s&%s=1&%s=2&", CONTROLLER_ATTR, UPDATE_TOPIC, COURSE_ID, COURSE_ID);
            assertEquals(result, buildCommand(CONTROLLER_ATTR, UPDATE_TOPIC, params));
        }

        @Test
        void testBuildCommandNoParams() {
            assertEquals(String.format("%s?command=%s&", CONTROLLER_ATTR, DELETE_COURSE), buildCommand(CONTROLLER_ATTR, DELETE_COURSE, params));
        }
    }

    @Nested
    class TestGetSpecifiedParamsMap {
        private Map<String, String[]> params;

        @BeforeEach
        void setup() {
            params = new HashMap<>();
            params.put(COURSE_ID, new String[]{"Never"});
            params.put(QUERY, new String[]{"Gonna"});
            params.put(USER_ID, new String[]{"Give"});
            params.put(TOPIC_ID, new String[]{"You"});
            params.put(FINAL_MARK, new String[]{"Up"});
        }

        @Test
        void testGetSpecifiedParamsMapGeneralMapIsNull() {
            assertTrue(getSpecifiedParamsMap(null, "param").isEmpty());
        }

        @Test
        void testGetSpecifiedParamsMapGeneralMapIsEmpty() {
            assertEquals(1, getSpecifiedParamsMap(new HashMap<>(), "param").size());
        }

        @Test
        void testGetSpecifiedParamsMapOneParam() {
            assertEquals(5, params.size());
            assertEquals(1, getSpecifiedParamsMap(params, COURSE_ID).size());
            assertNotNull(getSpecifiedParamsMap(params, COURSE_ID).get(COURSE_ID));
        }

        @Test
        void testGetSpecifiedParamsMapTwoParams() {
            Map<String, String[]> result = getSpecifiedParamsMap(params, COURSE_ID, TOPIC_ID);
            assertEquals(5, params.size());
            assertEquals(2, result.size());
            assertNotNull(result.get(COURSE_ID));
        }

        @Test
        void testGetSpecifiedParamsMapGeneralMapDontHaveSuch() {
            Map<String, String[]> result = getSpecifiedParamsMap(params, USER_COURSE_TABLE, TEACHER_COURSE_TABLE);
            assertEquals(5, params.size());
            assertEquals(2, result.size());
            assertNull(result.get(USER_COURSE_TABLE));
            assertNull(result.get(TEACHER_COURSE_TABLE));
            assertNull(result.get(COURSE_ID));
        }
    }
}
