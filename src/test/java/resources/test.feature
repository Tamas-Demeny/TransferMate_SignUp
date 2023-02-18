Feature: TransferMate sign up test suit

  Background: User is on sign up page
    Given User is on sign up page

  Scenario Outline: User account creation with valid credentials
    When User chooses account <type> radio button
    And User selects <country> country
    And User enters <First> and <Last> name
    And User enters email address
    And User selects the <country> mobile phone prefix
    And User enters mobile <phone> number
    And User ticks ToU and Marketing check-boxes
    And User fills in captcha
    And User clicks on Open my free account button
    Then User is redirected to the Email and Mobile Number Verification page

    Examples:
      | type       | country | First     | Last   | phone     |
      | individual | Romania | Alexander | Dubois | 182738416 |
      | education  | Poland  | Johannis  | Jester | 876548922 |

  Scenario Outline: User account creation with invalid first and last name
    When User chooses account <type> radio button
    And User selects <country> country
    And User enters <First> and <Last> name
    And User enters email address
    And User selects the <country> mobile phone prefix
    And User enters mobile <phone> number
    And User ticks ToU and Marketing check-boxes
    And User fills in captcha
    And User clicks on Open my free account button
    Then User gets invalid First and Last name warning

    Examples:
      | type       | country | First   | Last    | phone     |
      | individual | Romania | 123453@ | 3Sd#$$@ | 182738416 |
      | education  | Poland  | @#!@$   | 13#@r   | 876548922 |

  Scenario: User account creation with already registered email address
    When User chooses account individual radio button
    And User selects Romania country
    And User enters Alexander and Dubois name
    And User enters used email address
    And User selects the Romania mobile phone prefix
    And User enters mobile 075657865432 number
    And User ticks ToU and Marketing check-boxes
    And User fills in captcha
    And User clicks on Open my free account button
    Then User gets already used email warning

  Scenario: User account creation with invalid email format
    When User chooses account individual radio button
    And User selects Romania country
    And User enters Alexander and Dubois name
    And User enters invalid email address
    And User selects the Romania mobile phone prefix
    And User enters mobile 075657865432 number
    And User ticks ToU and Marketing check-boxes
    And User fills in captcha
    And User clicks on Open my free account button
    Then User gets incorrect email format warning

  Scenario Outline: User account creation with more than 10 digit phone number
    When User chooses account <type> radio button
    And User selects Romania country
    And User enters <First> and <Last> name
    And User enters email address
    And User selects the <country> mobile phone prefix
    And User enters mobile <phone> number
    And User ticks ToU and Marketing check-boxes
    And User fills in captcha
    And User clicks on Open my free account button
    Then User gets invalid phone number warning

    Examples:
      | type       | country | First    | Last    | phone                |
      | individual | Romania | Sorin    | Horhe   | 18273841612354398    |
      | education  | Poland  | Jozsef   | Benedek | 87654892213123523554 |
      | education  | USA     | Santiago | Horhez  | 43254819223139685244 |

  Scenario: User submits registration without completing mandatory fields
    When User clicks on Open my free account button
    Then User gets error messages for mandatory fields

#    Scenario: User account creation and validation
#      When User fills in the required fields and submits application
#      And User confirms email verification
#      And User sets password and fills SMS verification code
#      Then User is redirected to the new Account being processed page