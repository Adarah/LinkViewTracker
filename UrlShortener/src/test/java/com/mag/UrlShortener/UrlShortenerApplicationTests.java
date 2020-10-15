package com.mag.UrlShortener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.mag.UrlShortener.model.redirect.Redirect;
import com.mag.UrlShortener.model.redirect.RedirectRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@TestPropertySource(properties = {"SECRET_KEY=foobar"})
@Testcontainers
class UrlShortenerApplicationTests {
  static final PostgreSQLContainer postgreSQLContainer;

  static {
    postgreSQLContainer =
        (PostgreSQLContainer)
            new PostgreSQLContainer("postgres:12.4")
                .withDatabaseName("test")
                .withUsername("inmemory")
                .withPassword("inmemory")
                .withReuse(true);
    postgreSQLContainer.start();
  }

  @Autowired RedirectRepository redirectRepository;
  @Autowired MockMvc mockMvc;
  @Autowired Flyway flyway;

  @Value("#{environment.SECRET_KEY}")
  String secretKey;

  @LocalServerPort private int port;

  @DynamicPropertySource
  static void datasourceConfig(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
  }

  @BeforeAll
  private void initDatabaseProperties() {
    flyway.clean();
    flyway.migrate();
  }

  private String toJson(Object obj) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    return ow.writeValueAsString(obj);
  }

  @Test
  @Transactional
  public void shouldHaveDataInRepository() {
    assertEquals(redirectRepository.findByAlias("foo"), "https://stackoverflow.com");
  }

  @Test
  @Transactional
  public void shouldRedirectToHomePage() throws Exception {
    this.mockMvc
        .perform(get("/gohomeyouredrunk"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  @Transactional
  public void shouldRedirectToTarget() throws Exception {
    this.mockMvc
        .perform(get("/foo"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("https://stackoverflow.com"));

    this.mockMvc
        .perform(get("/bar"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("https://google.com"));

    this.mockMvc
        .perform(get("/baz"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("https://reddit.com"));
  }

  @Test
  @Transactional
  public void cacheControlHeaderShouldBeNoStore() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(get("/bar")).andReturn();
    String header = mvcResult.getResponse().getHeader("Cache-control");
    assertThat(header).contains("no-store");
  }

  @Test
  @Transactional
  public void shouldCreateRedirectWithRandomTitle() throws Exception {
    Redirect redirect = new Redirect();
    redirect.setTarget("https://facebook.com");
    String json = toJson(redirect);
    String response =
        this.mockMvc
            .perform(post("/").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();
    String id = JsonPath.read(response, "$.redirect.alias");
    assertThat(id).hasSize(7);
  }

  @Test
  @Transactional
  public void shouldCreateRedirectWithCustomTitle() throws Exception {
    String title = "randomtitle";
    Redirect redirect = new Redirect();
    redirect.setAlias(title);
    redirect.setTarget("https://facebook.com");
    String json = toJson(redirect);

    this.mockMvc
        .perform(post("/").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().json(String.format("{'redirect': {'alias': '%s'}}", title)));
  }

  @Test
  @Transactional
  public void shouldRejectInvalidRedirectId() throws Exception {
    var redirect = new Redirect();
    redirect.setAlias("a");
    redirect.setTarget("https://google.com");
    String json = toJson(redirect);

    this.mockMvc
        .perform(post("/").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @Transactional
  public void shouldShowI18nErrorMessage() throws Exception {
    var redirect = new Redirect();
    redirect.setTarget("");
    String json = toJson(redirect);

    this.mockMvc
        .perform(
            post("/")
                .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].message").value("URL alvo não pode estar em branco"));
  }

  @Test
  @Transactional
  public void shouldRejectDuplicateAlias() throws Exception {
    var redirect = new Redirect();
    redirect.setAlias("foo");
    redirect.setTarget("https://example.com");
    String json = toJson(redirect);

    this.mockMvc
        .perform(post("/").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.errors[0].message").value("Alias was already taken"));
  }

  @Test
  @Transactional
  public void shouldOnlyAllowAuthorized() throws Exception {

    this.mockMvc.perform(get("/info/foo")).andExpect(status().is4xxClientError());

    this.mockMvc
        .perform(get("/info/foo").header("Authorization", "Bearer " + secretKey))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  @Transactional
  public void shouldReceiveVisitorIps() throws Exception {
    this.mockMvc
        .perform(get("/info/foo").header("Authorization", "Bearer " + secretKey))
        .andExpect(status().is2xxSuccessful())
        .andExpect(
            content()
                .json(
                    "{'visitors': [{'ip': '192.168.10.0', 'totalVisits': 1}, {'ip': '192.172.10.154', 'totalVisits': 1}]}}"));
  }

  @Test
  @Transactional
  public void shouldAddNewVisitor() throws Exception {
    this.mockMvc.perform(get("/baz"));

    this.mockMvc
        .perform(get("/info/baz").header("Authorization", "Bearer " + secretKey))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().json("{'visitors': [{'ip': '127.0.0.1', 'totalVisits': 1}]}"));
  }

  @Test
  @Transactional
  public void shouldIncreaseVisitorCount() throws Exception {
    this.mockMvc.perform(get("/baz"));
    this.mockMvc.perform(get("/baz"));
    this.mockMvc.perform(get("/baz"));

    this.mockMvc
            .perform(get("/info/baz").header("Authorization", "Bearer " + secretKey))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().json("{'visitors': [{'ip': '127.0.0.1', 'totalVisits': 3}]}"));
  }
}
