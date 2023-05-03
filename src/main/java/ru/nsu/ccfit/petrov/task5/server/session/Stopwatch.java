package ru.nsu.ccfit.petrov.task5.server.session;

import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.server.event.StopwatchFinishedEvent;


/**
 * The type {@code Stopwatch} is class for measuring time.
 *
 * @author ptrvsrg
 */
@RequiredArgsConstructor
public class Stopwatch {

    private static final int DELAY = 0;
    private static final int SECOND = 1000;
    private static final int PERIOD = 1000;
    private final int timeoutMillis;
    private final ListeningSupport listeningSupport = new ListeningSupport();
    private int millis = 0;
    private Timer timer;

    private class StopwatchTask
        extends TimerTask {

        @Override
        public void run() {
            millis += SECOND;

            if (millis == timeoutMillis) {
                listeningSupport.notifyListeners(new StopwatchFinishedEvent());
            }
        }
    }

    /**
     * Runs stopwatch.
     */
    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new StopwatchTask(), DELAY, PERIOD);
    }

    /**
     * Resets stopwatch.
     */
    public void reset() {
        millis = 0;
    }

    /**
     * Stops stopwatch.
     */
    public void stop() {
        timer.purge();
    }

    /**
     * Adds listener.
     *
     * @param listener the listener
     */
    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }
}
