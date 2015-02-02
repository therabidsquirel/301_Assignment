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

public class EditClaimActivity extends ClaimActivity {
    private Claim claim;
    private boolean new_claim;
    
    private EditText name_field;
    private EditText status_field;
    private EditText start_date_field;
    private EditText end_date_field;
    private EditText description_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_claim);
        
        claim = TravelClaimController.getCurrentClaim();
        
        name_field = (EditText) findViewById(R.id.claimNameField);
        status_field = (EditText) findViewById(R.id.statusField);
        start_date_field = (EditText) findViewById(R.id.startDateField);
        end_date_field = (EditText) findViewById(R.id.endDateField);
        description_field = (EditText) findViewById(R.id.claimDescriptionField);

        // claim will be null if we came here from wanting to add a new one. Otherwise
        // we're here because we're editing an existing one.
        if (claim == null) {
            claim = new Claim();
            new_claim = true;
            this.setTitle(R.string.title_activity_add_claim);
            // Force a new claim to be created with the default status.
            status_field.setFocusable(false);
        } else {
            new_claim = false;
            this.setTitle(R.string.title_activity_edit_claim);
            status_field.setFocusable(true);
        }
        
        // If the claim can't be edited due to status, then disable all of the fields
        // from being edited except status.
        if (TravelClaimController.canEditCurrentClaim()) {
            name_field.setFocusable(true);
            start_date_field.setFocusable(true);
            end_date_field.setFocusable(true);
            description_field.setFocusable(true);
        } else {
            name_field.setFocusable(false);
            start_date_field.setFocusable(false);
            end_date_field.setFocusable(false);
            description_field.setFocusable(false);
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
        setFieldsFromClaim();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_claim, menu);
        return true;
    }

    // When the save button is hit, check each field in order to ensure everything is
    // of valid type. Show the user a toast message describing any error that occurred.
    // If no error, update the claim and exit out of this view.
    public void onSave(View v) {
        String name = name_field.getText().toString();
        String status = status_field.getText().toString();
        String start_date = start_date_field.getText().toString();
        String end_date = end_date_field.getText().toString();
        String description = description_field.getText().toString();
        
        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        ArrayList<String> presets = new ArrayList<String>();
        presets.add(TravelClaimController.STATUS1);
        presets.add(TravelClaimController.STATUS2);
        presets.add(TravelClaimController.STATUS3);
        presets.add(TravelClaimController.STATUS4);
        
        boolean valid_status = false;
        for (String preset : presets) {
            if (status.equals(preset)) {
                valid_status = true;
                break;
            }
        }
        
        if (!valid_status && !status.isEmpty()) {
            Toast.makeText(this, "Invalid status!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ClaimDate.invalidDate(start_date)) {
            Toast.makeText(this, "Invalid start date!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ClaimDate.invalidDate(end_date)) {
            Toast.makeText(this, "Invalid end date!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (ClaimDate.lesserDate(end_date, start_date)) {
            Toast.makeText(this, "End date can't be before start date!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        claim.setName(name);
        claim.setStatus(status);
        claim.setStartDate(start_date);
        claim.setEndDate(end_date);
        claim.setDescription(description);

        TravelClaimController.addClaim(claim);
        TravelClaimController.setCurrentClaim(claim);
        
        // We want to head to the summary of the claim after, so if this is a new
        // one being added we'll have to create the view we need.
        if (new_claim) {
            Intent intent = new Intent(EditClaimActivity.this, ClaimInfoActivity.class);
            startActivity(intent);
        }
        finish();
    }
    
    public void onCancel(View v) {
        finish();
    }
    
    public void setFieldsFromClaim() {
        name_field.setText(claim.getName());
        status_field.setText(claim.getStatus());
        start_date_field.setText(claim.getStartDate());
        end_date_field.setText(claim.getEndDate());
        description_field.setText(claim.getDescription());
    }
}
