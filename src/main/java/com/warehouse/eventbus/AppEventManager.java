package com.warehouse.eventbus;

import com.warehouse.eventbus.listeners.EventListener;

import java.util.ArrayList;
import java.util.List;

public class AppEventManager implements EventManager {
  private final List<EventListener> listeners;

  public AppEventManager() {
    this.listeners = new ArrayList<>();
  }

  @Override
  public void register(EventListener listeners) {
    this.listeners.add(listeners);
  }

  @Override
  public List<EventListener> getListeners() {
    return listeners;
  }
}
