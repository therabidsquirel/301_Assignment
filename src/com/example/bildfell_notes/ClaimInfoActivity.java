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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// For displaying all of the information of a claim, and letting a user
// decide to either edit the information, delete the claim, email the
// claim, or add a new expense item.
public class ClaimInfoActivity extends ClaimActivity {
    private Claim claim;

    private TextView claimAttributes;
    private ListView expensesList;
    private ArrayAdapter<Expense> adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_info);

        claim = TravelClaimController.getCurrentClaim();
        
        claimAttributes = (TextView) findViewById(R.id.claimAttributes);
        expensesList = (ListView) findViewById(R.id.expensesList);
        
        ArrayList<Expense> expenses = TravelClaimController.getCurrentClaim().getExpenses();
        adapter = new ArrayAdapter<Expense>(this, android.R.layout.simple_list_item_1, expenses);
        expensesList.setAdapter(adapter);
        expensesList.setScrollbarFadingEnabled(false);

        expensesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense expense = claim.getExpenses().get(position);
                TravelClaimController.getCurrentClaim().setCurrentExpense(expense);
                Intent intent = new Intent(ClaimInfoActivity.this, ExpenseInfoActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        setClaimAttributes();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.claim_info, menu);
        return true;
    }

    // Only allow addition of new expense if the claim is editable.
    public void addExpense(View v) {
        if (TravelClaimController.canEditCurrentClaim()) {
            TravelClaimController.getCurrentClaim().setCurrentExpense(null);
            Intent intent = new Intent(ClaimInfoActivity.this, EditExpenseActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, TravelClaimController.editRejection(), Toast.LENGTH_SHORT).show();
        }
    }

    public void editClaim(MenuItem menu) {
        Intent intent = new Intent(ClaimInfoActivity.this, EditClaimActivity.class);
        startActivity(intent);
    }
    
    public void deleteClaim(MenuItem menu) {
        TravelClaimController.deleteClaim(claim);
        finish();
    }
    
    // Will get a string that represents all of the information for the specified claim, and
    // then create an email intent with the string as the body. Will fail if there are no
    // email apps on the device.
    public void emailClaim(MenuItem menu) {
        String email = TravelClaimController.getEmailText(claim);
        
        // Taken on February 1, 2015 from:
        // https://stackoverflow.com/questions/8284706/send-email-via-gmail
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT, email);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setClaimAttributes() {
        claimAttributes.setText(claim.toStringWDescription());
    }
}
