package com.warehouse.exports;

import com.warehouse.exports.csv.CsvExporter;
import com.warehouse.exports.json.JsonExporter;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component
public class ExporterFactory {

  public Exporter get(ExportType type) throws NotSupportedException {
    if (type.equals(ExportType.CSV)) {
      return new CsvExporter();
    }

    if (type.equals(ExportType.JSON)) {
      return new JsonExporter();
    }

    throw new NotSupportedException();
  }
}
