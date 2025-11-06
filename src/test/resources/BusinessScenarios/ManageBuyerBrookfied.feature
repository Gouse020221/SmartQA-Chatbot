Feature: Checking the complete functionalities of Manage Buyer Information Setting Page
Background: 
    Given I open the 'chrome' browser

  @managebuyerinformation @regression_login @critical
  Scenario: Basic Login Functionality
    Given Enter url of the website 'https://brookfieldresidential.stg.lotvue.com/'
    Then I should see 'Customer Login' page with all the details
    When I enter the uname 'support@lotvue.com'
    When I enter the password 'BR_ELV_#stg_%$' and click on Login button
    Then based on the entered login details it should show Error Message or Username

  @managebuyerinformation @smoke_test_select_setting_option
  Scenario: Selecting the setting Option
    When I click on manage settings icon
    Then I should able to see the list of settings options
    When I selects the 'Manage Buyers' setting option from the list
    Then I should be able to see 'Manage Buyers' page with all the information

  @managebuyerinformation @ai_generated_test_data
  Scenario Outline: Create Multiple Buyers with Valid and Invalid Test Data
    When I create <validBuyerCount> buyers with valid AI generated data
    And I attempt to create <invalidBuyerCount> buyers with invalid test data
    
    Examples:
      | validBuyerCount | invalidBuyerCount |
      | 2               | 3                 |