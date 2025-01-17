package org.g02.flightsalesfx;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple helper to make testing equals and hashCode Trivial.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TestUtil {

    /**
     * Verify equals and hash.
     *
     * @param <T>   type of objects
     * @param ref   reference object
     * @param equal equal to ref
     * @param uneq  var args list of unequal objects.
     */
    public static <T> void verifyEqualsHasCode(T ref, T equal, T... uneq) {

        T rawNull = null;
        Object x = "Something";
        assertThat(ref.equals(ref)).isTrue();
        assertThat(ref.equals(equal)).isTrue();
        assertThat(ref.equals(rawNull)).isFalse();
        assertThat(ref.equals(x)).isFalse();
        for (T t : uneq) {
            assertThat(ref.equals(t)).isFalse();
        }

        assertThat(ref.hashCode()).isEqualTo(equal.hashCode());
    }

    static double getDoubleConsideringLocale(String input) {
        try {
            return DecimalFormat.getNumberInstance(Locale.getDefault()).parse(input).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1.0;
        }
    }

    static int getIntConsideringLocale(String input) {
        try {
            return DecimalFormat.getNumberInstance(Locale.getDefault()).parse(input).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
