package com.acelost.chrono;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.*;

public final class Chrono {

    private static final Object timersLock = new Object();
    private static final Object bunchesLock = new Object();
    private static final Map<String, Timer> timers = new HashMap<>();
    private static final Map<String, Set<Timer>> bunches = new HashMap<>();

    @NonNull
    public static Timer start(@NonNull final String subject) {
        final Timer timer = new Timer(subject);
        synchronized (timersLock) {
            timers.put(subject, timer);
        }
        timer.start();
        return timer;
    }

    public static void capture(@NonNull final String subject, @NonNull final String message) {
        final Timer timer;
        synchronized (timersLock) {
            timer = timers.get(subject);
        }
        if (timer != null) {
            timer.capture(message);
        } else {
            warn("Can't capture \'%s\' for \'%s\' because this timer is not started", message, subject);
        }
    }

    public static void stop(@NonNull final String subject) {
        final Timer timer;
        synchronized (timersLock) {
            timer = timers.get(subject);
        }
        if (timer != null) {
            timer.stop();
        } else {
            warn("Can't stop \'%s\' because this timer is not started", subject);
        }
    }

    public static void resume(@NonNull final String subject) {
        final Timer timer;
        synchronized (timersLock) {
            timer = timers.get(subject);
        }
        if (timer != null) {
            timer.resume();
        } else {
            warn("Can't resume \'%s\' because this timer is not started", subject);
        }
    }

    public static void toBunch(@NonNull final String bunch, @NonNull final String subject) {
        final Timer timer;
        synchronized (timersLock) {
            timer = timers.get(subject);
        }
        if (timer != null) {
            toBunch(bunch, timer);
        } else {
            warn("Can't add \'%s\' to bunch because timer is not found", subject);
        }
    }

    static void toBunch(@NonNull final String bunch, @NonNull final Timer timer) {
        synchronized (bunchesLock) {
            Set<Timer> timers = bunches.get(timer.getSubject());
            if (timers == null) {
                final Set<Timer> newBunch = new HashSet<>();
                bunches.put(bunch, newBunch);
                timers = newBunch;
            }
            timers.add(timer);
        }
    }

    public static long bunchTotal(@NonNull final String bunch) {
        final Set<Timer> timers;
        synchronized (bunchesLock) {
            timers = bunches.get(bunch);
        }
        if (timers != null) {
            long total = 0;
            int stopped = 0;
            for (Timer timer : timers) {
                total += timer.time();
                if (!timer.isStarted()) {
                    ++stopped;
                }
            }
            log("Total amount of bunch \'%s\' is %s (%d of %d stopped)", bunch, ms(total), stopped, timers.size());
            return total;
        }
        warn("Bunch \'%s\' is not found", bunch);
        return 0;
    }

    public static long bunchAverage(@NonNull final String bunch) {
        final Set<Timer> timers;
        synchronized (bunchesLock) {
            timers = bunches.get(bunch);
        }
        if (timers != null) {
            long total = 0;
            for (Timer timer : timers) {
                total += timer.time();
            }
            final long average = timers.size() > 0 ? total / timers.size() : 0;
            log("Average timer of bunch \'%s\' is %s", bunch, ms(average));
            return average;
        }
        warn("Bunch \'%s\' is not found", bunch);
        return 0;
    }

    public static void clearBunch(@NonNull final String bunch) {
        synchronized (bunchesLock) {
            bunches.remove(bunch);
        }
    }

    @NonNull
    static String ms(float ms) {
        return String.format(Locale.ENGLISH, "%.1f\u202Ams", ms);
    }

    static void warn(@NonNull final String message, @Nullable Object... args) {
        print(Log.WARN, message, args);
    }

    static void log(@NonNull final String message, @Nullable Object... args) {
        print(Log.DEBUG, message, args);
    }

    private static void print(final int priority, @NonNull final String message, @Nullable Object... args) {
        final String formattedMessage = args != null && args.length > 0
                ? String.format(message, args) : message;
        Log.println(priority, "ChronoLog", formattedMessage);
    }
}
