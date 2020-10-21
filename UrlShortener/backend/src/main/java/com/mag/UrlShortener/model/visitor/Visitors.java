package com.mag.UrlShortener.model.visitor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Visitors {
  List<Visitor> visitors;
}
