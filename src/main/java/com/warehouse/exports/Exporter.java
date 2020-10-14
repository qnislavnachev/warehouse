package com.warehouse.exports;

import java.util.List;

public interface Exporter {

  Report generateReport(List<? extends Exportable> exportables);
}
