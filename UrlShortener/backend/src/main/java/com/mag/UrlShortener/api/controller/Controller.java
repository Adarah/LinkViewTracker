package com.mag.UrlShortener.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mag.UrlShortener.application.service.RedirectService;
import com.mag.UrlShortener.application.service.VisitorService;
import com.mag.UrlShortener.model.redirect.Redirect;
import com.mag.UrlShortener.model.visitor.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class Controller {
  @Autowired RedirectService redirectService;
  @Autowired VisitorService visitorService;
  @Autowired ObjectMapper objectMapper;

  @GetMapping("/")
  public String homePage() {
    return "Hello, world!";
  }

  @GetMapping("/{urlAlias}")
  @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
  @CrossOrigin(origins = "http://localhost:3000", exposedHeaders = "Location")
  public RedirectView redirect(
      @PathVariable(value = "urlAlias") String urlAlias, HttpServletRequest request) {
    String url = redirectService.findByAlias(urlAlias);
    if (!url.equals("/")) {
      visitorService.addVisitor(urlAlias);
    }
    return new RedirectView(url);
  }

  @PostMapping("/")
  @ResponseStatus(value = HttpStatus.CREATED)
  public Redirect createRedirect(@Validated @RequestBody Redirect redirect) {
    return redirectService.create(redirect);
  }

  @GetMapping("/info/{urlAlias}")
  public String getVisitors(@PathVariable("urlAlias") String urlAlias)
      throws JsonProcessingException {
    List<Visitor> visitors = visitorService.getVisitors(urlAlias);
    final ObjectWriter writer = objectMapper.writer().withRootName("visitors");
    return writer.writeValueAsString(visitors);
  }
}
