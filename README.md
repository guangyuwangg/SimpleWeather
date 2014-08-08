
|------------------------------------------------------------|
|  / ___/(_)___ ___| |     / /__  ____ _/ /_/ /_  ___  _____ |
|  \__ \/ / __ `__ \ | /| / / _ \/ __ `/ __/ __ \/ _ \/ ___/ |
| ___/ / / / / / / / |/ |/ /  __/ /_/ / /_/ / / /  __/ /     |
|/____/_/_/ /_/ /_/|__/|__/\___/\__,_/\__/_/ /_/\___/_/      |
|------------------------------------------------------------|

Demo pictures can be found at: https://guangyuwangg.github.io

How to use:
Install the application on a real device.
Please make sure you use a real device (NOT EMULATOR!) for this application. The reason is that I used a ProgressBar which is too heavy for emulator.

Functionality:
- Display the condition, temperature, "feels like" temperature in current location(hard coded to Beijing at this moment).
- Allow users to enter their id
- Notify users if there is any update found
- Shows an offline icon when the app can not connect to the Internet

How it works:
This application will check for update in two cases: 1. User click for update; 2. Background task check for update. 
If it's the case that background task check for an update, the updated information will not be displayed. Only one notification will be displayed on the screen and inform the user.

Work to be done:
- Add support to multiple locations. Right now the code only checks weather for a fix location.
- Add offline support. Use previous forecast data when off-line
