package com.eyvot.nbce.domian.contract.db;

import com.eyvot.nbce.domian.entity.Brand;

import java.util.List;


public interface BrandDbContract {

    List<Brand> findAllBrandsCalculatingAverage();

    // TODO: Pending to implement
    // Brand addNewBrand(String name);

}
