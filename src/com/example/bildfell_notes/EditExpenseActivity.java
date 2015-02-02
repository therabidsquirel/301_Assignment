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

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditExpenseActivity extends ClaimActivity {
    private Expense expense;
    private boolean new_expense;
    
    private EditText name_field;
    private EditText category_field;
    private EditText date_field;
    private EditText currency_field;
    private EditText amount_field;
    private EditText description_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);
        
        expense = TravelClaimController.getCurrentClaim().getCurrentExpense();
        
        name_field = (EditText) findViewById(R.id.expNameField);
        category_field = (EditText) findViewById(R.id.categoryField);
        date_field = (EditText) findViewById(R.id.dateField);
        currency_field = (EditText) findViewById(R.id.currencyField);
        amount_field = (EditText) findViewById(R.id.amountField);
        description_field = (EditText) findViewById(R.id.expDescriptionField);
        
        // expense will be null if we came here from wanting to add a new one. Otherwise
        // we're here because we're editing an existing one.
        if (expense == null) {
            expense = new Expense();
            new_expense = true;
            this.setTitle(R.string.title_activity_add_expense);
        } else {
            new_expense = false;
            this.setTitle(R.string.title_activity_edit_expense);
        }
        
        // Taken on February 1, 2015 from:
        // http://stackoverflow.com/questions/6070805/prevent-enter-key-on-edittext-but-still-show-the-text-as-multi-line
        description_field.setOnKeyListener(new View.OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // Disables the enter key in this EditText.
                return (keyCode == KeyEvent.KEYCODE_ENTER);
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        setFieldsFromExpense();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_expense, menu);
        return true;
    }

    // When the save button is hit, check each field in order to ensure everything is
    // of valid type. Show the user a toast message describing any error that occurred.
    // If no error, update the expense and exit out of this view.
    public void onSave(View v) {
        String name = name_field.getText().toString();
        String category = category_field.getText().toString();
        String date = date_field.getText().toString();
        String currency = currency_field.getText().toString();
        String amount = amount_field.getText().toString();
        String description = description_field.getText().toString();
        
        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ClaimDate.invalidDate(date)) {
            Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        ArrayList<String> presets = new ArrayList<String>();
        presets.add(TravelClaimController.CURRENCY1);
        presets.add(TravelClaimController.CURRENCY2);
        presets.add(TravelClaimController.CURRENCY3);
        presets.add(TravelClaimController.CURRENCY4);
        
        boolean valid_currency = false;
        for (String preset : presets) {
            if (currency.equals(preset)) {
                valid_currency = true;
                break;
            }
        }

        if (!valid_currency) {
            Toast.makeText(this, "Invalid currency!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        expense.setName(name);
        expense.setCategory(category);
        expense.setDate(date);
        expense.setCurrency(currency);
        expense.setAmount(amount);
        expense.setDescription(description);
        
        TravelClaimController.getCurrentClaim().addExpense(expense);
        TravelClaimController.getCurrentClaim().setCurrentExpense(expense);

        // We want to head to the summary of the expense after, so if this is a new
        // one being added we'll have to create the view we need.
        if (new_expense) {
            Intent intent = new Intent(EditExpenseActivity.this, ExpenseInfoActivity.class);
            startActivity(intent);
        }
        finish();
    }
    
    public void onCancel(View v) {
        finish();
    }
    
    public void setFieldsFromExpense() {
        name_field.setText(expense.getName());
        category_field.setText(expense.getCategory());
        date_field.setText(expense.getDate());
        currency_field.setText(expense.getCurrency());
        amount_field.setText(String.valueOf(expense.getAmount()));
        description_field.setText(expense.getDescription());
    }
}
