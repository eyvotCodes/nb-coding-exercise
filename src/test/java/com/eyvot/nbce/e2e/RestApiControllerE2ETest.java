package com.eyvot.nbce.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Tag("e2e")
@ActiveProfiles("test")
public class RestApiControllerE2ETest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;


    @Test
    void should_return_example_json_snippet() throws Exception {
        MvcResult result = mockMvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        String expectedSnippet = ""
                + "{\"id\":1,\"name\":\"Acura\",\"average_price\":702109},"
                + "{\"id\":2,\"name\":\"Audi\",\"average_price\":630759},"
                + "{\"id\":3,\"name\":\"Bentley\",\"average_price\":3342575},"
                + "{\"id\":4,\"name\":\"BMW\",\"average_price\":858702},"
                + "{\"id\":5,\"name\":\"Buick\",\"average_price\":290371},";
        assertThat(responseBody).contains(expectedSnippet);

        System.out.println("✔ GET /brands → List all brands");
        System.out.printf("  Found %d characters in total response%n", responseBody.length());
        Set<String> exampleNames = Set.of("Acura", "Audi", "Bentley", "BMW", "Buick");
        System.out.println("  Example brands in JSON response...");
        JsonNode brandsArray = objectMapper.readTree(responseBody);
        brandsArray.forEach(brand -> {
            if (exampleNames.contains(brand.get("name").asText())) {
                System.out.println(brand.toPrettyString() + ",");
            }
        });
    }

}
