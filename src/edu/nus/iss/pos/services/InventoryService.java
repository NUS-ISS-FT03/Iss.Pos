package edu.nus.iss.pos.services;

import java.util.Collection;

import edu.nus.iss.pos.core.Category;
import edu.nus.iss.pos.core.Product;
import edu.nus.iss.pos.core.Vendor;
import edu.nus.iss.pos.core.dao.IRepository;
import edu.nus.iss.pos.core.dao.IUnitOfWork;
import edu.nus.iss.pos.core.services.IInventoryService;
import edu.nus.iss.pos.dao.format.RepoType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vishnu
 */
public class InventoryService implements IInventoryService {

    private final IUnitOfWork unitOfWork;
    
    public InventoryService(IUnitOfWork unitOfWork){
        if(unitOfWork == null) throw new IllegalArgumentException("unitOfWork");
        this.unitOfWork = unitOfWork;
    }
    
    /**
     * Adds a category
     * @param id
     * @param name
     * @return
     * @throws Exception 
     */
    @Override
    public Category addCategory(String id, String name) throws Exception  {
        Category category = new Category(id, name);
        try {
            unitOfWork.add(category);
            return category;
        } catch (Exception ex) {
            throw ex;
        }
        
    }

    /**
     * Deletes a category
     * @param categoryId
     * @throws Exception 
     */
    @Override
    public void deleteCategory(String categoryId) throws Exception {
       
        Category category = (Category) unitOfWork.getRepository(RepoType.Category).getByKey(categoryId);
        unitOfWork.delete(category);
    }

    /**
     * Adds a new product
     * @param category
     * @param name
     * @param description
     * @param availableQuantity
     * @param price
     * @param barcodeNumber
     * @param reorderQuantity
     * @param orderQuantity
     * @return
     * @throws Exception 
     */
    @Override
    public Product addProduct(Category category, String name, String description, int availableQuantity, float price,
                    String barcodeNumber, int reorderQuantity, int orderQuantity) throws Exception {

        
        Product product = new Product(category, getNewId(category), name, description, availableQuantity, price, barcodeNumber, reorderQuantity, orderQuantity);
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        repository.add(product);
        return product;
    }
    
    @Override
    public void updateProduct(Product product) throws Exception{
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        repository.update(product.getKey(), product);
    }
    
    @Override
    public void updateCategory(Category category) throws Exception {
        IRepository repository = unitOfWork.getRepository(RepoType.Category);
        repository.update(category.getKey(), category);
    }

    private int getNewId(Category c) throws Exception{
        int maxId = 0;
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        Iterable<Product> products =  repository.getAll();
        for(Product p : products) {
            if(p.getCategory().getKey().equals(c.getKey())){
                int index = Integer.parseInt(p.getKey().split("/")[1]);
                if(maxId < index){
                    maxId = index;
                }
            }
        }
        return maxId + 1;
    }
    
    /**
     * Deletes the product from the file of a particular product id
     * @param productId
     * @throws Exception 
     */
    @Override
    public void deleteProduct(String productId) throws Exception {
            
        Product product = (Product) unitOfWork.getRepository(RepoType.Product).getByKey(productId);
        unitOfWork.delete(product);
    }

    /**
     * 
     * @param product
     * @throws Exception 
     */
    @Override
    public void reorderProduct(Product product) throws Exception {
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        product.setQuantity(product.getQuantity() + product.getOrderQuantity());
        repository.update(product.getKey(), product);
    }

    /**
     * Will return a list of products on passing a string as input
     * @param name
     * @return Collection of Products
     * @throws Exception 
     */
    @Override
    public Collection<Product> searchProductByName(String name) throws Exception {
            
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        Iterable<Product> products = repository.getAll();
        List<Product> productList = new ArrayList();
        for(Product product : products){
            if(product.getName().contains(name)){
                productList.add(product);
            }              
        }    
        return productList;
    }

    /**
     * Searches a product by its bar code
     * @param barcode
     * @return
     * @throws Exception 
     */
    @Override
    public Product searchProductByBarcode(String barcode) throws Exception {
            
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        Iterable<Product> products = repository.getAll();
        for(Product product : products){
            if(product.getBarcodeNumber().equals(barcode)){
                return product;
            }              
        }    
        return null;
    }

    /**
     * Returns a list of product using quantity is below threshold quantity
     * @return
     * @throws Exception 
     */
    @Override
    public Collection<Product> getProductsBelowThreshold() throws Exception {
        
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        Iterable<Product> products = repository.getAll();
        List<Product> productList = new ArrayList<>();
        for(Product product : products){
            if(product.getQuantity() < product.getReorderQuantity()){
                productList.add(product);
            }
        }
        return productList;
    }

    /**
     * Returns a list of product having a particular category id
     * @param categoryId
     * @return
     * @throws Exception 
     */
    @Override
    public Collection<Product> getProductsByCategoryId(String categoryId) throws Exception {
        
        IRepository repository = unitOfWork.getRepository(RepoType.Product);
        Iterable<Product> products = repository.getAll();
        List<Product> productList = new ArrayList();
        for(Product product : products){
            if(product.getCategory().getKey().equals(categoryId)){
                productList.add(product);
            }
        }
        return productList;
    }
    
     @Override
    public Iterable<Category> getAllCategory() throws Exception{
        return unitOfWork.getRepository(RepoType.Category).getAll();
    }

    @Override
    public Collection<Vendor> getVendorsForCategory(String categoryId) throws Exception {
        Collection<Vendor> vendors = (Collection<Vendor>) unitOfWork.getRepository(RepoType.Vendor).getAll();
        Collection<Vendor> vendorsNeeded = new ArrayList<Vendor>();
        
        for(Vendor vendor : vendors){
            for(Category category : vendor.getCategories()){
                if(category.getKey().equals(categoryId)){
                    vendorsNeeded.add(vendor);
                }
            }
        }
        
        return vendorsNeeded;
        
    }

    @Override
    public Vendor addVendorForCategory(String name, String description, Category category) throws Exception {
        Vendor vendor = new Vendor(name, description);
        vendor.addCategory(category);
        IRepository repository = unitOfWork.getRepository(RepoType.Vendor);
        repository.add(vendor);
        return vendor;
    }
    
    
    public List<Vendor> getAllVendors() throws Exception {
        IRepository repository = unitOfWork.getRepository(RepoType.Vendor);
        List<Vendor> vendors = new ArrayList();
        for(Object vendor : repository.getAll())
            vendors.add((Vendor)vendor);
        return vendors;
    }
}
