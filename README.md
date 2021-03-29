# RestAssuredCategoryUATs
Why: Task for Software Test Engineer job offer

Used libraries/toolkits:
Rest-assured (using Builder pattern)
TestNG
AssertJ
hamcrest
JSON.simple


1) Setup

    Before running test you must provide "Client ID" and "Client Secret" into proper fields in Authorization class (src.test.java.Helpers.Authorization)
   
2) Running tests (after inserting "Client ID" and "Client Secret")
   
      - manually: to run whole suite, build project, go to src.test.java.UAT_tests open class GETCategoriesAndParametersTests, 
      then right click on class name and chose option "Run GETCategoriesAndParametersTests", it will run all cases with annotation @Test.
      Each test can be run separately with right click on method annotated as Test, similar way as above (right click > Run methodName())
      
      - search for TestNG.xml file, right-click on it and select Run option