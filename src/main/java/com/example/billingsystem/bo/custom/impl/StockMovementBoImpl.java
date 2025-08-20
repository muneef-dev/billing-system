package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.StockMovementBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.StockMovementDao;
import com.example.billingsystem.dto.StockMovementDto;
import com.example.billingsystem.entity.StockMovement;
import com.example.billingsystem.util.KeyGenerator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StockMovementBoImpl implements StockMovementBo {

    private final StockMovementDao stockMovementDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.STOCK_MOVEMENT);

    @Override
    public boolean createStockMovement(StockMovementDto stockMovementDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (stockMovementDto.getId() == null || stockMovementDto.getId().trim().isEmpty()) {
            stockMovementDto.setId(KeyGenerator.generateId());
        }

        // Validate reason enum values according to schema: ('Sale','Return','Adjustment','Purchase')
        if (stockMovementDto.getReason() != null) {
            String reason = stockMovementDto.getReason();
            if (!reason.equals("Sale") && !reason.equals("Return") &&
                    !reason.equals("Adjustment") && !reason.equals("Purchase")) {
                throw new IllegalArgumentException("Invalid stock movement reason. Must be 'Sale', 'Return', 'Adjustment', or 'Purchase'");
            }
        } else {
            throw new IllegalArgumentException("Stock movement reason is required");
        }

        return stockMovementDao.create(new StockMovement(
                stockMovementDto.getId(),
                stockMovementDto.getItemId(),
                stockMovementDto.getChangeQuantity(),
                stockMovementDto.getReason(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updateStockMovement(StockMovementDto stockMovementDto) throws SQLException, ClassNotFoundException {
        return stockMovementDao.update(new StockMovement(
                stockMovementDto.getId(),
                stockMovementDto.getItemId(),
                stockMovementDto.getChangeQuantity(),
                stockMovementDto.getReason(),
                stockMovementDto.getCreatedAt(),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean deleteStockMovement(String id) throws SQLException, ClassNotFoundException {
        return stockMovementDao.delete(id);
    }

    @Override
    public StockMovementDto getStockMovement(String id) throws SQLException, ClassNotFoundException {
        StockMovement stockMovement = stockMovementDao.find(id);
        return stockMovement != null ? convertToDto(stockMovement) : null;
    }

    @Override
    public List<StockMovementDto> getAllStockMovements() throws SQLException, ClassNotFoundException {
        List<StockMovement> stockMovements = stockMovementDao.loadAll();
        return convertToDtoList(stockMovements);
    }

    @Override
    public List<StockMovementDto> getStockMovementsByItem(String itemId) throws SQLException, ClassNotFoundException {
        List<StockMovement> stockMovements = stockMovementDao.findByItemId(itemId);
        return convertToDtoList(stockMovements);
    }

    @Override
    public List<StockMovementDto> getStockMovementsByReason(String reason) throws SQLException, ClassNotFoundException {
        List<StockMovement> stockMovements = stockMovementDao.findByReason(reason);
        return convertToDtoList(stockMovements);
    }

    private StockMovementDto convertToDto(StockMovement stockMovement) {
        return new StockMovementDto(
                stockMovement.getId(),
                stockMovement.getItemId(),
                stockMovement.getChangeQuantity(),
                stockMovement.getReason(),
                stockMovement.getCreatedAt(),
                stockMovement.getUpdatedAt()
        );
    }

    private List<StockMovementDto> convertToDtoList(List<StockMovement> stockMovements) {
        List<StockMovementDto> stockMovementDtos = new ArrayList<>();
        for (StockMovement stockMovement : stockMovements) {
            stockMovementDtos.add(convertToDto(stockMovement));
        }
        return stockMovementDtos;
    }
}
