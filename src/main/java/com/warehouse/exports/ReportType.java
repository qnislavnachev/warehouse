package com.warehouse.exports;

public enum ReportType {
  CSV(".csv"), JSON(".json");

  private final String extension;

  ReportType(String extension) {
    this.extension = extension;
  }

  public String getFileExtension() {
    return extension;
  }
}
