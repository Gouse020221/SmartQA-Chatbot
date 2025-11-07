Feature: Checking the complete functionalities of Manage Buyer Information Setting Page
 
  @managebuyerinformation @regression_login @critical
  Scenario: Basic Login Functionality until opening setting option
    Given I open the 'chrome' browser
    Given Enter url of the website 'https://brookfieldresidential.stg.lotvue.com'
    Then I should see 'Customer Login' page with all the details
    When I enter the username from config
    When I enter the password from config and click on Login button
    Then based on the entered login details it should show Error Message or Username
    When I click on the manage settings icon
    Then I should able to see the list of settings options
    When I selects the 'Manage Buyers' setting option from the list
    Then I should be able to see 'Manage Buyers' page with all the information

  @managebuyerinformation @ai_generated_test_data
  Scenario Outline: Create Multiple Buyers with Valid and Invalid Test Data
    When I create <validBuyerCount> buyers with valid AI generated data
    And I attempt to create <invalidBuyerCount> buyers with invalid test data

    Examples: 
      | validBuyerCount | invalidBuyerCount |
      |               2 |                 1 |
