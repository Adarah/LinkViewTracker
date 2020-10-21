package com.mag.UrlShortener.model.redirect;

public interface RedirectRepository {
  String findByAlias(String alias);

  boolean isAliasAvailable(String alias);

  void create(String Alias, String target);
}
