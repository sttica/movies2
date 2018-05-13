# Popular Movies, Stage 2 - Udacity Android Developer Nanodegree Project

## Waht does this do?
In this stage I added additional functionality to the app from Stage 1
- Add more information to movie details view
- Allow users to view and play trailers
- Allow users to read reviews of a selected movie
- Allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that does not require an API request
- Modify the existing sorting criteria for the main view to include an additional pivot to show favorites collection

## What I learned
Use a ConentProvider
Hide the API key - I adapted the example code provided here https://medium.com/@abhi007tyagi/storing-api-keys-using-android-ndk-6abb0adcadad
-> thanks to Abhinav Tyagi!
Start several ASYNC tasks in one activity
Dynamically extend an existing layout (for the trailers)
Add SQLite persistence for movie favorites
