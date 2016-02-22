package com.xing.api.data;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link SafeCalendar}.
 *
 * @author daniel.hartwich
 */
public class SafeCalendarTest {
    private SafeCalendar calendar;

    @Before
    public void setUp() throws Exception {
        calendar = new SafeCalendar();
    }

    @SuppressWarnings("MagicNumber")
    @Test
    public void testSet() throws Exception {
        calendar.set(Calendar.DAY_OF_YEAR, 53);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(Calendar.FEBRUARY);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(22);
        assertThat(calendar.get(Calendar.HOUR)).isEqualTo(0);
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(1970);
    }
}
