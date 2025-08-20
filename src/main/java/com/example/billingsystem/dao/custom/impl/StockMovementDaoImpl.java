package com.example.billingsystem.dao.custom.impl;

import com.example.billingsystem.dao.CrudUtil;
import com.example.billingsystem.dao.custom.StockMovementDao;
import com.example.billingsystem.entity.StockMovement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockMovementDaoImpl implements StockMovementDao {

    @Override
    public boolean create(StockMovement stockMovement) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO stock_movements (id, item_id, change_quantity, reason, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                stockMovement.getId(),
                stockMovement.getItemId(),
                stockMovement.getChangeQuantity(),
                stockMovement.getReason(),
                stockMovement.getCreatedAt(),
                stockMovement.getUpdatedAt()
        );
    }

    @Override
    public List<StockMovement> search(String s) throws SQLException, ClassNotFoundException {
        return List.of();
    }

    @Override
    public boolean update(StockMovement stockMovement) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE stock_movements SET item_id=?, change_quantity=?, reason=?, updated_at=? WHERE id=?",
                stockMovement.getItemId(),
                stockMovement.getChangeQuantity(),
                stockMovement.getReason(),
                stockMovement.getUpdatedAt(),
                stockMovement.getId()
        );
    }

    @Override
    public List<StockMovement> loadAll() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM stock_movements ORDER BY created_at DESC");
        return extractStockMovementsFromResultSet(resultSet);
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM stock_movements WHERE id=?", id);
    }

    @Override
    public StockMovement find(String id) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM stock_movements WHERE id=?", id);
        if (resultSet.next()) {
            return extractStockMovementFromResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<StockMovement> findByItemId(String itemId) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM stock_movements WHERE item_id=? ORDER BY created_at DESC", itemId);
        return extractStockMovementsFromResultSet(resultSet);
    }

    @Override
    public List<StockMovement> findByReason(String reason) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM stock_movements WHERE reason=? ORDER BY created_at DESC", reason);
        return extractStockMovementsFromResultSet(resultSet);
    }

    private StockMovement extractStockMovementFromResultSet(ResultSet resultSet) throws SQLException {
        return new StockMovement(
                resultSet.getString("id"),
                resultSet.getString("item_id"),
                resultSet.getInt("change_quantity"),
                resultSet.getString("reason"),
                resultSet.getTimestamp("created_at"),
                resultSet.getTimestamp("updated_at")
        );
    }

    private List<StockMovement> extractStockMovementsFromResultSet(ResultSet resultSet) throws SQLException {
        List<StockMovement> stockMovements = new ArrayList<>();
        while (resultSet.next()) {
            stockMovements.add(extractStockMovementFromResultSet(resultSet));
        }
        return stockMovements;
    }
}
