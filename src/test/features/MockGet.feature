Feature: Creating a customer

  Scenario: Getting an existing customer
    Given a customer exists with id 24
    When a user retrieves the user by id 24
    Then the status code is 200
    And response customer includes
      | id         | 24    |
      | first_name | first |
      | last_name  | last  |
    And response customer properties includes
      | age           | 19         |
      | active        | true       |
      | date_of_birth | 30/09/1980 |

  Scenario: Getting an existing customer
    Given a customer exists with id 24
    When a user retrieves the user by id 400
    Then the status code is 404
