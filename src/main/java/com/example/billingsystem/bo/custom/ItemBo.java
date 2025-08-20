package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemBo extends SuperBo {
    boolean createItem(ItemDto itemDto) throws SQLException, ClassNotFoundException;
    boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    ItemDto getItem(String id) throws SQLException, ClassNotFoundException;
    List<ItemDto> getAllItems() throws SQLException, ClassNotFoundException;
    List<ItemDto> searchItems(String searchTerm) throws SQLException, ClassNotFoundException;
    int getItemCount() throws SQLException, ClassNotFoundException;
    ItemDto getItemByCode(String itemCode) throws SQLException, ClassNotFoundException;
    boolean updateStock(String id, int quantity) throws SQLException, ClassNotFoundException;
}
