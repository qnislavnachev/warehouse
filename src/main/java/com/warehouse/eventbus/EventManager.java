package com.warehouse.eventbus;

import com.warehouse.eventbus.listeners.EventListener;

import java.util.List;

public interface EventManager {

  void register(EventListener listeners);

  List<EventListener> getListeners();
}
