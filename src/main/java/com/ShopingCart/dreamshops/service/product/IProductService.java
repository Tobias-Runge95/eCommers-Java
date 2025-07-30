package com.ShopingCart.dreamshops.service.product;

import com.ShopingCart.dreamshops.DTO.Image.ProductDto;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.request.AddProductRequest;
import com.ShopingCart.dreamshops.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(long id);
    Product addProduct(AddProductRequest productRequest);
    Product updateProduct(ProductUpdateRequest request, long productId);
    void deleteProduct(long id);
    List<Product> getProductsByCategory(String categoryName);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String categoryName, String brand);
    List<Product> getProductsByName(String productName);
    List<Product> getProductsByBrandAndName(String brand, String productName);
    Long countProductsByBrandAndName(String brand, String productName);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
