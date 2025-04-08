package com.eyvot.nbce.infrastructure.db;

import com.eyvot.nbce.domian.contract.db.BrandDbContract;
import com.eyvot.nbce.domian.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface BrandRepository extends JpaRepository<Brand, Integer>, BrandDbContract {

    @Override
    @Query(value = """
        SELECT
            b.id,
            b.name,
            FLOOR(AVG(m.average_price)) AS average_price
        FROM brands b
        LEFT JOIN car_models m ON m.brand_id = b.id
        GROUP BY b.id, b.name
        ORDER BY b.id
    """, nativeQuery = true)
    List<Brand> findAllBrandsCalculatingAverage();

}
