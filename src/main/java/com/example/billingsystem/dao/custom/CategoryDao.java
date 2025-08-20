package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao extends CrudDao<Category, String> {
    List<Category> findByParentCategory(String parentCategoryId) throws SQLException, ClassNotFoundException;
    List<Category> searchCategories(String searchTerm) throws SQLException, ClassNotFoundException;
    Category find(String id) throws SQLException, ClassNotFoundException;
    int getCategoryCount() throws SQLException, ClassNotFoundException;
}
