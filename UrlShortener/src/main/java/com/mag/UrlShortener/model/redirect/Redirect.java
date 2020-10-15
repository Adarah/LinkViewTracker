package com.mag.UrlShortener.model.redirect;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.mag.UrlShortener.infrastructure.validator.Expensive;
import com.mag.UrlShortener.infrastructure.validator.KeyType;
import com.mag.UrlShortener.infrastructure.validator.UniqueKey;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@JsonRootName("redirect")
@GroupSequence({Redirect.class, Expensive.class})
public class Redirect {
  @Size(min = 3, max = 30, message = "{errorMessage.alias.size}")
  @Pattern(regexp = "^[A-Za-z0-9]+", message = "{errorMessage.alias.alphanumeric}")
  @UniqueKey(
      value = KeyType.ALIAS,
      message = "{errorMessage.alias.unique}",
      groups = Expensive.class)
  String alias;

  @URL(message = "{errorMessage.target.URL}")
  @NotBlank(message = "{errorMessage.target.blank}")
  String target;
}
