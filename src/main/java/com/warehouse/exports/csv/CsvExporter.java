package com.warehouse.exports.csv;

import com.warehouse.exports.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.StringJoiner;

public class CsvExporter extends AbstractExporter {
  private final Marshaller marshaller;

  public CsvExporter() {
    this.marshaller = new CsvMarshaller();
  }

  @Override
  public Report generateReport(List<? extends Exportable> exportables) {
    StringJoiner joiner = new StringJoiner(",\n");

    for (Exportable exportable : exportables) {
      String row = exportable.accept(marshaller);
      joiner.add(row);
    }

    ByteArrayInputStream content = new ByteArrayInputStream(joiner.toString().getBytes());
    return new Report(ReportType.CSV, content);
  }
}
