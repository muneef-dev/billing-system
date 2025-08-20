package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.CategoryDto;

import java.sql.SQLException;
import java.util.List;

public interface CategoryBo extends SuperBo {
    boolean createCategory(CategoryDto categoryDto) throws SQLException, ClassNotFoundException;
    boolean updateCategory(CategoryDto categoryDto) throws SQLException, ClassNotFoundException;
    boolean deleteCategory(String id) throws SQLException, ClassNotFoundException;
    CategoryDto getCategory(String id) throws SQLException, ClassNotFoundException;
    List<CategoryDto> getAllCategories() throws SQLException, ClassNotFoundException;
    List<CategoryDto> searchCategories(String searchTerm) throws SQLException, ClassNotFoundException;
    List<CategoryDto> getCategoriesByParent(String parentCategoryId) throws SQLException, ClassNotFoundException;
    int getCategoryCount() throws SQLException, ClassNotFoundException;
}
