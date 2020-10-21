package com.mag.UrlShortener.infrastructure.repository;

import com.mag.UrlShortener.model.visitor.Visitor;
import com.mag.UrlShortener.model.visitor.VisitorRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.codegen.maven.example.Tables.VISITOR;

@Repository
public class VisitorRepositoryImpl implements VisitorRepository {
  @Autowired DSLContext create;

  @Override
  public List<Visitor> findVisitorsByUrlAlias(String urlAlias) {
    return create
        .select(VISITOR.IP, VISITOR.TOTAL_VISITS)
        .from(VISITOR)
        .where(VISITOR.REDIRECT_ALIAS.eq(urlAlias))
        .fetchInto(Visitor.class);
  }

  @Override
  public void insert(String urlAlias, String ip) {
    create
        .insertInto(VISITOR, VISITOR.REDIRECT_ALIAS, VISITOR.IP)
        .values(urlAlias, ip)
        .onDuplicateKeyUpdate()
        .set(VISITOR.TOTAL_VISITS, VISITOR.TOTAL_VISITS.plus(1))
        .execute();
  }
}
