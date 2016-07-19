package time.web.service;

import org.junit.Test;
import time.web.bean.TermPeriodFilter;

import static org.assertj.core.api.Assertions.assertThat;

public class BucketServiceTest {

    private BucketService bucketService = new BucketService();

    @Test
    public void test1M3M() {
        final String term = "@1M @2M";
        final String expectedScale = "0";
        assertScale(term, expectedScale);
    }

    @Test
    public void test1m3m() {
        final String term = "@1m @2m";
        final String expectedScale = "0";
        assertScale(term, expectedScale);
    }

    @Test
    public void testDatePrecise() {
        final String term = "@10/02/1984 @10/02/1985";
        final String expectedScale = "3";
        assertScale(term, expectedScale);
    }

    @Test
    public void testDatePrecise2() {
        final String term = "@10/02/1984 @10/02/1994";
        final String expectedScale = "3";
        assertScale(term, expectedScale);
    }

    @Test
    public void testDatePrecise3() {
        final String term = "@10/02/1984 @10/02/2014";
        final String expectedScale = "2";
        assertScale(term, expectedScale);
    }

    @Test
    public void testDatePrecise4() {
        final String term = "@-1000 @1000";
        final String expectedScale = "1";
        assertScale(term, expectedScale);
    }

    private void assertScale(String term, String expectedScale) {
        final TermPeriodFilter termPeriodFilter = TermPeriodFilter.parse(term);
        final String actualScale = bucketService.determineScale(termPeriodFilter);
        assertThat(actualScale).isEqualTo(expectedScale);
    }

}
