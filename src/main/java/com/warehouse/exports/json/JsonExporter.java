package com.warehouse.exports.json;

import com.warehouse.exports.AbstractExporter;
import com.warehouse.exports.Exportable;
import com.warehouse.exports.Marshaller;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonExporter extends AbstractExporter {
  private final Marshaller marshaller;
  private static final String extension = ".json";

  public JsonExporter() {
    this.marshaller = new JsonMarshaller();
  }

  public File export(List<? extends Exportable> exportables) throws IOException {
    StringBuilder builder = new StringBuilder();

    builder.append("[\n");

    for (Exportable exportable : exportables) {
      String row = exportable.accept(marshaller);
      builder.append(row).append(",").append("\n");
    }

    builder.append("]");

    return buildFile(builder.toString(), extension);
  }
}
