/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.pos.dao.format;

import edu.nus.iss.pos.core.Category;
import edu.nus.iss.pos.core.Product;
import edu.nus.iss.pos.core.dao.IFileFormatter;
import edu.nus.iss.pos.core.dao.IUnitOfWork;

/**
 *
 * @author zz
 */
public class ProductFileFormatter implements IFileFormatter<Product>{
    
    private static final ProductFileFormatter singleton = new ProductFileFormatter();
    private static IUnitOfWork unitOfWork;
    
    private ProductFileFormatter(){}
    
    public static ProductFileFormatter getInstance(IUnitOfWork unitOfWork){
        ProductFileFormatter.unitOfWork = unitOfWork;
        return singleton;
    }

    @Override
    public String format(Product entity) {
        return entity.getKey() + "," +
                entity.getName() + "," + 
                entity.getDescription() + "," +
                entity.getQuantity() + "," +
                entity.getPrice() + "," +
                entity.getBarcodeNumber() + "," + 
                entity.getReorderQuantity() + "," + 
                entity.getOrderQuantity() + "\n";
    }

    @Override
    public Product readEntity(String data) throws Exception {
        String lines[] = data.split("\n");
        if(lines.length == 0) throw new Exception("Cannot map the data to entity!");
        String param[] = lines[0].split(",");
        if(param.length != 8) throw new Exception("Cannot map the data to entity!");
        String key = param[0];
        String categoryId = key.split("/")[0];
        int index = Integer.parseInt(key.split("/")[1]);
        Category c = (Category) unitOfWork.getRepository(RepoType.Category).getByKey(categoryId);
        String name = param[1];
        String description = param[2];
        int quantity = Integer.parseInt(param[3]);
        float price= Float.parseFloat(param[4]);
        String barcodeNumber = param[5];
        int recordQuantity = Integer.parseInt(param[6]);
        int orderQuantity = Integer.parseInt(param[7]);
        return new Product(c, index, name, description, quantity, price, barcodeNumber, recordQuantity, orderQuantity);
    }
    
    @Override
    public String getKey(String data) throws Exception{
        int pos = data.indexOf(",");
        if(pos < 1) throw new Exception("No key found!"); 
        String key = data.substring(0, pos);
        return key;
    }
    
}
