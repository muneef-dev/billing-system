package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<Item, String> {
    Item findByItemCode(String itemCode) throws SQLException, ClassNotFoundException;
    List<Item> searchItems(String searchTerm) throws SQLException, ClassNotFoundException;
    boolean updateStock(String id, int quantity) throws SQLException, ClassNotFoundException;
    Item find(String id) throws SQLException, ClassNotFoundException;
    int getItemCount() throws SQLException, ClassNotFoundException;
}
