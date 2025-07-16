package com.ShopingCart.dreamshops.service.category;

import com.ShopingCart.dreamshops.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategory(Long id);
    List<Category> getAllCategories();
}
