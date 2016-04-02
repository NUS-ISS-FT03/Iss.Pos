/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.nus.iss.pos.core.services;

import edu.nus.iss.pos.core.Member;
import edu.nus.iss.pos.core.Product;
import edu.nus.iss.pos.core.Transaction;
import edu.nus.iss.pos.core.TransactionDetail;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Liu Zhenchang
 */
public interface ISalesService {
    
    Transaction beginTransaction(Member member) throws Exception;
    
    TransactionDetail addToCart(Transaction transaction, Product product, int quantity) throws Exception;
    
    void checkout(Transaction transaction, float discount, boolean useLoyaltyPoints) throws Exception;
    
    float getFinalPrice(Transaction transaction,float discount, boolean useLoyaltyPoints) throws Exception;
    
    public float getPriceAfterDiscount(Transaction transaction, float discount) throws Exception;
    
    public List<Transaction> getTransactions(Date startDate, Date endDate) throws Exception;
}
