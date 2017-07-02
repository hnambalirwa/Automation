#Application under Test.
#Verify that all the buttons are present and text written on them is readable

Feature: Calculator App
  Verify that all the buttons are present and text written on them is readable

  Scenario: Calculator buttons
    Given The user has opened up the calculator App, user gets an a screen showing number buttons
    When user clicks on any number button
    Then user sees the clicked number in the top section of the app