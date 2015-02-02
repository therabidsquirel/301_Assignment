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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// For displaying a summary of all the claims, letting a user add
// new claims, or expand an existing claim.
public class ClaimsListActivity extends ClaimActivity {
    private ListView claimsList;
    private ArrayAdapter<Claim> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_list);
        
        claimsList = (ListView) findViewById(R.id.claimsList);

        loadData();
        adapter = new ArrayAdapter<Claim>(this, android.R.layout.simple_list_item_1, TravelClaimController.getClaims());
        claimsList.setAdapter(adapter);
        claimsList.setScrollbarFadingEnabled(false);

        claimsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Claim claim = TravelClaimController.getClaims().get(position);
                TravelClaimController.setCurrentClaim(claim);
                Intent intent = new Intent(ClaimsListActivity.this, ClaimInfoActivity.class);
                startActivity(intent);
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.claims_list, menu);
        return true;
    }

    public void addClaim(View v) {
        TravelClaimController.setCurrentClaim(null);
        Intent intent = new Intent(ClaimsListActivity.this, EditClaimActivity.class);
        startActivity(intent);
    }

    // Taken on February 1, 2015 from:
    // https://github.com/therabidsquirel/lonelyTwitter/blob/a04a649aef4b2adf53ca5c409170995c5d73cfc8/src/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
    private void loadData() {
        Gson gson = new Gson();
        ArrayList<Claim> claims = new ArrayList<Claim>();
        
        try {
            FileInputStream fis = openFileInput(TravelClaimController.getFile());
            InputStreamReader in = new InputStreamReader(fis);
            Type typeOfT = new TypeToken<ArrayList<Claim>>(){}.getType();
            claims = gson.fromJson(in, typeOfT);
            fis.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        TravelClaimController.setClaims(claims);
    }
}
