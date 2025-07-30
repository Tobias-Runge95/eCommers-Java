package com.ShopingCart.dreamshops.service.product;

import com.ShopingCart.dreamshops.DTO.Image.ImageDto;
import com.ShopingCart.dreamshops.DTO.Image.ProductDto;
import com.ShopingCart.dreamshops.exceptions.AlreadyExistsException;
import com.ShopingCart.dreamshops.exceptions.ProductNotFoundException;
import com.ShopingCart.dreamshops.model.Category;
import com.ShopingCart.dreamshops.model.Image;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.repository.CategoryRepository;
import com.ShopingCart.dreamshops.repository.ImageRepository;
import com.ShopingCart.dreamshops.repository.ProductRepository;
import com.ShopingCart.dreamshops.request.AddProductRequest;
import com.ShopingCart.dreamshops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService  implements IProductService {

    private final ProductRepository _repository;
    private final CategoryRepository _categoryRepository;
    private final ModelMapper _mapper;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

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

        if(productExists(productRequest.getName(), productRequest.getName())) {
            throw new AlreadyExistsException(String.format("%s %s Product already exists", productRequest.getBrand(), productRequest.getName()));
        }

        Category category = Optional.ofNullable(_categoryRepository.findByName((productRequest.getCategory().getName())))
                .orElseGet(() ->{
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return _categoryRepository.save(newCategory);
                });
        productRequest.setCategory(category);

        return _repository.save(createProduct(productRequest, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
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
        return _repository.findByBrand(brand);
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
        return _repository.countByBrandAndName(brand, productName);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = _mapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().map(image -> _mapper.map(image, ImageDto.class)).toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
