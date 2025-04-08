package com.eyvot.nbce.application;

import com.eyvot.nbce.domian.entity.Brand;
import com.eyvot.nbce.infrastructure.db.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CarsService {

    private final BrandRepository carsRepository;


    public CarsService(BrandRepository carsRepository) {
        this.carsRepository = carsRepository;
    }


    public List<Brand> getBrands() {
        return carsRepository.findAllBrandsCalculatingAverage();
    }

}
