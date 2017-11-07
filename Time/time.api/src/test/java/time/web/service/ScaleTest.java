package time.api.service;

import org.junit.Test;
import time.domain.Scale;
import time.tool.date.Dates;
import time.api.bean.TermPeriodFilter;

import static org.assertj.core.api.Assertions.assertThat;

public class ScaleTest {

    @Test
    public void test1M3M() {
        assertScale("@1M @2M", "0");
    }

    @Test
    public void test1m3m() {
        assertScale("@1m @2m", "0");
    }

    @Test
    public void testDatePrecise() {
        assertScale("@1984 @1985", "3");
    }

    @Test
    public void testDatePrecise2() {
        assertScale("@1984 @1994", "3");
    }

    @Test
    public void testDatePrecise3() {
        assertScale("@10/02/1984 @10/02/2014", "2");
    }

    @Test
    public void testDatePrecise4() {
        assertScale("@-1000 @1000", "1");
    }

    @Test
    public void test1(){
        assertScale("@0 @2m", "0");
    }







    private void assertScale(String term, String expectedScale) {
        final TermPeriodFilter termPeriodFilter = TermPeriodFilter.fromString(term);
        final long from = termPeriodFilter.getFrom();
        final long to = termPeriodFilter.getTo();
        final long totalDays = to - from;
        assertScale(totalDays, expectedScale);
    }

    private void assertScaleYear(long years, String expectedScale){
        assertScale(Dates.yearsToDays(years), expectedScale);
    }

    private void assertScale(long totalDays, String expectedScale) {
        final String actualScale = Scale.get(totalDays);
        assertThat(actualScale).isEqualTo(expectedScale);
    }

}
