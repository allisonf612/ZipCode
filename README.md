This software downloads the zipcodes for a given country code.  Master branch only downloads from a file for the whole US which contains only 17 states and 100 postal codes.  The branch "downloadByStateAbbr" downloads from each state's file.  This retrieves data for all 50 states.  The branches will be merged when tests have been modified for "downloadByStateAbbr."  (Tests are currently tailored to master branch).

# Features:
* master Branch
    * Add a new country with the country code to download for
    * Load zipcodes into database for a given country
    * clear zipcodes and states for a given country
    * Delete countries
    * Unit, Integration, and Functional tests
    * State and zipcode creation is limited to those parsed from loaded xml
* downloadByStateAbbr Branch
    * US added during BootStrap
    * Load zipcodes for all states
    * clear all zipcodes for country
    * Delete country
    * Zipcode creation is limited to those parsed from loaded xml
    * Loads states during BootStrap
    * Unit, Integration and Functional tests

# Limitations
* master Branch
    * Controller unit tests don't all pass
    * Validators on Zipcode class limit functionality to US format but other country codes will not break it

