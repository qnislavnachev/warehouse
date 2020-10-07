package com.warehouse.exports;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Exporter {

  File export(List<? extends Exportable> exportables) throws IOException;
}
