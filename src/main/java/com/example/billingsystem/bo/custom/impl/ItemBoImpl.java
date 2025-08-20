package com.example.billingsystem.bo.custom.impl;

import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dao.DaoFactory;
import com.example.billingsystem.dao.custom.ItemDao;
import com.example.billingsystem.dto.ItemDto;
import com.example.billingsystem.entity.Item;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {

    private final ItemDao itemDao = DaoFactory.getInstance().getDao(DaoFactory.DaoType.ITEM);

    @Override
    public boolean createItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return itemDao.create(new Item(
                itemDto.getId(),
                itemDto.getItemCode(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getPrice(),
                itemDto.getStockQuantity(),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        ));
    }

    @Override
    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                itemDto.getId(),
                itemDto.getItemCode(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getPrice(),
                itemDto.getStockQuantity(),
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
        return 0;
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
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getStockQuantity(),
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
