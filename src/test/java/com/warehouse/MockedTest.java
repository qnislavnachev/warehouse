package com.warehouse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.concurrent.Synchroniser;

public class MockedTest {
  private final Mockery mock = new Mockery() {{
    setThreadingPolicy(new Synchroniser());
  }};

  public final <T> T mock(Class<T> clazz) {
    return mock.mock(clazz);
  }

  public void expect(Expectations expectations) {
    mock.checking(expectations);
  }
}
