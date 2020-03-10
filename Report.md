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

* I have used an `AVL Tree` structure to store and process customers because it was quick to search and natrually sorted.
* The `AVLTreeCustomer` is sorted with `customer id` in default.
  *  To sort customers in their name will require a new `AVLTreeCustomer` with "name" in it's constructor.

### Space Complexity
<!-- Write here what you think the overall store space complexity is and gives a brief reason why. -->
<!-- <br> gives a line break -->
<!-- In tables, you don't really need the spaces, it only makes it look nice in text form -->
<!-- You can just do "CustomerStore|O(n)|I have used a single `ArrayList` to store customers." -->

 Store         | Worst Case | Description                                            
 ------------- | ---------- | ------------------------------------------------------ 
 CustomerStore | O(n)       | I have used an `AVL Tree` structure to store customers 

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

Method                           | Average Case     | Description
-------------------------------- | ---------------- | -----------
addCustomer(Customer c)          | `Θ(2log(n))` | `n` for the size of the customerTree<br>First `log(n)` is for searching through already added customers finding for the repeated one<br> Second `log(n)` is for adding the customer into the tree.<br> However if deletion happens, that can also takes `log(n)` 
addCustomer(Customer[] c)        | `Θ(2mlog(n))`    | Above process repeated `m` time 
getCustomer(Long id)             | `Θ(log(n))` | Linear search <br>`n` is the size of the customerTree 
getCustomers()                   | `Θ(log(n))` | Sorted <br>Access nodes in order and adding them into an array list then to an array. 
getCustomers(Customer[] c)       | `Θ(2nlogn + 2n)` | AVL Tree <br>`n` is the length of the customers<br>Insert each one of the array element into the tree.<br> Get everything out from the tree. 
getCustomersByName()             | `Θ(2nlogn + 2n)` | AVL Tree <br>`n` is the size of the customerTree<br>Get customers out from the original tree<br>Insert each one of then into the tree that is sorted by name<br> 
getCustomersByName(Customer[] c) | -                | -
getCustomersContaining(String s) | `Θ(n)` | Searches all customers <br>`n` is the size of the `customerTree` 

<!-- Don't delete these <div>s! -->
<!-- And note the spacing, do not change -->
<!-- This is used to get a page break when we convert your report to PDF to read when marking -->
<!-- It is not the end of the world if you do remove, it just makes it harder to read if you do -->
<!-- On things you can remove though, you should remember to remove these comments -->

<div style="page-break-after: always;"></div>

## FavouriteStore
### Overview
* I have used `ArrayList` to store favourites because it is flexible.
* I used `CustomerQuicksort` to sort arrays
  * `idCompare` is used to compare id
  * `nameCompare` is used to compare lastname and first name. if both are same then it will compare by their id.

### Space Complexity
 Store          | Worst Case | Description                                                  
 -------------- | ---------- | ------------------------------------------------------------ 
 FavouriteStore | O(n)       | I have used `ArrayList` for `favouriteArray` and `blacklistedFavouriteID`<br>Where n is the number of favorite stored in object 

### Time Complexity
Method                                                          | Average Case     | Description
------------------------------------------------------- | ---------------- | -----------
`addFavourite(Favourite f)`                             | `O(n + m)` | `n` is length of stored `favouriteArray` <br>m is length of `blacklist`
`addFavourite(Favourite[] f)`                           | `O(k(n + m))` | `k` is the length of given array <br>Remaining is the same as the above
`getFavourite(Long id)`                                 | `O(n)`      | `n` is length of array
`getFavourites()`                                       | `O(n log n)` | `quicksort` using `IDCompare()`
`getFavourites(Favourite[] f)`                          | `O(n log n)` | `quicksort` using `IDCompare()`
`getFavouritesByCustomerID(Long id)`                    | `O(n log n + n)` | `n` find matching favourite with given id<br>`quicksort` using `IDCompare()`
`getFavouritesByRestaurantID(Long id)`                  | `O(n log n)` | `quicksort` using `IDCompare()`
`getCommonFavouriteRestaurants(Long id1, Long id2)`     | `Θ(n)` | linear seach in `hashmap` to get to find commons 
`getMissingFavouriteRestaurants(Long id1, Long id2)`    | `Θ(n)` | linear seach in `hashmap` to get to remove commons and the remaining are the missings from `customer 1` 
`getNotCommonFavouriteRestaurants(Long id1, Long id2)`  | `Θ(n)`  | repeat the above `getMissingFavouriteRestaurants(Long id1, Long id2) ` twice 
`getTopCustomersByFavouriteCount()`                     | `O(n^2)` | Loop through favouriteArray and add them into topCustomerFavourites.<br/>Then check the newly formed topCustomerFavourites for existed Favourites.
`getTopRestaurantsByFavouriteCount()`                   | `O(n^2)` | Loop through favouriteArray and add them into topRestaurantFavourites.<br>Then check the newly formed topRestaurantFavourites for existed Favourites.

<div style="page-break-after: always;"></div>

## RestaurantStore
### Overview
* I have used `AVL Tree` to store restaurants because its natrually sorted.
* I used `restaurantQuicksort` to sort `restaurantArray`

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
RestaurantStore | O(...)     | I have used `...` ... <br>Where `...` is ...

### Time Complexity
Method                                                                        | Average Case     | Description
----------------------------------------------------------------------------- | ---------------- | -----------
addRestaurant(Restaurant r)                                                   | O(...)           | Description <br>`...` is ...
addRestaurant(Restaurant[] r)                                                 | O(...)           | Description <br>`...` is ...
getRestaurant(Long id)                                                        | O(...)           | Description <br>`...` is ...
getRestaurants()                                                              | O(...)           | Description <br>`...` is ...
getRestaurants(Restaurant[] r)                                                | O(...)           | Description <br>`...` is ...
getRestaurantsByName()                                                        | O(...)           | Description <br>`...` is ...
getRestaurantsByDateEstablished()                                             | O(...)           | Description <br>`...` is ...
getRestaurantsByDateEstablished(Restaurant[] r)                    | O(...)           | Description <br>`...` is ...
getRestaurantsByWarwickStars()                                                | O(...)           | Description <br>`...` is ...
getRestaurantsByRating(Restaurant[] r)                                        | O(...)           | Description <br>`...` is ...
getRestaurantsByDistanceFrom(float lat, float lon)                 | O(...)           | Description <br>`...` is ...
getRestaurantsByDistanceFrom(Restaurant[] r, float lat, float lon) | O(...)           | Description <br>`...` is ...
getRestaurantsContaining(String s)                                            | O(...)           | Description <br>`...` is ...

<div style="page-break-after: always;"></div>

## ReviewStore
### Overview
* I have used `...` to store reviews ...
* I used `...` to sort ...
* To get ...
* To get top ...

### Space Complexity
Store           | Worst Case | Description
--------------- | ---------- | -----------
ReviewStore     | O(...)     | I have used `...` ... <br>Where `...` is ...

### Time Complexity
Method                                     | Average Case     | Description
------------------------------------------ | ---------------- | -----------
addReview(Review r)                        | O(...)           | Description <br>`...` is ...
addReview(Review[] r)                      | O(...)           | Description <br>`...` is ...
getReview(Long id)                         | O(...)           | Description <br>`...` is ...
getReviews()                               | O(...)           | Description <br>`...` is ...
getReviews(Review[] r)                     | O(...)           | Description <br>`...` is ...
getReviewsByDate()                         | O(...)           | Description <br>`...` is ...
getReviewsByRating()                       | O(...)           | Description <br>`...` is ...
getReviewsByRestaurantID(Long id)          | O(...)           | Description <br>`...` is ...
getReviewsByCustomerID(Long id)            | O(...)           | Description <br>`...` is ...
getAverageCustomerReviewRating(Long id)    | O(...)           | Description <br>`...` is ...
getAverageRestaurantReviewRating(Long id)  | O(...)           | Description <br>`...` is ...
getCustomerReviewHistogramCount(Long id)   | O(...)           | Description <br>`...` is ...
getRestaurantReviewHistogramCount(Long id) | O(...)           | Description <br>`...` is ...
getTopCustomersByReviewCount()             | O(...)           | Description <br>`...` is ...
getTopRestaurantsByReviewCount()           | O(...)           | Description <br>`...` is ...
getTopRatedRestaurants()                   | O(...)           | Description <br>`...` is ...
getTopKeywordsForRestaurant(Long id)       | O(...)           | Description <br>`...` is ...
getReviewsContaining(String s)             | O(...)           | Description <br>`...` is ...

<div style="page-break-after: always;"></div>

## Util
### Overview
* **ConvertToPlace**
    * ...
* **DataChecker**
    * ...
* **HaversineDistanceCalculator (HaversineDC)**
    * ...
* **KeywordChecker**
    * ...
* **StringFormatter**
    * ...

### Space Complexity
Util               | Worst Case | Description
-------------------| ---------- | -----------
ConvertToPlace     | O(...)     | ...
DataChecker        | O(...)     | ...
HaversineDC        | O(...)     | ...
KeywordChecker     | O(...)     | ...
StringFormatter    | O(...)     | ...

### Time Complexity
Util              | Method                                                                             | Average Case     | Description
----------------- | ---------------------------------------------------------------------------------- | ---------------- | -----------
ConvertToPlace    | convert(float lat, float lon)                                                      | O(...)           | ...
DataChecker       | extractTrueID(String[] repeatedID)                                                 | O(...)           | ...
DataChecker       | isValid(Long id)                                                                   | O(...)           | ...
DataChecker       | isValid(Customer customer)                                                         | O(...)           | ...
DataChecker       | isValid(Favourite favourite)                                                       | O(...)           | ...
DataChecker       | isValid(Restaurant restaurant)                                                     | O(...)           | ...
DataChecker       | isValid(Review review)                                                             | O(...)           | ...
HaversineDC       | inKilometres(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2) | O(...)           | ...
HaversineDC       | inMiles(<br>&emsp; float lat1, float lon1, <br>&emsp; float lat2, float lon2)      | O(...)           | ...
KeywordChecker    | isAKeyword(String s)                                                               | O(...)           | ...
StringFormatter   | convertAccentsFaster(String s)                                                     | O(...)           | ...
