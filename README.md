This software downloads the zipcodes that match a given country code (eg, country code = "US").  Version 2 downloads from each state's file.  This retrieves data for all 50 states.  Version 1 only downloads from a file for the whole US which contains only 17 states and 100 postal codes.  Version 2 has been merged from the branch "downloadByStateAbbr".

# Features:
* Version 3
    * Parallelization offering 2x speedup
    * Reentrant locks on load and clear ZipcodeService methods to prevent errors from concurrent execution
* Version 2 (previously, downloadByStateAbbr Branch)
    * US added during BootStrap
    * Load zipcodes for all states
    * clear all zipcodes for country
    * Delete country
    * Zipcode creation is limited to those parsed from loaded xml
    * Loads states during BootStrap
    * Unit, Integration and Functional tests
* Version 1
    * Add a new country with the country code to download for
    * Load zipcodes into database for a given country
    * clear zipcodes and states for a given country
    * Delete countries
    * Unit, Integration, and Functional tests
    * State and zipcode creation is limited to those parsed from loaded xml


# Limitations
* Controller unit tests don't all pass
* Validators on Zipcode class limit functionality to US format but other country codes will not break it

