package com.mag.UrlShortener.model.visitor;

// This has nothing to do with the Visitor design pattern

import java.util.List;

public interface VisitorRepository {
  // List of IPs. For now represented as Strings, but I might change it to IP objects later.
  List<Visitor> findVisitorsByUrlAlias(String urlAlias);

  void insert(String urlAlias, String ip);
}
