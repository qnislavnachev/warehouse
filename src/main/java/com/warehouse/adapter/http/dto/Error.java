package com.warehouse.adapter.http.dto;

public class Error {
  public String description;

  public Error() {
    this.description = "";
  }

  private Error(String description) {
    this.description = description;
  }

  public static Error of(String description, Object... placeholdersValues) {
    if (placeholdersValues.length == 0) {
      return new Error(description);
    }

    return new Error(String.format(description, placeholdersValues));
  }
}
