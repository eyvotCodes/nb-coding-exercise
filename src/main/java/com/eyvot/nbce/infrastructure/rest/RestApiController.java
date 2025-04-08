package com.eyvot.nbce.infrastructure.rest;

import com.eyvot.nbce.application.CarsService;
import com.eyvot.nbce.domian.contract.rest.RestApiContract;
import com.eyvot.nbce.domian.entity.Brand;
import com.eyvot.nbce.domian.entity.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/")
public class RestApiController implements RestApiContract {

    private final CarsService carsService;


    public RestApiController(CarsService carsService) {
        this.carsService = carsService;
    }


    @Override
    @GetMapping("/brands")
    public List<Brand> listBrands() {
        return carsService.getBrands();
    }

    @Override
    public List<Model> listModelsForBrand(int brandId) {
        return List.of();
    }

    @Override
    public Brand registerNewBrand(String name) {
        return null;
    }

    @Override
    public Model registerNewModelForBrand(int brandId, String modelName, int modelAveragePrice) {
        return null;
    }

    @Override
    public Model editModel(int id, int averagePrice) {
        return null;
    }

    @Override
    public List<Model> listModelsForRangeOfPrices(int greater, int lower) {
        return List.of();
    }

}
