@projectStatus
Feature: Checking the search functionality of both address & buyer

   Scenario: Basic Login and Search Functionality
    Given I open the 'chrome' browser
    Given Enter url of the website 'https://brookfieldresidential.stg.lotvue.com'
   Then I should see 'Customer Login' page with all the details
   When I enter the uname 'support@lotvue.com'
    When I enter the password 'BR_ELV_#stg_%$' and click on Login button
    Then based on the entered login details it should show Error Message or Username
     When I click on the manage settings icon
    Then I should able to see the list of settings options
    When I selects the 'Manage Buyers' setting option from the list
    Then I should be able to see 'Manage Buyers' page with all the information
