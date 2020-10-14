package com.warehouse.exports;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

public class Report {
  private final String fileName;
  private final ByteArrayInputStream content;
  private final ReportType reportType;

  public Report(ReportType reportType, ByteArrayInputStream content) {
    this.content = content;
    this.reportType = reportType;
    this.fileName = UUID.randomUUID().toString();
  }

  public InputStream getContent() {
    return content;
  }

  public Long getContentLength() {
    return (long) content.available();
  }

  public String getFileName() {
    return fileName + reportType.getFileExtension();
  }
}
