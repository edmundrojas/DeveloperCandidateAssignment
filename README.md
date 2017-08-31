# DeveloperCandidateAssignment
An Android app demo using the Yelp api

Features
--------

MainActivity
- Displays a keyword search for the Yelp API returning 10 restaurants 
- Displays in a grid view that includes restaurant name and address
- Ability to sort list in ASC and DESC order
- Tapping each view takes the user to a detail view using a transition animation
- Uses a navigation drawer design pattern with a an option to display FavouriteActivity

DetailActivity
- Displays an expanded view with details of selected restaurant 
- Displays a list of reviews and rating
- Displays a Fab button which gives the option to favourite current restaurant
- Displays a permission dialog prompt to grant application access to WRITE_EXTERNAL permission

FavouritesActivity
- Displays a list of restaurants the user has favourited

External Libraries
------------------

This project uses the Retrofit2 Java HTTP Client

