package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.CategoryBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.CategoryDao;
import com.example.billingsystem.dto.CategoryDto;
import com.example.billingsystem.entity.Category;
import com.example.billingsystem.util.KeyGenerator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CategoryBoImpl implements CategoryBo {

    private final CategoryDao categoryDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.CATEGORY);

    @Override
    public boolean createCategory(CategoryDto categoryDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (categoryDto.getId() == null || categoryDto.getId().trim().isEmpty()) {
            categoryDto.setId(KeyGenerator.generateId());
        }

        return categoryDao.create(new Category(
                categoryDto.getId(),
                categoryDto.getCategoryName(),
                categoryDto.getDescription(),
                categoryDto.getCategoryId(),
                categoryDto.isActive(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updateCategory(CategoryDto categoryDto) throws SQLException, ClassNotFoundException {
        return categoryDao.update(new Category(
                categoryDto.getId(),
                categoryDto.getCategoryName(),
                categoryDto.getDescription(),
                categoryDto.getCategoryId(),
                categoryDto.isActive(),
                categoryDto.getCreatedAt(),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean deleteCategory(String id) throws SQLException, ClassNotFoundException {
        return categoryDao.delete(id);
    }

    @Override
    public CategoryDto getCategory(String id) throws SQLException, ClassNotFoundException {
        Category category = categoryDao.find(id);
        return category != null ? convertToDto(category) : null;
    }

    @Override
    public List<CategoryDto> getAllCategories() throws SQLException, ClassNotFoundException {
        List<Category> categories = categoryDao.loadAll();
        return convertToDtoList(categories);
    }

    @Override
    public List<CategoryDto> searchCategories(String searchTerm) throws SQLException, ClassNotFoundException {
        List<Category> categories = categoryDao.searchCategories(searchTerm);
        return convertToDtoList(categories);
    }

    @Override
    public List<CategoryDto> getCategoriesByParent(String parentCategoryId) throws SQLException, ClassNotFoundException {
        List<Category> categories = categoryDao.findByParentCategory(parentCategoryId);
        return convertToDtoList(categories);
    }

    @Override
    public int getCategoryCount() throws SQLException, ClassNotFoundException {
        return categoryDao.getCategoryCount();
    }

    private CategoryDto convertToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getCategoryName(),
                category.getDescription(),
                category.getCategoryId(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    private List<CategoryDto> convertToDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(convertToDto(category));
        }
        return categoryDtos;
    }
}
