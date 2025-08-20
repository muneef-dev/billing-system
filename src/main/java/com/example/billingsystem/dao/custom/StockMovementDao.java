package com.example.billingsystem.dao.custom;

import com.example.billingsystem.dao.CrudDao;
import com.example.billingsystem.entity.StockMovement;

import java.sql.SQLException;
import java.util.List;

public interface StockMovementDao extends CrudDao<StockMovement, String> {
    List<StockMovement> findByItemId(String itemId) throws SQLException, ClassNotFoundException;
    List<StockMovement> findByReason(String reason) throws SQLException, ClassNotFoundException;
    StockMovement find(String id) throws SQLException, ClassNotFoundException;
}
