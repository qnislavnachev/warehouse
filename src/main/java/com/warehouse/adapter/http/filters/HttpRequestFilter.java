package com.warehouse.adapter.http.filters;

import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

abstract class HttpRequestFilter extends OncePerRequestFilter {

  public abstract ResponseEntity<Object> filter(HttpServletRequest request);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    ResponseEntity<Object> responseEntity = this.filter(request);
    HttpStatus status = responseEntity.getStatusCode();

    if (status.is2xxSuccessful()) {
      chain.doFilter(request, response);
      return;
    }

    response.setStatus(status.value());

    setBody(responseEntity.getBody(), response);
    setHeaders(responseEntity.getHeaders(), response);
  }

  private void setBody(Object body, HttpServletResponse response) throws IOException {
    String jsonBody = new Gson().toJson(body);

    PrintWriter writer = response.getWriter();
    writer.print(jsonBody);
  }

  private void setHeaders(HttpHeaders headers, HttpServletResponse response) {
//    headers.entrySet().forEach(header -> {
//      response.addHeader(header.getKey(), header.getValue());
//    });
  }
}
