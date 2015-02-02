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

import java.util.ArrayList;

public class Claim {
    // If empty values are passed to the setter methods, any attributes that
    // have defaults will be set to them.
    private static final String default_status = "In Progress";
    private static final String default_description = "(no description)";
    private static final String no_expenses = "(no expenses)";
    
    private String name;
    private String status;
    private ClaimDate start_date;
    private ClaimDate end_date;
    private String description;
    
    private Expense current_expense;
    private ArrayList<Expense> expenses;
    
    public Claim() {
        super();
        
        name = null;
        status = Claim.default_status;
        start_date = new ClaimDate();
        end_date = new ClaimDate();
        description = Claim.default_description;
        
        current_expense = null;
        expenses = new ArrayList<Expense>();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setStatus(String status) {
        if (status.isEmpty()) {
            this.status = Claim.default_status;
        } else {
            this.status = status;
        }
    }
    public String getStatus() {
        return status;
    }
    
    public void setStartDate(String date) {
        start_date.setDate(date);
    }
    
    public String getStartDate() {
        return start_date.getDate();
    }
    
    public void setEndDate(String date) {
        end_date.setDate(date);
    }
    
    public String getEndDate() {
        return end_date.getDate();
    }
    
    public void setDescription(String description) {
        if (description.isEmpty()) {
            this.description = Claim.default_description;
        } else {
            this.description = description;
        }
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setCurrentExpense(Expense expense) {
        current_expense = expense;
    }
    
    public Expense getCurrentExpense() {
        return current_expense;
    }
    
    public void deleteExpense(Expense expense) {
        expenses.remove(expense);
    }
    
    // Called whether a new expense needs to be inserted or an existing one has its
    // information updated. The expenses are sorted by decreasing order based on their
    // date attributes.
    public void addExpense(Expense expense) {
        Expense next_expense = expense;
        expenses.remove(expense);
        int size = expenses.size();
        
        for (int i = 0; i <= size; i++) {
            expense = next_expense;
            if (i == size) {
                expenses.add(expense);
                break;
            }
            
            next_expense = expenses.get(i);
            if (ClaimDate.lesserDate(expense.getDate(), next_expense.getDate())) {
                next_expense = expense;
            } else {
                expenses.set(i, expense);
            }
        }
    }
    
    public ArrayList<Expense> getExpenses() {
        return expenses;
    }
    
    // Will go through all of the expenses in the claim (if any) and calculate the
    // totals for each currency type. Only returns totals of types that at least
    // one of its expenses has.
    public String getAmountTotals() {
        if (expenses.isEmpty()) {
            return "\n\t" + no_expenses;
        }
        
        ArrayList<Double> amounts = new ArrayList<Double>();
        ArrayList<Boolean> have_amounts = new ArrayList<Boolean>();
        ArrayList<String> types = new ArrayList<String>();
        
        int num_types = 4;
        types.add(TravelClaimController.CURRENCY1);
        types.add(TravelClaimController.CURRENCY2);
        types.add(TravelClaimController.CURRENCY3);
        types.add(TravelClaimController.CURRENCY4);
        
        // Initialization.
        Double default_double = Double.valueOf(0);
        for (int i = 0; i < num_types; i++) {
            amounts.add(default_double);
            have_amounts.add(false);
        }
        
        String currency;
        Double new_amount;
        
        // Add the amounts up for each type.
        for (Expense expense : expenses) {
            currency = expense.getCurrency();
            for (int i = 0; i < num_types; i++) {
                if (currency.equals(types.get(i))) {
                    new_amount = amounts.get(i) + expense.getAmount();
                    amounts.set(i, new_amount);
                    have_amounts.set(i, true);
                    break;
                }
            }
        }

        String return_value = "";
        String next_amount;

        // Concatenate the type and total to the return value if applicable.
        for (int i = 0; i < num_types; i++) {
            if (have_amounts.get(i)) {
                next_amount = "\n\t" + types.get(i) + "\t " + String.valueOf(amounts.get(i));
                return_value = return_value.concat(next_amount);
            }
        }
        
        return return_value;
    }
    
    // Will be called for use in a ListView, so the description can be cut out here.
    @Override
    public String toString() {
        return "NAME:\t\t " + getName() + "\n" +
               "STATUS:\t " + getStatus() + "\n" +
               "PERIOD:\t " + getStartDate() + " - " + getEndDate() +
               getAmountTotals();
    }
    
    // An extra method for when we also need the description.
    public String toStringWDescription() {
        return "NAME:\t\t" + getName() + "\n" +
               "STATUS:\t" + getStatus() + "\n" +
               "PERIOD:\t" + getStartDate() + " - " + getEndDate() +
               getAmountTotals() + "\n" +
               getDescription();
    }
}
