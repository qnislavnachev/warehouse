package com.warehouse.exports.json;

import com.warehouse.exports.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.StringJoiner;

public class JsonExporter extends AbstractExporter {
  private final Marshaller marshaller;

  public JsonExporter() {
    this.marshaller = new JsonMarshaller();
  }

  public Report generateReport(List<? extends Exportable> exportables) {
    StringJoiner joiner = new StringJoiner(",\n", "[\n", "\n]");

    for (Exportable exportable : exportables) {
      String row = exportable.accept(marshaller);
      joiner.add(row);
    }

    ByteArrayInputStream content = new ByteArrayInputStream(joiner.toString().getBytes());
    return new Report(ReportType.JSON, content);
  }
}
