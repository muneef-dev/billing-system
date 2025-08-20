package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.CategoryDao;
import com.example.billingsystem.entity.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public boolean create(Category category) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO categories (id, category_name, description, category_id, is_active, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                category.getId(),
                category.getCategoryName(),
                category.getDescription(),
                category.getCategoryId(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    @Override
    public List<Category> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Category category) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE categories SET category_name=?, description=?, category_id=?, is_active=?, updated_at=? WHERE id=?",
                category.getCategoryName(),
                category.getDescription(),
                category.getCategoryId(),
                category.isActive(),
                category.getUpdatedAt(),
                category.getId()
        );
    }

    @Override
    public List<Category> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM categories ORDER BY category_name");
        return extractCategoriesFromResultSet(resultSet);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM categories WHERE id=?", id);
    }

    @Override
    public Category find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM categories WHERE id=?", id);
        if (resultSet.next()) {
            return extractCategoryFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Category> findByParentCategory(String parentCategoryId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM categories WHERE category_id=?", parentCategoryId);
        return extractCategoriesFromResultSet(resultSet);
    }

    @Override
    public List<Category> searchCategories(String searchTerm) throws SQLException, ClassNotFoundException {
        searchTerm = "%" + searchTerm + "%";
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM categories WHERE category_name LIKE ? OR description LIKE ?",
                searchTerm, searchTerm
        );
        return extractCategoriesFromResultSet(resultSet);
    }

    @Override
    public int getCategoryCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM categories");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private Category extractCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        return new Category(
                resultSet.getString("id"),
                resultSet.getString("category_name"),
                resultSet.getString("description"),
                resultSet.getString("category_id"),
                resultSet.getBoolean("is_active"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at")
        );
    }

    private List<Category> extractCategoriesFromResultSet(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();
        while (resultSet.next()) {
            categories.add(extractCategoryFromResultSet(resultSet));
        }
        return categories;
    }
}
