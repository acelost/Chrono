package com.acelost.chrono;

import android.os.SystemClock;
import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicLong;

public final class Timer {

    private static final int NOT_STARTED = -1;

    @NonNull
    private final String subject;

    @NonNull
    private final AtomicLong startTime = new AtomicLong(NOT_STARTED);

    @NonNull
    private final AtomicLong stoppedIn = new AtomicLong();

    @NonNull
    public static Timer startNew() {
        return createNew().start();
    }

    @NonNull
    public static Timer startNew(@NonNull final String subject) {
        return createNew(subject).start();
    }

    @NonNull
    public static Timer createNew() {
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = null;
        boolean chronoPackageAchieved = false;
        for (int i = 0; i < trace.length; i++) {
            final StackTraceElement element = trace[i];
            final String className = element.getClassName();
            if (!chronoPackageAchieved) {
                chronoPackageAchieved = className.startsWith("com.acelost.chrono.");
            } else if (!className.startsWith("com.acelost.chrono.")) {
                caller = element;
                break;
            }
        }
        final String subject = caller != null ? String.format(
                ".(%s:%s):%s",
                caller.getFileName(),
                caller.getLineNumber(),
                caller.getMethodName()
        ) : "unknown-caller";
        return new Timer(subject);
    }

    @NonNull
    public static Timer createNew(@NonNull final String subject) {
        return new Timer(subject);
    }

    Timer(@NonNull final String subject) {
        this.subject = subject;
    }

    @NonNull String getSubject() {
        return subject;
    }

    @NonNull
    public Timer start() {
        if (isStarted()) {
            Chrono.warn("Timer \'%s\' is already started!", subject);
        } else {
            Chrono.log("Start timer \'%s\'", subject);
            startTime.set(now());
        }
        return this;
    }

    public void capture(@NonNull final String message) {
        if (isStarted()) {
            final long past = past();
            Chrono.log("Capture \'%s\' for \'%s\' after %s", message, subject, ms(past));
        } else {
            Chrono.warn("Can't capture \'%s\' for \'%s\' because this timer is not started", message, subject);
        }
    }

    public void stop() {
        if (isStarted()) {
            final long past = past();
            startTime.set(NOT_STARTED);
            stoppedIn.set(past);
            Chrono.log("Stop timer \'%s\' after \'%s\'", subject, ms(past));
        } else {
            Chrono.warn("Can't stop \'%s\' because timer is not started", subject);
        }
    }

    public void resume() {
        if (isStarted()) {
            Chrono.warn("Timer \'%s\' is already resumed", subject);
        } else {
            final long now = now();
            final long past = stoppedIn.get();
            startTime.set(now - past);
            stoppedIn.set(0);
            Chrono.log("Resume timer \'%s\' in %d from %s", subject, now, ms(past));
        }
    }

    public long time() {
        return startTime.get() == NOT_STARTED ? stoppedIn.get() : past();
    }

    @NonNull
    public Timer toBunch(@NonNull final String bunch) {
        Chrono.toBunch(bunch, this);
        return this;
    }

    private static long now() {
        return SystemClock.elapsedRealtimeNanos();
    }

    private long past() {
        return now() - startTime.get();
    }

    boolean isStarted() {
        return startTime.get() != NOT_STARTED;
    }

    @NonNull
    private static String ms(long ns) {
        return Chrono.ms(ns);
    }
}
