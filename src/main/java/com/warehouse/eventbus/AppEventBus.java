package com.warehouse.eventbus;

import com.warehouse.core.events.Event;
import com.warehouse.eventbus.listeners.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AppEventBus implements EventBus {
  private final EventManager manager;
  private static final String eventHandlerMethodName = "handle";

  public AppEventBus(EventManager manager) {
    this.manager = manager;
  }

  @Override
  public void publish(Event event) {
    List<EventListener> listeners = manager.getListeners();

    for (EventListener listener : listeners) {
      handleEvent(event, listener);
    }
  }

  private void handleEvent(Event event, EventListener listener) {
    Thread thread = new Thread(() -> {
      try {
        Method method = listener.getClass().getMethod(eventHandlerMethodName, event.getClass());
        method.invoke(listener, event);

      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        // skip the listener because it do not support handling for that event
      }
    });

    thread.start();
  }
}
