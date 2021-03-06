package com.warehouse.adapter.http.dto;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Response {

  public static ResponseEntity<Object> ok(Object body) {
    return ResponseEntity.status(200).body(body);
  }

  public static ResponseEntity<Object> ok() {
    return ResponseEntity.status(200).build();
  }

  public static ResponseEntity<Object> ok(String fileName, Long contentLength, InputStreamResource content) {
    return ResponseEntity.status(200)
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", fileName))
            .contentLength(contentLength)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(content);
  }

  public static ResponseEntity<Object> created() {
    return ResponseEntity.status(201).build();
  }

  public static ResponseEntity<Object> created(Object body) {
    return ResponseEntity.status(201).body(body);
  }

  public static ResponseEntity<Object> badRequest(Object body) {
    return ResponseEntity.status(400).body(body);
  }

  public static ResponseEntity<Object> forbidden(Object body) {
    return ResponseEntity.status(403).body(body);
  }

  public static ResponseEntity<Object> notFound(Object body) {
    return ResponseEntity.status(404).body(body);
  }

  public static ResponseEntity<Object> conflict(Object body) {
    return ResponseEntity.status(409).body(body);
  }

  public static ResponseEntity<Object> serverError(Object body) {
    return ResponseEntity.status(500).body(body);
  }
}
