package com.eyvot.nbce.infrastructure.db;

import com.eyvot.nbce.domian.contract.db.ModelDbContract;
import com.eyvot.nbce.domian.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ModelRepository extends JpaRepository<Model, Integer>, ModelDbContract {}
