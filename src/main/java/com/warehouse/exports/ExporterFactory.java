package com.warehouse.exports;

import com.warehouse.exports.csv.CsvExporter;
import com.warehouse.exports.json.JsonExporter;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component
public class ExporterFactory {

  public Exporter get(ReportType type) throws NotSupportedException {
    if (type.equals(ReportType.CSV)) {
      return new CsvExporter();
    }

    if (type.equals(ReportType.JSON)) {
      return new JsonExporter();
    }

    throw new NotSupportedException();
  }
}
