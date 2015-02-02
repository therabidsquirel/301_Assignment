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

// All methods and attributes in this class are static, as it keeps a list of claims
// and all the respective information for the entire app.
public class TravelClaimController {
    // Presets for Claim.status and Expense.currency.
    public static final String STATUS1 = "In Progress";
    public static final String STATUS2 = "Submitted";
    public static final String STATUS3 = "Returned";
    public static final String STATUS4 = "Approved";
    public static final String CURRENCY1 = "CAD";
    public static final String CURRENCY2 = "USD";
    public static final String CURRENCY3 = "EUR";
    public static final String CURRENCY4 = "GBP";
    
    private static final String FILENAME = "data.sav";
    private static final String REJECTION = "Can't edit claim under its current status!";
    
    // current_claim is set whenever one activity has to switch to another activity, and
    // the new activity has to be created using information from a specific Claim object.
    private static Claim current_claim = null;
    private static ArrayList<Claim> claims = new ArrayList<Claim>();
    
    public static String getFile() {
        return FILENAME;
    }
    
    public static String editRejection() {
        return REJECTION;
    }
    
    public static void setCurrentClaim(Claim claim) {
        current_claim = claim;
    }
    
    public static Claim getCurrentClaim() {
        return current_claim;
    }

    // Can't edit claims of certain statuses.
    public static boolean canEditCurrentClaim() {
        if (current_claim == null) {
            return true;
        } else if (current_claim.getStatus().equals(STATUS2) || current_claim.getStatus().equals(STATUS4)) {
            return false;
        } else {
            return true;
        }
    }
    
    public static void deleteClaim(Claim claim) {
        claims.remove(claim);
    }
    
    // Called whether a new claim needs to be inserted or an existing one has its
    // information updated. The claims are sorted by decreasing order based on their
    // start_date attributes.
    public static void addClaim(Claim claim) {
        Claim next_claim = claim;
        claims.remove(claim);
        int size = claims.size();
        
        for (int i = 0; i <= size; i++) {
            claim = next_claim;
            if (i == size) {
                claims.add(claim);
                break;
            }
            
            next_claim = claims.get(i);
            if (ClaimDate.lesserDate(claim.getStartDate(), next_claim.getStartDate())) {
                next_claim = claim;
            } else {
                claims.set(i, claim);
            }
        }
    }
    
    public static void setClaims(ArrayList<Claim> newClaimsList) {
        claims = newClaimsList;
    }
    
    public static ArrayList<Claim> getClaims() {
        return claims;
    }
    
    // Will return one big String that is all of the information for the specified
    // claim and its expenses (if any).
    public static String getEmailText(Claim claim) {
        String email = claim.toStringWDescription() + "\n\n\n\n";
        ArrayList<Expense> expenses = claim.getExpenses();
        
        if (expenses.isEmpty()) {
            email = email.concat("No expenses for this claim.\n\n");
        }
        
        else {
            email = email.concat("Expenses for this claim:\n\n");
            String next_amount;
            
            for (Expense expense : expenses) {
                next_amount = expense.toStringWDescription() + "\n\n";
                email = email.concat(next_amount);
            }
        }
        
        return email;
    }
}
