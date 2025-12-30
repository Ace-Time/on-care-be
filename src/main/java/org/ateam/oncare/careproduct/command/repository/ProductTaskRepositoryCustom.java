package org.ateam.oncare.careproduct.command.repository;

import org.ateam.oncare.careproduct.command.entity.ProductTask;

public interface ProductTaskRepositoryCustom {
    ProductTask selectByProductId(String productId);
}
