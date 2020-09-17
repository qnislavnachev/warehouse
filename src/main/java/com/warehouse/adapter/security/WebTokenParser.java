package com.warehouse.adapter.security;

public interface WebTokenParser {

  WebToken parse(String token);
}
