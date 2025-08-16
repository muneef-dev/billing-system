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
        return CrudUtil.execute("INSERT INTO items (id, item_code, name, description, price, stock_quantity, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                item.getId(),
                item.getItemCode(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getStockQuantity(),
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
        return CrudUtil.execute("UPDATE items SET item_code=?, name=?, description=?, price=?, stock_quantity=?, updated_at=? WHERE id=?",
                item.getItemCode(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getStockQuantity(),
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
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM items ORDER BY name");
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
        ResultSet resultSet = CrudUtil.execute(
                "SELECT * FROM items WHERE name LIKE ? OR item_code LIKE ? OR description LIKE ?",
                searchTerm, searchTerm, searchTerm
        );
        return extractItemsFromResultSet(resultSet);
    }

    @Override
    public boolean updateStock(String id, int quantity) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE items SET stock_quantity = stock_quantity + ? WHERE id = ?",
                quantity, id
        );
    }

    private Item extractItemFromResultSet(ResultSet rs) throws SQLException {
        return new Item(
                rs.getString("id"),
                rs.getString("item_code"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock_quantity"),
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
