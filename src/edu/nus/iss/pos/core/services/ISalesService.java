/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.pos.core.services;

import edu.nus.iss.pos.core.Customer;
import edu.nus.iss.pos.core.Product;
import edu.nus.iss.pos.core.Transaction;
import edu.nus.iss.pos.core.TransactionDetail;

/**
 *
 * @author Liu Zhenchang
 */
public interface ISalesService {
    
    Transaction beginTransaction(Customer customer) throws Exception;
    
    TransactionDetail addToCart(Transaction transaction, Product product, int quantity) throws Exception;
    
    void checkout(Transaction transaction, int discount, boolean useLoyaltyPoints) throws Exception;
    
    float getFinalPrice(Transaction transaction,int discount, boolean useLoyaltyPoints) throws Exception;
    
    public float getPriceAfterDiscount(Transaction transaction, int discount) throws Exception;
}
