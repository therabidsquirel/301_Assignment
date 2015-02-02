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

// A class for storing a basic date and for performing basic methods
// on objects of its type.
public class ClaimDate {
    private int year;
    private int month;
    private int day;
    
    public ClaimDate() {
        super();
        setDate(0, 0, 0);
    }
    
    public void setDate(String date) {
        String[] parts = date.split("/");
        
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        
        setDate(year, month, day);
    }
    
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    
    public String getDate() {
        if (year == 0) {
            return null;
        }
        return year + "/" + month + "/" + day;
    }

    // A correct date String will be in the format "yyyy/mm/dd", which this function
    // checks for. Any error in the formatting returns true, otherwise returns false.
    public static boolean invalidDate(String date) {
        String[] parts = date.split("/");

        if (parts.length != 3) {
            return true;
        }
        
        int year;
        int month;
        int day;
        
        try {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            return true;
        }
        
        // Constraints on year, month, and day.
        if (year > 2999 || year < 1000 || month > 12 || month < 1 || day > 31 || day < 1) {
            return true;
        } else if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)) {
            return true;
        } else if (day > 29 && month == 2) {
            return true;
        } else if (day > 28 && month == 2 && year % 4 != 0) {
            return true;
        }
        
        return false;
    }
    
    // If date1 is any date before date2 this method returns true. The
    // equivalent of (date1 < date2). Date strings are assumed valid
    // (checking them beforehand with invalidDate method).
    public static boolean lesserDate(String date1, String date2) {
        String[] parts1 = date1.split("/");
        int year1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int day1 = Integer.parseInt(parts1[2]);
        
        String[] parts2 = date2.split("/");
        int year2 = Integer.parseInt(parts2[0]);
        int month2 = Integer.parseInt(parts2[1]);
        int day2 = Integer.parseInt(parts2[2]);
        
        if (year1 < year2) {
            return true;
        } else if (month1 < month2 && year1 == year2) {
            return true;
        } else if (day1 < day2 && month1 == month2 && year1 == year2) {
            return true;
        }
        
        return false;
    }
}
