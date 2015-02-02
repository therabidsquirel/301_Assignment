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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

// For displaying all of the information of an expense, and letting a user
// decide to either edit the information or delete the expense.
public class ExpenseInfoActivity extends ClaimActivity {
    private Expense expense;
    
    private TextView expenseAttributes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_info);
        
        expense = TravelClaimController.getCurrentClaim().getCurrentExpense();
        
        expenseAttributes = (TextView) findViewById(R.id.expenseAttributes);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        setExpenseAttributes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.expense_info, menu);
        return true;
    }

    // Only allow editing of expense if parent claim is editable.
    public void editExpense(MenuItem menu) {
        if (TravelClaimController.canEditCurrentClaim()) {
            Intent intent = new Intent(ExpenseInfoActivity.this, EditExpenseActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, TravelClaimController.editRejection(), Toast.LENGTH_SHORT).show();
        }
    }
    
    // Only allow deletion of expense if parent claim is editable.
    public void deleteExpense(MenuItem menu) {
        if (TravelClaimController.canEditCurrentClaim()) {
            TravelClaimController.getCurrentClaim().deleteExpense(expense);
            finish();
        } else {
            Toast.makeText(this, TravelClaimController.editRejection(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setExpenseAttributes() {
        expenseAttributes.setText(expense.toStringWDescription());
    }
}
