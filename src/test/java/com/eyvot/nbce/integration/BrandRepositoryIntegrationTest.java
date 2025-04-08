package com.eyvot.nbce.integration;

import com.eyvot.nbce.domian.entity.Brand;
import com.eyvot.nbce.infrastructure.db.BrandRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@Tag("integration")
@ActiveProfiles("test")
public class BrandRepositoryIntegrationTest {

    @Autowired
    BrandRepository repository;


    @Test
    void should_return_brands_with_average_price() {
        List<Brand> result = repository.findAllBrandsCalculatingAverage();
        NumberFormat currencyFormat = NumberFormat.getIntegerInstance(Locale.US);
        Set<String> expectedBrands = Set.of("Acura", "Audi", "Bentley", "BMW", "Buick");
        System.out.println("✔ Should return brands with the average price of their related models");
        System.out.printf("  Found %d brands in total (showing only those in the example)%n", result.size());

        result.stream()
                .filter(b -> expectedBrands.contains(b.getName()))
                .forEach(brand -> {
                    String formattedPrice = currencyFormat.format(brand.getAveragePrice());
                    System.out.printf("    ↳ %s → avg: %s%n", brand.getName(), formattedPrice);
                });

        assertThat(result).hasSizeGreaterThanOrEqualTo(5);

        assertThat(result).anySatisfy(brand -> {
            assertThat(brand.getId()).isEqualTo(1);
            assertThat(brand.getName()).isEqualTo("Acura");
            assertThat(brand.getAveragePrice()).isEqualTo(702109);
        });

        assertThat(result).anySatisfy(brand -> {
            assertThat(brand.getId()).isEqualTo(2);
            assertThat(brand.getName()).isEqualTo("Audi");
            assertThat(brand.getAveragePrice()).isEqualTo(630759);
        });

        assertThat(result).anySatisfy(brand -> {
            assertThat(brand.getId()).isEqualTo(3);
            assertThat(brand.getName()).isEqualTo("Bentley");
            assertThat(brand.getAveragePrice()).isEqualTo(3342575);
        });

        assertThat(result).anySatisfy(brand -> {
            assertThat(brand.getId()).isEqualTo(4);
            assertThat(brand.getName()).isEqualTo("BMW");
            assertThat(brand.getAveragePrice()).isEqualTo(858702);
        });

        assertThat(result).anySatisfy(brand -> {
            assertThat(brand.getId()).isEqualTo(5);
            assertThat(brand.getName()).isEqualTo("Buick");
            assertThat(brand.getAveragePrice()).isEqualTo(290371);
        });

    }

}
