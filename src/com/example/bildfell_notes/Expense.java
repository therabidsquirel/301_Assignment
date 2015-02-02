/*
 *   Copyright 2015 Stuart Bildfell
 *   
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   
 *       http://www.apache.org/licenses/LICENSE-2.0
 *   
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.example.bildfell_notes;

public class Expense {
    // If empty values are passed to the setter methods, any attributes that
    // have defaults will be set to them.
    private static final String default_category = "(no category)";
    private static final Double default_amount = Double.valueOf(0);
    private static final String default_description = "(no description)";
    
    private String name;
    private String category;
    private ClaimDate date;
    private String currency;
    private Double amount;
    private String description;

    public Expense() {
        super();
        
        name = null;
        category = Expense.default_category;
        date = new ClaimDate();
        currency = null;
        amount = Expense.default_amount;
        description = Expense.default_description;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setCategory(String category) {
        if (category.isEmpty()) {
            this.category = Expense.default_category;
        } else {
            this.category = category;
        }
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setDate(String date) {
        this.date.setDate(date);
    }
    
    public String getDate() {
        return date.getDate();
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setAmount(String amount) {
        if (amount.isEmpty()) {
            this.amount = Expense.default_amount;
        } else {
            this.amount = Double.valueOf(amount);
        }
    }

    public Double getAmount() {
        return amount;
    }
    
    public void setDescription(String description) {
        if (description.isEmpty()) {
            this.description = Expense.default_description;
        } else {
            this.description = description;
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    // Will be called for use in a ListView, so the description can be cut out here.
    @Override
    public String toString() {
        return "NAME:\t" + getName() + "\n" +
               "TYPE:\t\t" + getCategory() + "\n" +
               "DATE:\t\t" + getDate() + "\n" +
               "\t" + getCurrency() + "\t " + getAmount();
    }
    
    // An extra method for when we also need the description.
    public String toStringWDescription() {
        return "NAME:\t" + getName() + "\n" +
               "TYPE:\t\t" + getCategory() + "\n" +
               "DATE:\t\t" + getDate() + "\n" +
               "\t" + getCurrency() + "\t " + getAmount() + "\n" +
               getDescription();
    }
}
