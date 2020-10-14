package com.warehouse.eventbus;

import com.warehouse.core.events.Event;

public interface EventBus {

  void publish(Event event);
}
