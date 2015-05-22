package com.ryan.unofficialhendrixapp;

import com.ryan.unofficialhendrixapp.helpers.Utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.text.ParseException;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest="/app/src/main/AndroidManifest.xml")
public class test {

    @Test
    public void testDateConversion() throws ParseException {
        long exampleTime = 1432174254000l;
        String exampleDate = "Thu, 21 May 2015 02:10:54 GMT";
        assertTrue(exampleTime == Utility.convertToDate(exampleDate).getTime());
    }

}
