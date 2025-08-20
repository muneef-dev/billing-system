package com.example.billingsystem.bo.custom;

import com.example.billingsystem.bo.SuperBo;
import com.example.billingsystem.dto.StockMovementDto;

import java.sql.SQLException;
import java.util.List;

public interface StockMovementBo extends SuperBo {
    boolean createStockMovement(StockMovementDto stockMovementDto) throws SQLException, ClassNotFoundException;
    boolean updateStockMovement(StockMovementDto stockMovementDto) throws SQLException, ClassNotFoundException;
    boolean deleteStockMovement(String id) throws SQLException, ClassNotFoundException;
    StockMovementDto getStockMovement(String id) throws SQLException, ClassNotFoundException;
    List<StockMovementDto> getAllStockMovements() throws SQLException, ClassNotFoundException;
    List<StockMovementDto> getStockMovementsByItem(String itemId) throws SQLException, ClassNotFoundException;
    List<StockMovementDto> getStockMovementsByReason(String reason) throws SQLException, ClassNotFoundException;
}
