package ru.nsu.ccfit.petrov.task5.server;

import ru.nsu.ccfit.petrov.task5.listener.ListeningSupport;
import ru.nsu.ccfit.petrov.task5.listener.Listener;
import ru.nsu.ccfit.petrov.task5.listener.event.RequestReceivedEvent;

public abstract class AbstractRequestHandler
    extends Thread {

    private final ListeningSupport listeningSupport = new ListeningSupport();

    protected abstract Object receiveRequest();
    protected abstract Object generateResponse(Object request);
    protected abstract void sendResponse(Object response);

    /**
     * If this thread was constructed using a separate {@code Runnable} run object, then that {@code Runnable} object's
     * {@code run} method is called; otherwise, this method does nothing and returns.
     * <p>
     * Subclasses of {@code Thread} should override this method.
     *
     * @see #start()
     * @see #stop()
     */
    @Override
    public void run() {
        while (true) {
            Object request = receiveRequest();
            listeningSupport.notifyListeners(new RequestReceivedEvent());
            Object response = generateResponse(request);
            sendResponse(response);
        }
    }

    public void addListener(Listener listener) {
        listeningSupport.addListener(listener);
    }

    public void removeListener(Listener listener) {
        listeningSupport.removeListener(listener);
    }
}