# CS126 WAFFLES Coursework Report [1915472]

<!-- This document gives a brief overview about your solution.  -->
<!-- You should change number in the title to your university ID number.  -->
<!-- You should delete these comments  -->
<!-- And for the latter sections should delete and write your explanations in them. -->
<!-- # <-- Indicates heading, ## <-- Indicates subheading, and so on -->

## CustomerStore

### Overview

<!-- A short description about what structures/algorithms you have implemented and why you used them. For example: -->
<!-- The template is only a guide, you are free to make any changes, add any bullets points, re-word it entirely, etc. -->
<!-- * <- is a bullet point, you can also use - minuses or + pluses instead -->
<!-- And this is *italic* and this is **bold** -->
<!-- Words in the grave accents, or in programming terms backticks, formats it as code: `put code here` -->

- I have used an `AVL Tree` structure to store and process customers because it was quick to search and natrually sorted.
- The `AVLTreeCustomer` is sorted with `customer id` in default.
  - To sort customers in their name will require a new `AVLTreeCustomer` with `"name"` in it's constructor.
- The `blacklistedCustomerID` is stored in `AVLTreeID` and sort in ascending order of its id. Because it is quick to search.

### Space Complexity

<!-- Write here what you think the overall store space complexity is and gives a brief reason why. -->
<!-- <br> gives a line break -->
<!-- In tables, you don't really need the spaces, it only makes it look nice in text form -->
<!-- You can just do "CustomerStore|O(n)|I have used a single `ArrayList` to store customers." -->

| Store                   | Worst Case | Description                                                     |
| ----------------------- | ---------- | --------------------------------------------------------------- |
| `customerArray`         | O(n)       | I have used an `AVLTreeCustomer` structure to store customers   |
| `blackListedCustomerID` | O(n)       | I have used an `AVLTreeID` structure to blacklisted customer ID |

### Time Complexity

<!-- Tell us the time complexity of each method and give a very short description. -->
<!-- These examples may or may not be correct, these examples have not taken account for other requirements like duplicate IDs and such.  -->
<!-- Again, the template is only a guide, you are free to make any changes. -->
<!-- If you did not do a method, enter a dash "-" -->
<!-- Technically, for the getCustomersContaining(s) average case, you are suppose to do it relative to s -->
<!-- Note, that this is using the original convertToAccents() method -->
<!-- So O(a*s + n*(a*t + t)) -->
<!-- Where a is the amount of accents -->
<!-- Where s is the length of the search term String  -->
<!-- Where t is the average length of a name -->
<!-- Where n is the total number of customers -->
<!-- But you can keep it simple -->

| Method                           | Average Case     | Description                                                                                                                                                                                                                                                      |
| -------------------------------- | ---------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| addCustomer(Customer c)          | `Θ(2log(n))`     | `n` for the size of the customerTree<br>First `log(n)` is for searching through already added customers finding for the repeated one<br> Second `log(n)` is for adding the customer into the tree.<br> However if deletion happens, that can also takes `log(n)` |
| addCustomer(Customer[] c)        | `Θ(2mlog(n))`    | Above process repeated `m` time                                                                                                                                                                                                                                  |
| getCustomer(Long id)             | `Θ(log(n))`      | Linear search <br>`n` is the size of the customerTree                                                                                                                                                                                                            |
| getCustomers()                   | `Θ(log(n))`      | Sorted <br>Access nodes in order and adding them into an array list then to an array.                                                                                                                                                                            |
| getCustomers(Customer[] c)       | `Θ(2nlogn + 2n)` | AVL Tree <br>`n` is the length of the customers<br>Insert each one of the array element into the tree.<br> Get everything out from the tree.                                                                                                                     |
| getCustomersByName()             | `Θ(2nlogn + 2n)` | AVL Tree <br>`n` is the size of the customerTree<br>Get customers out from the original tree<br>Insert each one of then into the tree that is sorted by name<br>                                                                                                 |
| getCustomersByName(Customer[] c) | -                | -                                                                                                                                                                                                                                                                |
| getCustomersContaining(String s) | `Θ(n)`           | Searches all customers <br>`n` is the size of the `customerTree`                                                                                                                                                                                                 |

<!-- Don't delete these <div>s! -->
<!-- And note the spacing, do not change -->
<!-- This is used to get a page break when we convert your report to PDF to read when marking -->
<!-- It is not the end of the world if you do remove, it just makes it harder to read if you do -->
<!-- On things you can remove though, you should remember to remove these comments -->

<div style="page-break-after: always;"></div>

## FavouriteStore

### Overview

- I have used `ArrayList` to store favourites because it is flexible.
- I used `favouriteQuickSort` to sort favourites
  - `idCompare` is used to compare id
  - `dateCompare` is used to compare by date

### Space Complexity

| Store                    | Worst Case | Description                                                                                                   |
| ------------------------ | ---------- | ------------------------------------------------------------------------------------------------------------- |
| `favouriteArray`         | O(n)       | I have used `ArrayList` for `favouriteArray`<br>Where n is the number of favorite stored in the arraylist     |
| `blacklistedFavouriteID` | O(n)       | I have used `AVLTreeID` to store all the blacklisted id and the tree is inserted in ascending order of the ID |

### Time Complexity

| Method                                                 | Average Case     | Description                                                                                                                                           |
| ------------------------------------------------------ | ---------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| addFavourite(Favourite f)                            | `Θ(n + m)`       | `n` is length of stored `favouriteArray` <br>m is length of `blacklist`                                                                               |
| addFavourite(Favourite[] f)                          | `Θ(k(n + m))`    | `k` is the length of given array <br>Remaining is the same as the above                                                                               |
| getFavourite(Long id)                                | `Θ(n)`           | `n` is length of array                                                                                                                                |
| getFavourites()                                      | `Θ(n log n)`     | `quicksort` using `IDCompare()`                                                                                                                       |
| getFavourites(Favourite[] f)                         | `Θ(n log n)`     | `quicksort` using `IDCompare()`                                                                                                                       |
| getFavouritesByCustomerID(Long id)                   | `Θ(n log n + n)` | `n` find matching favourite with given id<br>`quicksort` using `IDCompare()`                                                                          |
| getFavouritesByRestaurantID(Long id)                 | `Θ(n log n)`     | `quicksort` using `IDCompare()`                                                                                                                       |
| getCommonFavouriteRestaurants(Long id1, Long id2)    | `Θ(n)`           | linear seach in `hashmap` to get to find commons                                                                                                      |
| getMissingFavouriteRestaurants(Long id1, Long id2)   | `Θ(n)`           | linear seach in `hashmap` to get to remove commons and the remaining are the missings from `customer 1`                                               |
| getNotCommonFavouriteRestaurants(Long id1, Long id2) | `Θ(n)`           | repeat the above `getMissingFavouriteRestaurants(Long id1, Long id2)` twice                                                                           |
| getTopCustomersByFavouriteCount()                    | `Θ(n^2)`         | Loop through favouriteArray and add them into topCustomerFavourites.<br/>Then check the newly formed topCustomerFavourites for existed Favourites.    |
| getTopRestaurantsByFavouriteCount()                  | `Θ(n^2)`         | Loop through favouriteArray and add them into topRestaurantFavourites.<br>Then check the newly formed topRestaurantFavourites for existed Favourites. |

<div style="page-break-after: always;"></div>

## RestaurantStore

### Overview

- I have used `AVL Tree` to store restaurants because its natrually sorted.
  - `AVLTreeRestaurant` is sorted by ID in default
  - In order to sort the array with the name, it needed to include `"name"` in the tree's constractor.
  - similar approach to `"warwickStar"`, `"rating"` and `"date"`.
- I have used `AVLTreeID` to store blacklisted restaurant ID.

### Space Complexity

| Store                     | Worst Case | Description                                                |
| ------------------------- | ---------- | ---------------------------------------------------------- |
| `restaurantTree`          | `O(n)`     | I have used `AVLTreeRestaurant` to store restaurants       |
| `blackListedRestaurantID` | `O(n)`     | I have used `AVLTreeID` to store blacklisted restaurant ID |

### Time Complexity

| Method                                                             | Average Case | Description                                                                                        |
| ------------------------------------------------------------------ | ------------ | -------------------------------------------------------------------------------------------------- |
| addRestaurant(Restaurant r)                                        | `Θ(logn)`    | search through `restaurantTree` to find repetitive restaurant ID                                   |
| addRestaurant(Restaurant[] r)                                      | `Θ(nlogn)`   | repeat above process n times                                                                       |
| getRestaurant(Long id)                                             | `Θ(logn)`    | search through AVL Tree                                                                            |
| getRestaurants()                                                   | `Θ(nlogn)`   | Get sorted restaurants from binary tree                                                            |
| getRestaurants(Restaurant[] r)                                     | `Θ(nlogn)`   | sort array by insert each element into `AVLTreeRestaurant`                                         |
| getRestaurantsByName()                                             | `Θ(nlogn)`   | sort array by insert each element by name into `AVLTreeRestaurant`                                 |
| getRestaurantsByDateEstablished()                                  | `Θ(nlogn)`   | sort `restaurantStore` element by insert each element by date established into `AVLTreeRestaurant` |  |
| getRestaurantsByDateEstablished(Restaurant[] r)                    | `Θ(nlogn)`   | sort array by insert each element by date established into `AVLTreeRestaurant`                     |  |
| getRestaurantsByWarwickStars()                                     | `Θ(nlogn)`   | sort array by insert each element by name into `AVLTreeRestaurant`                                 |
| getRestaurantsByRating(Restaurant[] r)                             | `Θ(nlogn)`   | sort array by insert each element by name into `AVLTreeRestaurant`                                 |
| getRestaurantsByDistanceFrom(float lat, float lon)                 | `Θ(nlogn)`   | sort `restaurantDistance` by inserting each of them into `AVLTreeRestaurantDistance`               |
| getRestaurantsByDistanceFrom(Restaurant[] r, float lat, float lon) | `Θ(nlogn)`   | sort `restaurantDistance` by inserting each of them into `AVLTreeRestaurantDistance`               |
| getRestaurantsContaining(String s)                                 | `Θ(nlogn)`   | search through `restaurantStore` for matching pattern in restaurant name, place name, cusine name  |

<div style="page-break-after: always;"></div>

## ReviewStore

### Overview

- I have used `MyArrayList` for storing review objects
- I have used `AVLTreeID` for storing blackListed ID

### Space Complexity

| Store                 | Worst Case | Description                                          |
| --------------------- | ---------- | ---------------------------------------------------- |
| `reviewArray`         | O(n)       | I have used `MyArrayList` for storing review objects |
| `blackListedReviewID` | O(n)       | I have used `AVLTreeID` for storing blackListed ID   |

### Time Complexity

| Method                                     | Average Case | Description                                                                                                     |
| ------------------------------------------ | ------------ | --------------------------------------------------------------------------------------------------------------- |
| addReview(Review r)                        | `Θ(n)`       | check through reviews to find replicates                                                                        |
| addReview(Review[] r)                      | `Θ(n^2)`     | repeat above process n times.                                                                                   |
| getReview(Long id)                         | `Θ(n)`       | loop through each review in review array                                                                        |
| getReviews()                               | `Θ(nlogn)`   | Quicksort by ID                                                                                                 |
| getReviews(Review[] r)                     | `Θ(nlogn)`   | Quicksort by ID                                                                                                 |
| getReviewsByDate()                         | `Θ(nlogn)`   | Quicksort by date                                                                                               |
| getReviewsByRating()                       | `Θ(nlogn)`   | Quicksort by rating                                                                                             |
| getReviewsByRestaurantID(Long id)          | `Θ(nlogn)`   | Quicksort by date and loop through reviews to find matched restaurant id                                        |
| getReviewsByCustomerID(Long id)            | `Θ(nlogn)`   | Quicksort by date and loop through reviews to find matched customer id                                          |
| getAverageCustomerReviewRating(Long id)    | `Θ(n)`       | loop through review array to sum up all the customer's ratings                                                  |
| getAverageRestaurantReviewRating(Long id)  | `Θ(n)`       | loop through review array to sum up all the restaurant's ratings                                                |
| getCustomerReviewHistogramCount(Long id)   | `Θ(n)`       | loop through review array to collect all the customer's review ratings                                          |
| getRestaurantReviewHistogramCount(Long id) | `Θ(n)`       | loop through review array to collect all the restaurant's review ratings and set them into an array of length 5 |
| getTopCustomersByReviewCount()             | `Θ(nlogn)`   | loop through reviews and adding them into an `AVLTreeIDCounter`                                                 |
| getTopRestaurantsByReviewCount()           | `Θ(nlogn)`   | loop through reviews and adding them into an `AVLTreeIDCounter`                                                 |
| getTopRatedRestaurants()                   | `Θ(nlogn)`   | loop through reviews and adding matching restaurants into an `AVLTreeRating`                                    |
| getTopKeywordsForRestaurant(Long id)       | `Θ(nlogn)`   | loop through reviews and adding matching keyword into an `AVLTreeCounter`                                       |
| getReviewsContaining(String s)             | `Θ(n)`       | loop thorugh strings and then check if string contains in `placeName`, restaurant name or Cusine name           |

<div style="page-break-after: always;"></div>

## Util

### Overview

- **ConvertToPlace**
  - Searches through all the places from `placeArray` to find a match with the given latitude and
    longitude.
- **DataChecker**
  - Check each Class fields.
- **HaversineDistanceCalculator (HaversineDC)**
  - Calculate distance between 2 locations with their latitude and longtitude.
- **KeywordChecker**
  - Check whether a word is a keyword from the keywords list and will search in linear.
- **StringFormatter**
  - Format accented character into normal ones. Lists of characters are stored in Hashmap.

### Space Complexity

| Util            | Worst Case | Description                                               |
| --------------- | ---------- | --------------------------------------------------------- |
| ConvertToPlace  | O(n)       | import all place infomation into an array                 |
| DataChecker     | O(1)       | ...                                                       |
| HaversineDC     | O(1)       | ...                                                       |
| KeywordChecker  | O(n)       | stored all keywords in hashmap                            |
| StringFormatter | O(n)       | stored all accented strings and normal strings in hashmap |

### Time Complexity

| Util            | Method                                                                             | Average Case | Description                                                                                                         |
| --------------- | ---------------------------------------------------------------------------------- | ------------ | ------------------------------------------------------------------------------------------------------------------- |
| ConvertToPlace  | convert(float lat, float lon)                                                      | `Θ(n)`       | `n` is the length of the `placeArray`. Program loop through `placeArray` to find matching latitude and longtitude   |
| DataChecker     | extractTrueID(String[] repeatedID)                                                 | `Θ(3^2)`     | `n` is the length of the `repeatedID`. Program loop through string to check if there's a repeated id                |
| DataChecker     | isValid(Long id)                                                                   | `Θ(n)`       | `n` is the `length()` of the String form of `id`. Program loop through id string to find matching digit             |
| DataChecker     | isValid(Customer customer)                                                         | `Θ(1)`       | check customer properties valid for each fields                                                                     |
| DataChecker     | isValid(Favourite favourite)                                                       | `Θ(1)`       | check favourite properties valid for each fields                                                                    |
| DataChecker     | isValid(Restaurant restaurant)                                                     | `Θ(1)`       | check restaurant properties valid for each fields                                                                   |
| DataChecker     | isValid(Review review)                                                             | `Θ(1)`       | check review properties valid for each fields                                                                       |
| HaversineDC     | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | `Θ(1)`       | calculate km distance with equation                                                                                 |
| HaversineDC     | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | `Θ(1)`       | calculate distance in miles with equation                                                                           |
| KeywordChecker  | isAKeyword(String s)                                                               | `Θ(1)`       | use hash value to check wether a word is a keyword in hash map                                                      |
| StringFormatter | convertAccentsFaster(String s)                                                     | `Θ(n)`       | `n` is the length of the string and the program will go over each special characters and convert it into normal one |

## References

HashMap Structure was taken from CS126 lab 4 - [LINK](https://warwick.ac.uk/fac/sci/dcs/teaching/material/cs126/labs/lab4)

Files:
+ `structures/HashMap.java`
+ `structures/KeyValuePair.java`
+ `structures/KeyValuePairLinkedList.java`
+ `structures/ListElement.java`
