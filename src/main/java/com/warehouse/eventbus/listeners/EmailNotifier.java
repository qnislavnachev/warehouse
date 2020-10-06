package com.warehouse.eventbus.listeners;

import com.warehouse.core.events.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class EmailNotifier implements EventListener {

  public void handle(UserRegisteredEvent event) {
    // sent greeting email to the user
  }
}
