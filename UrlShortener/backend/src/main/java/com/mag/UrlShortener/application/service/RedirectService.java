package com.mag.UrlShortener.application.service;

import com.mag.UrlShortener.model.redirect.Redirect;
import com.mag.UrlShortener.model.redirect.RedirectRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedirectService {

  @Autowired RedirectRepository redirectRepository;

  @Transactional(readOnly = true)
  public String findByAlias(String alias) {
    return redirectRepository.findByAlias(alias);
  }

  @Transactional
  public Redirect create(Redirect redirect) {
    var response = new Redirect();
    String alias = redirect.getAlias();
    if (alias == null) {
      alias = generateValidId();
    }
    response.setAlias(alias);
    response.setTarget(redirect.getTarget());
    redirectRepository.create(response.getAlias(), response.getTarget());

    return response;
  }

  private String generateValidId() {
    String aliasCandidate;
    do {
      aliasCandidate = RandomStringUtils.randomAlphanumeric(7);
    } while (!redirectRepository.isAliasAvailable(aliasCandidate));
    return aliasCandidate;
  }
}
