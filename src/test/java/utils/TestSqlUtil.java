package utils;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.exceptions.UtilException;
import com.servlet.ejournal.model.dao.impl.UserDAO;
import com.servlet.ejournal.model.entities.Course;
import com.servlet.ejournal.model.entities.Topic;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.servlet.ejournal.utils.SqlUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.SQLQueries.*;

public class TestSqlUtil {
    private static final HttpServletRequest reqMock = mock(HttpServletRequest.class);

    static class TestGetPages {
        @Test
        void testGetPagesLimitZero() {
            assertEquals(1, getPages(0, 10).length);
            assertEquals(1, getPages(0, 10)[0]);
        }

        @Test
        void testGetPagesRecordsCountZero() {
            assertEquals(1, getPages(10, 0).length);
            assertEquals(1, getPages(0, 10)[0]);
        }

        @Test
        void testGetPagesEvenNumber() {
            assertEquals(2, getPages(5, 7).length);
        }

        @Test
        void testGetPagesOddNumber() {
            assertEquals(3, getPages(5, 15).length);
        }
    }

    static class TestGetLimit {
        @Test
        void testGetDefaultLimit() {
            when(reqMock.getParameter(DISPLAY_RECORDS_NUMBER)).thenReturn(null);
            assertEquals(DEFAULT_LIMIT, getLimit(reqMock));
        }

        @Test
        void testGetActualLimit() {
            when(reqMock.getParameter(DISPLAY_RECORDS_NUMBER)).thenReturn("6");
            assertEquals(6, getLimit(reqMock));
        }

        @Test
        void testGetExceptionParsingLimit() {
            when(reqMock.getParameter(DISPLAY_RECORDS_NUMBER)).thenReturn("string");
            assertThrows(NumberFormatException.class, () -> getLimit(reqMock));
        }
    }

    static class TestGetCurrentPage {
        @Test
        void testGetDefaultPage() {
            when(reqMock.getParameter(CURRENT_PAGE)).thenReturn(null);
            assertEquals(DEFAULT_PAGE, getCurrentPage(reqMock));
        }

        @Test
        void testGetActualPage() {
            when(reqMock.getParameter(CURRENT_PAGE)).thenReturn("5");
            assertEquals(5, getCurrentPage(reqMock));
        }

        @Test
        void testGetExceptionParsingPage() {
            when(reqMock.getParameter(CURRENT_PAGE)).thenReturn("not a number");
            assertThrows(NumberFormatException.class, () -> getCurrentPage(reqMock));
        }
    }

    static class TestGetOffset {
        @Test
        void testGetDefaultOffset() {
            assertEquals(DEFAULT_OFFSET, getOffset(5, 0));
        }

        @Test
        void testGetActualOffset() {
            assertEquals(10, getOffset(5, 3));
        }
    }

    static class TestGetSortingType {
        @Test
        void testNoDefaultSorting() {
            when(reqMock.getParameter(SORTING_TYPE)).thenReturn(null);
            assertThrows(UtilException.class, () -> getSortingType(reqMock, UserDAO.class));
        }

        @Test
        void testGetDefaultSorting() throws UtilException {
            when(reqMock.getParameter(SORTING_TYPE)).thenReturn(null);
            assertEquals(TOPIC_ID, getSortingType(reqMock, Topic.class));
        }

        @Test
        void testGetActualSorting() throws UtilException {
            when(reqMock.getParameter(SORTING_TYPE)).thenReturn("sorting");
            assertEquals("sorting", getSortingType(reqMock, Course.class));
        }
    }

    static class TestGetFilter {
        @Test
        void testGetNoneFilterNullParameter() {
            when(reqMock.getParameter(SWITCH)).thenReturn(null);
            assertEquals(NONE_ATTR, getFilter(reqMock, SWITCH));
        }

        @Test
        void testGetActualFilter() {
            when(reqMock.getParameter(SWITCH)).thenReturn("none");
            assertEquals(NONE_ATTR, getFilter(reqMock, SWITCH));
            when(reqMock.getParameter(COURSE_ID)).thenReturn(COURSE_ID);
            assertEquals(COURSE_ID, getFilter(reqMock, COURSE_ID));
        }
    }

    static class TestGetFilters {
        private Map<String, String[]> filters;

        @BeforeEach
        void setup() {
            when(reqMock.getParameter(COURSE_ID)).thenReturn("1");
            when(reqMock.getParameter(TOPIC_ID)).thenReturn("2");
        }

        @Test
        void testGetAllFilters() {
            filters = getFilters(reqMock, COURSE_ID, TOPIC_ID);
            assertFalse(filters.isEmpty());
            assertEquals(1, filters.get(COURSE_ID).length);
            assertEquals(1, filters.get(TOPIC_ID).length);
            assertEquals("1", filters.get(COURSE_ID)[0]);
            assertEquals("2", filters.get(TOPIC_ID)[0]);
        }

        @Test
        void testGetNotNullFilter() {
            when(reqMock.getParameter(TOPIC_ID)).thenReturn(null);
            filters = getFilters(reqMock, COURSE_ID, TOPIC_ID);
            assertFalse(filters.isEmpty());
            assertNull(filters.get(TOPIC_ID));
            assertEquals(1, filters.get(COURSE_ID).length);
            assertEquals("1", filters.get(COURSE_ID)[0]);
        }

        @Test
        void testGetNotNoneAttrFilter() {
            when(reqMock.getParameter(TOPIC_ID)).thenReturn(NONE_ATTR);
            filters = getFilters(reqMock, COURSE_ID, TOPIC_ID);
            assertFalse(filters.isEmpty());
            assertNull(filters.get(TOPIC_ID));
            assertEquals(1, filters.get(COURSE_ID).length);
            assertEquals("1", filters.get(COURSE_ID)[0]);
        }
    }

    static class TestGetEndDateFilter {
        private Map<String, String[]> filters;

        @BeforeEach
        void setup() {
            filters = new HashMap<>();
        }

        @Test
        void testCourseNotStartedCase() {
            getEndDateFilter(COURSE_NOT_STARTED, filters);
            assertFalse(filters.isEmpty());
            assertEquals(1, filters.get(QUERY).length);
            assertTrue(filters.get(QUERY)[0].contains(String.format("%s > ", START_DATE_MILLIS)));
        }

        @Test
        void testCourseInProgressCase() {
            getEndDateFilter(COURSE_IN_PROGRESS, filters);
            assertFalse(filters.isEmpty());
            assertEquals(1, filters.get(QUERY).length);
            assertTrue(filters.get(QUERY)[0].contains(String.format("%s > ", END_DATE_MILLIS)));
            assertTrue(filters.get(QUERY)[0].contains(String.format("%s < ", START_DATE_MILLIS)));
            assertTrue(filters.get(QUERY)[0].contains("and"));
        }

        @Test
        void testCourseEndedCase() {
            getEndDateFilter(COURSE_ENDED, filters);
            assertFalse(filters.isEmpty());
            assertEquals(1, filters.get(QUERY).length);
            assertTrue(filters.get(QUERY)[0].contains(String.format("%s < ", END_DATE_MILLIS)));
        }

        @Test
        void testInvalidEndDateFilter() {
            getEndDateFilter("invalid", filters);
            assertTrue(filters.isEmpty());
        }
    }

    static class TestGetMyCourseFilter {
        Map<String, String[]> filters;
        String userFilterString = String.format(AttributeConstants.FULL_COLUMN_NAME, AttributeConstants.USER_COURSE_TABLE, AttributeConstants.USER_ID);


        @BeforeEach
        void setup() {
            filters = new HashMap<>();
        }

        @Test
        void testGetActualMyCourseFilter() {
            when(reqMock.getParameter(SWITCH)).thenReturn("on");
            getMyCourseFilter(reqMock, 10, filters);
            assertFalse(filters.isEmpty());
            assertEquals(1, filters.get(userFilterString).length);
            assertEquals("10", filters.get(userFilterString)[0]);
        }

        @Test
        void testGetAllCoursesFilterInvalidParameter() {
            when(reqMock.getParameter(SWITCH)).thenReturn(NONE_ATTR);
            getMyCourseFilter(reqMock, 10, filters);
            assertFalse(filters.isEmpty());
            assertNull(filters.get(userFilterString));
            assertNotNull(filters.get(FINAL_MARK));
        }

        @Test
        void testGetAllCoursesFilterNullParameter() {
            when(reqMock.getParameter(SWITCH)).thenReturn(null);
            getMyCourseFilter(reqMock, 10, filters);
            assertFalse(filters.isEmpty());
            assertNull(filters.get(userFilterString));
            assertNotNull(filters.get(FINAL_MARK));
        }
    }

    static class TestGetEntityPaginationQuery {
        Map<String, String[]> filters;
        String selectPart = String.format("%s %s ", SELECT_EVERYTHING_FROM_PART, TOPIC_TABLE);

        @BeforeEach
        void setup() {
            filters = new HashMap<>();
        }

        @Test
        void testGetQueryNoFilters() {
            assertEquals(selectPart + PAGINATION_LIMIT_OFFSET_QUERY_PART, getEntityPaginationQuery(TOPIC_TABLE, filters));
        }

        @Test
        void testGetQueryOneFilter() {
            filters.put(TOPIC_ID, new String[]{"10"});
            assertEquals(selectPart + String.format("where %s = 10 ", TOPIC_ID) + PAGINATION_LIMIT_OFFSET_QUERY_PART,
                    getEntityPaginationQuery(TOPIC_TABLE, filters));
        }

        @Test
        void testGetQueryOneFilterString() {
            filters.put(TOPIC_ID, new String[]{"not a number"});
            assertEquals(selectPart + String.format("where %s = 'not a number' ", TOPIC_ID) + PAGINATION_LIMIT_OFFSET_QUERY_PART,
                    getEntityPaginationQuery(TOPIC_TABLE, filters));
        }

        @Test
        void testGetQueryTwoFilters() {
            filters.put(TOPIC_ID, new String[]{"4"});
            filters.put(COURSE_ID, new String[]{"5"});
            assertEquals(selectPart + String.format("where %s = 4 and %s = 5 ", TOPIC_ID, COURSE_ID) + PAGINATION_LIMIT_OFFSET_QUERY_PART,
                    getEntityPaginationQuery(TOPIC_TABLE, filters));
        }

        @Test
        void testGetQueryWithSubquery() {
            filters.put(QUERY, new String[]{START_DATE_MILLIS + " = 1"});
            assertEquals(selectPart + String.format("where %s = 1 ", START_DATE_MILLIS) + PAGINATION_LIMIT_OFFSET_QUERY_PART,
                    getEntityPaginationQuery(TOPIC_TABLE, filters));
        }

        @Test
        void testGetQueryWithFilterAndSubquery() {
            filters.put(COURSE_ID, new String[]{"-1"});
            filters.put(QUERY, new String[]{END_DATE_MILLIS + " = 1"});
            assertEquals(selectPart + String.format("where %s = 1 and %s = '-1' ", END_DATE_MILLIS, COURSE_ID) + PAGINATION_LIMIT_OFFSET_QUERY_PART,
                    getEntityPaginationQuery(TOPIC_TABLE, filters));

        }
    }
}
