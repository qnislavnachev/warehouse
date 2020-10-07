package com.warehouse.exports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public abstract class AbstractExporter implements Exporter {

  public File toFile(String value, String extension) throws IOException {
    String fileName = UUID.randomUUID().toString() + extension;

    File file = new File(fileName);
    FileWriter fileWriter = new FileWriter(file);

    fileWriter.write(value);

    return file;
  }
}
