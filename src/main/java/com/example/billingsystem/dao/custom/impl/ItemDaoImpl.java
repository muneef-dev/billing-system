package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.ItemDao;
import com.example.billingsystem.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemDaoImpl implements ItemDao {

    @Override
    public boolean create(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO items (id, item_code, item_name, category, author, publisher, description, cover_image_url, unit_price, cost_price, stock_quantity, minimum_stock_level, is_active, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",

                item.getId(),
                item.getItemCode(),
                item.getItemName(),
                item.getCategory(),
                item.getAuthor(),
                item.getPublisher(),
                item.getDescription(),
                item.getCoverImageUrl(),
                item.getUnitPrice(),
                item.getCostPrice(),
                item.getStockQuantity(),
                item.getMinimumStockLevel(),
                item.isActive(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    @Override
    public List<Item> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE items SET item_code=?, item_name=?, category=?, author=?, publisher=?, description=?, cover_image_url=?, unit_price=?, cost_price=?, stock_quantity=?, minimum_stock_level=?, is_active=?, updated_at=? WHERE id=?",
                item.getItemCode(),
                item.getItemName(),
                item.getCategory(),
                item.getAuthor(),
                item.getPublisher(),
                item.getDescription(),
                item.getCoverImageUrl(),
                item.getUnitPrice(),
                item.getCostPrice(),
                item.getStockQuantity(),
                item.getMinimumStockLevel(),
                item.isActive(),
                item.getUpdatedAt(),
                item.getId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM items WHERE id=?", id);
    }

    @Override
    public Item find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM items WHERE id=?", id);
        if (resultSet.next()) {
            return extractItemFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Item> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM items ORDER BY item_name");
        return extractItemsFromResultSet(resultSet);
    }

    @Override
    public Item findByItemCode(String itemCode) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM items WHERE item_code=?", itemCode);
        if (resultSet.next()) {
            return extractItemFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Item> searchItems(String searchTerm) throws SQLException, ClassNotFoundException {
        searchTerm = "%" + searchTerm + "%";
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM items WHERE (item_name LIKE ? OR item_code LIKE ? OR category LIKE ? OR author LIKE ?) AND is_active=true ORDER BY item_name",
                searchTerm, searchTerm, searchTerm, searchTerm);
        return extractItemsFromResultSet(resultSet);
    }

    @Override
    public boolean updateStock(String id, int quantity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE items SET stock_quantity = stock_quantity + ? WHERE id = ?",
                quantity, id
        );
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT COUNT(*) FROM items");
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private Item extractItemFromResultSet(ResultSet rs) throws SQLException {
        return new Item(
                rs.getString("id"),
                rs.getString("item_code"),
                rs.getString("item_name"),
                rs.getString("category"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getString("description"),
                rs.getString("cover_image_url"),
                rs.getBigDecimal("unit_price"),
                rs.getBigDecimal("cost_price"),
                rs.getInt("stock_quantity"),
                rs.getInt("minimum_stock_level"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }

    private List<Item> extractItemsFromResultSet(ResultSet rs) throws SQLException {
        List<Item> items = new ArrayList<>();
        while (rs.next()) {
            items.add(extractItemFromResultSet(rs));
        }
        return items;
    }
}
