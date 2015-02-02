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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;

import android.app.Activity;

// All of our activities will need the two methods in here the way they are,
// so have them all inherit this class.
public abstract class ClaimActivity extends Activity {
    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    // Taken on February 1, 2015 from:
    // https://github.com/therabidsquirel/lonelyTwitter/blob/a04a649aef4b2adf53ca5c409170995c5d73cfc8/src/ca/ualberta/cs/lonelytwitter/LonelyTwitterActivity.java
    private void saveData() {
        Gson gson = new Gson();
        try {
            FileOutputStream fos = openFileOutput(TravelClaimController.getFile(), 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            gson.toJson(TravelClaimController.getClaims(), osw);
            osw.flush();
            fos.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
