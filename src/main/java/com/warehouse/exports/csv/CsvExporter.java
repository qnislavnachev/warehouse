package com.warehouse.exports.csv;

import com.warehouse.exports.AbstractExporter;
import com.warehouse.exports.Exportable;
import com.warehouse.exports.Marshaller;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CsvExporter extends AbstractExporter {
  private final Marshaller marshaller;
  private static final String extension = ".csv";

  public CsvExporter() {
    this.marshaller = new CsvMarshaller();
  }

  @Override
  public File export(List<? extends Exportable> exportables) throws IOException {
    StringBuilder builder = new StringBuilder();

    //TODO: headers should be added
    for (Exportable exportable : exportables) {
      String row = exportable.accept(marshaller);
      builder.append(row).append("\n");
    }

    return toFile(builder.toString(), extension);
  }
}
