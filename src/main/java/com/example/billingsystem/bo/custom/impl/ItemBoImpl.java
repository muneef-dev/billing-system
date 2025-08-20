package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.ItemDao;
import com.example.billingsystem.dto.ItemDto;
import com.example.billingsystem.entity.Item;
import com.example.billingsystem.util.KeyGenerator;
import com.example.billingsystem.util.GeneratorUtil;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {

    private final ItemDao itemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ITEM);

    @Override
    public boolean createItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        // Generate ID if not provided
        if (itemDto.getId() == null || itemDto.getId().trim().isEmpty()) {
            itemDto.setId(KeyGenerator.generateId());
        }

        // Generate item code if not provided
        if (itemDto.getItemCode() == null || itemDto.getItemCode().trim().isEmpty()) {
            itemDto.setItemCode(GeneratorUtil.generateItemCode());
        }

        return itemDao.create(new Item(
                itemDto.getId(),
                itemDto.getItemCode(),
                itemDto.getItemName(),
                itemDto.getCategory(),
                itemDto.getAuthor(),
                itemDto.getPublisher(),
                itemDto.getDescription(),
                itemDto.getCoverImageUrl(),
                itemDto.getUnitPrice(),
                itemDto.getCostPrice(),
                itemDto.getStockQuantity(),
                itemDto.getMinimumStockLevel(),
                itemDto.isActive(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                itemDto.getId(),
                itemDto.getItemCode(),
                itemDto.getItemName(),
                itemDto.getCategory(),
                itemDto.getAuthor(),
                itemDto.getPublisher(),
                itemDto.getDescription(),
                itemDto.getCoverImageUrl(),
                itemDto.getUnitPrice(),
                itemDto.getCostPrice(),
                itemDto.getStockQuantity(),
                itemDto.getMinimumStockLevel(),
                itemDto.isActive(),
                itemDto.getCreatedAt(),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return itemDao.delete(id);
    }

    @Override
    public ItemDto getItem(String id) throws SQLException, ClassNotFoundException {
        Item item = itemDao.find(id);
        return item != null ? convertToDto(item) : null;
    }

    @Override
    public ItemDto getItemByCode(String itemCode) throws SQLException, ClassNotFoundException {
        Item item = itemDao.findByItemCode(itemCode);
        return item != null ? convertToDto(item) : null;
    }

    @Override
    public List<ItemDto> searchItems(String searchTerm) throws SQLException, ClassNotFoundException {
        List<Item> items = itemDao.searchItems(searchTerm);
        return convertToDtoList(items);
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        return itemDao.loadAll().size();
    }

    @Override
    public List<ItemDto> getAllItems() throws SQLException, ClassNotFoundException {
        List<Item> items = itemDao.loadAll();
        return convertToDtoList(items);
    }

    @Override
    public boolean updateStock(String id, int quantity) throws SQLException, ClassNotFoundException {
        return itemDao.updateStock(id, quantity);
    }

    private ItemDto convertToDto(Item item) {
        return new ItemDto(
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

    private List<ItemDto> convertToDtoList(List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(convertToDto(item));
        }
        return itemDtos;
    }
}
