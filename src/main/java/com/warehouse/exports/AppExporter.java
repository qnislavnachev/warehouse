package com.warehouse.exports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AppExporter implements Exporter {
  private final Marshaller marshaller;
  private final String fileExtension;

  public AppExporter(Marshaller marshaller, String fileExtension) {
    this.marshaller = marshaller;
    this.fileExtension = fileExtension;
  }

  public File export(List<? extends Exportable> exportables) throws IOException {
    String fileName = UUID.randomUUID().toString() + fileExtension;

    File file = new File(fileName);
    FileWriter fileWriter = new FileWriter(file);

    for (Exportable exportable : exportables) {
      String row = exportable.accept(marshaller);
      fileWriter.write(row);
    }

    return file;
  }
}
