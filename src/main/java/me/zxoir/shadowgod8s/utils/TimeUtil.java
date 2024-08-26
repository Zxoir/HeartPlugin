package me.zxoir.shadowgod8s.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Provides utility methods for time conversion and formatting.
 */
public class TimeUtil {
    private static final Map<Long, String> formattedTimeCache = new HashMap<>();
    private static final Map<Long, String> compactFormattedTimeCache = new HashMap<>();
    private static final StringBuilder formattedTime = new StringBuilder();

    /**
     * Formats a time duration in milliseconds into a human-readable string.
     *
     * @param time     The time duration in milliseconds.
     * @param compact  Whether to format the time string without spaces.
     * @param withSecs Whether to include seconds in the formatted string.
     * @return The formatted time string.
     */
    public static String formatTime(long time, boolean compact, boolean withSecs) {
        Map<Long, String> cache = compact ? compactFormattedTimeCache : formattedTimeCache;

        if (cache.containsKey(time))
            return cache.get(time);

        formattedTime.setLength(0);

        long seconds = time / 1000;
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds % 86400);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds % 3600);
        seconds %= 60;

        if (days > 0) {
            formattedTime.append(days).append(compact ? "d " : (days != 1 ? " days" : " day"));
            if (!compact && (hours > 0 || minutes > 0 || (withSecs && seconds > 0)) && !formattedTime.toString().endsWith(" ")) {
                formattedTime.append(" ");
            }
        }
        if (hours > 0) {
            formattedTime.append(hours).append(compact ? "h " : (hours != 1 ? " hours" : " hour"));
            if (!compact && (minutes > 0 || (withSecs && seconds > 0)) && !formattedTime.toString().endsWith(" ")) {
                formattedTime.append(" ");
            }
        }
        if (minutes > 0) {
            if ((days > 0 || hours > 0) && !formattedTime.isEmpty() && !compact) {
                formattedTime.append("and ");
            }
            formattedTime.append(minutes).append(compact ? "m " : (minutes != 1 ? " minutes" : " minute"));
            if (!compact && (withSecs && seconds > 0) && !formattedTime.toString().endsWith(" ")) {
                formattedTime.append(" ");
            }
        }
        if (withSecs && seconds > 0 && !(days > 0 && hours > 0) && (((days > 0 || hours > 0) && minutes <= 0) || (days <= 0 && hours <= 0))) {
            if (days > 0 || hours > 0 || minutes > 0) {
                formattedTime.append(compact ? "" : "and ");
            }
            formattedTime.append(seconds).append(compact ? "s" : (seconds != 1 ? " seconds" : " second"));
        }
        if (days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0)
            formattedTime.append(0).append(" seconds");

        String result = formattedTime.toString();
        cache.put(time, result);
        return result;
    }
}