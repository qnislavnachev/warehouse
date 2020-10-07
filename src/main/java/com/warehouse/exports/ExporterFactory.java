package com.warehouse.exports;

import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component
public class ExporterFactory {

  public Exporter get(ExportType type) throws NotSupportedException {
    if (type.equals(ExportType.CSV)) {
      return new AppExporter(new CsvMarshaller(), ".csv");
    }

    if (type.equals(ExportType.JSON)) {
      return new AppExporter(new JsonMarshaller(), ".json");
    }

    throw new NotSupportedException();
  }
}
