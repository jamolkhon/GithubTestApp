# Test exam on Android developer position 

## Requirements.

Android application fetches GitHub users list from API: `https://api.github.com/users?since=0`

### Returned data should be presented on 2 screens:

- Main screen should be shown when application is started. Here is mapping of UI elements to user parameters:
  * "login" - title
  * "id" - subtitle
  * "avatar_url" - image 
 
<div style="text-align:center"><img src="https://raw.githubusercontent.com/shakurocom/android-Test/master/MainUI.png" alt="Main Screen" /></div> 

* User details screen that is shown when you tap on any item.

<div style="text-align:center"><img src ="https://raw.githubusercontent.com/shakurocom/android-Test/master/DetailsUI.png" alt="Details Screen" /></div> 

## Optional requirements

Implementation of additional functionality will be an extra plus:

* Paging for further users loading. See `since` query parameter.
 
* Animation between Main and Details screens. 
 

## P.S.

Please upload your code to a GitHub repository and send us link to the repository.

If you have any questions - please don't hesitate to ask the contact who shared this test with you.
