package com.eyvot.nbce.domian.contract.rest;

import com.eyvot.nbce.domian.entity.Brand;
import com.eyvot.nbce.domian.entity.Model;

import java.util.List;


public interface RestApiContract {

    List<Brand> listBrands(); // GET /brands
    List<Model> listModelsForBrand(int brandId); // GET /brands/:id/models
    Brand registerNewBrand(String name); // POST /brands
    Model registerNewModelForBrand(int brandId, String modelName, int modelAveragePrice); // POST /brands/:id/models
    Model editModel(int id, int averagePrice); // PUT /models/:id
    List<Model> listModelsForRangeOfPrices(int greater, int lower); // GET /models?greater=&lower=

}
