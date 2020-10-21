package com.mag.UrlShortener.application.service;

import com.mag.UrlShortener.model.visitor.Visitor;
import com.mag.UrlShortener.model.visitor.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitorService {
  @Autowired VisitorRepository visitorRepository;

  public List<Visitor> getVisitors(String urlAlias) {
    return visitorRepository.findVisitorsByUrlAlias(urlAlias);
  }

  public void addVisitor(String urlAlias) {
    String ip = RequestUtil.getRemoteIP();
    visitorRepository.insert(urlAlias, ip);
  }
}
