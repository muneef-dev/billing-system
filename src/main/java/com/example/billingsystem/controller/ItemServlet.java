package com.example.billingsystem.controller;

import com.example.billingsystem.bo.BoFactory;
import com.example.billingsystem.bo.custom.ItemBo;
import com.example.billingsystem.dto.ItemDto;
import com.example.billingsystem.util.KeyGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ItemServlet", urlPatterns = {"/items/*"})
public class ItemServlet extends HttpServlet {
    private final ItemBo itemBo = BoFactory.getInstance().getBo(BoFactory.BoType.ITEM);
    private static final Logger logger = Logger.getLogger(ItemServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleListItems(request, response);
            } else if (pathInfo.equals("/new")) {
                handleNewItemForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleEditItemForm(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/search")) {
                handleSearchItems(request, response);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in ItemServlet doGet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                handleCreateItem(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                handleUpdateItem(request, response, pathInfo.substring(6));
            } else if (pathInfo.startsWith("/delete/")) {
                handleDeleteItem(request, response, pathInfo.substring(8));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in ItemServlet doPost", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleListItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<ItemDto> items = itemBo.getAllItems();
        request.setAttribute("items", items);
        request.getRequestDispatcher("/WEB-INF/views/item/list.jsp").forward(request, response);
    }

    private void handleNewItemForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.getRequestDispatcher("/WEB-INF/views/item/form.jsp").forward(request, response);
    }

    private void handleEditItemForm(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        ItemDto item = itemBo.getItem(id);
        if (item != null) {
            request.setAttribute("item", item);
            request.getRequestDispatcher("/WEB-INF/views/item/form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleSearchItems(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String term = request.getParameter("term");
        if (term != null && !term.trim().isEmpty()) {
            List<ItemDto> items = itemBo.searchItems(term.trim());
            request.setAttribute("items", items);
            request.setAttribute("searchTerm", term);
        }
        request.getRequestDispatcher("/WEB-INF/views/item/list.jsp").forward(request, response);
    }

    private void handleCreateItem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(KeyGenerator.generateId());
        itemDto.setItemCode(request.getParameter("itemCode").trim());
        itemDto.setName(request.getParameter("name").trim());
        itemDto.setDescription(request.getParameter("description").trim());
        itemDto.setPrice(new BigDecimal(request.getParameter("price").trim()));
        itemDto.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity").trim()));

        if (itemBo.createItem(itemDto)) {
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            request.setAttribute("error", "Failed to create item");
            request.setAttribute("item", itemDto);
            handleNewItemForm(request, response);
        }
    }

    private void handleUpdateItem(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        ItemDto itemDto = itemBo.getItem(id);
        if (itemDto != null) {
            itemDto.setItemCode(request.getParameter("itemCode").trim());
            itemDto.setName(request.getParameter("name").trim());
            itemDto.setDescription(request.getParameter("description").trim());
            itemDto.setPrice(new BigDecimal(request.getParameter("price").trim()));
            itemDto.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity").trim()));

            if (itemBo.updateItem(itemDto)) {
                response.sendRedirect(request.getContextPath() + "/items");
            } else {
                request.setAttribute("error", "Failed to update item");
                request.setAttribute("item", itemDto);
                handleEditItemForm(request, response, id);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleDeleteItem(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
        if (itemBo.deleteItem(id)) {
            response.sendRedirect(request.getContextPath() + "/items");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete item");
        }
    }
}
