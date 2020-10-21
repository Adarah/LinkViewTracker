package com.mag.UrlShortener.infrastructure.repository;

import com.mag.UrlShortener.model.redirect.RedirectRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.codegen.maven.example.Tables.REDIRECT;

@Repository
public class RedirectRepositoryImpl implements RedirectRepository {
  @Autowired DSLContext create;

  @Override
  public String findByAlias(String alias) {
    return create
        .select(REDIRECT.TARGET)
        .from(REDIRECT)
        .where(REDIRECT.ALIAS.eq(alias))
        .fetchOptional()
        .map(rec1 -> rec1.getValue(REDIRECT.TARGET))
        .orElseGet(() -> "/");
  }

  @Override
  public boolean isAliasAvailable(String alias) {
    return create.selectFrom(REDIRECT).where(REDIRECT.ALIAS.eq(alias)).fetchOptional().isEmpty();
  }

  @Override
  public void create(String alias, String target) {
    create.insertInto(REDIRECT, REDIRECT.ALIAS, REDIRECT.TARGET).values(alias, target).execute();
  }
}
