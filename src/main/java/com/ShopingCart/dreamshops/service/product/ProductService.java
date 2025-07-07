package com.ShopingCart.dreamshops.service.product;

import com.ShopingCart.dreamshops.exceptions.ProductNotFoundException;
import com.ShopingCart.dreamshops.model.Category;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.repository.CategoryRepository;
import com.ShopingCart.dreamshops.repository.ProductRepository;
import com.ShopingCart.dreamshops.request.AddProductRequest;
import com.ShopingCart.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService  implements IProductService {

    private final ProductRepository _repository;
    private final CategoryRepository _categoryRepository;

    @Override
    public List<Product> getAllProducts() {
        return _repository.findAll();
    }

    @Override
    public Product getProductById(long id) {
        return _repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public Product addProduct(AddProductRequest productRequest) {
        Category category = Optional.ofNullable(_categoryRepository.findByName((productRequest.getCategory().getName())))
                .orElseGet(() ->{
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return _categoryRepository.save(newCategory);
                });
        productRequest.setCategory(category);

        return _repository.save(createProduct(productRequest, category));
    }

    private Product createProduct(AddProductRequest productRequest, Category category) {
        return new Product(
                productRequest.getName(),
                productRequest.getBrand(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                category,
                productRequest.getInventory()
        );
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, long productId) {
        return _repository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct,request))
                .map(_repository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());

        Category category = _categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public void deleteProduct(long id) {
        _repository.findById(id)
                .ifPresentOrElse(_repository::delete, ()->{throw new ProductNotFoundException("Product not found");});
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return _repository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return _repository.findByBrandName(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String categoryName, String brand) {
        return _repository.findByCategoryNameAndBrand(categoryName, brand);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return _repository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String productName) {
        return _repository.findByBrandAndName(brand, productName);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String productName) {
        return _repository.countByPrandAndName(brand, productName);
    }
}
