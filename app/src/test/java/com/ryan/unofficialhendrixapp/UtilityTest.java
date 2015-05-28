package com.ryan.unofficialhendrixapp;

import com.ryan.unofficialhendrixapp.helpers.DateUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, manifest="/app/src/main/AndroidManifest.xml")
public class UtilityTest {

    @Test
    public void testDateConversion() throws ParseException {
        long exampleTime = 1432174254000l;
        String exampleDate = "Thu, 21 May 2015 02:10:54 GMT";
        assertTrue(exampleTime == DateUtils.convertToDate(exampleDate).getTime());
        assertTrue(DateUtils.convertToDate(exampleTime).equals(DateUtils.convertToDate(exampleDate)));
    }

}
