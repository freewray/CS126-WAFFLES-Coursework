package uk.ac.warwick.cs126.test;

import uk.ac.warwick.cs126.models.Favourite;
import uk.ac.warwick.cs126.stores.FavouriteStore;

public class TestTheFavouriteStore extends TestRunner {
    TestTheFavouriteStore(){
        System.out.println("\n[Testing FavouriteStore]");

        // Run tests, comment out if you want to omit a test, feel free to modify or add more.
        testAddFavourite();
        testAddFavourites();
        testGetFavourite();
        testGetFavourites();
        testGetFavouritesByCustomerID();
        testGetFavouritesByRestaurantID();
        testGetCommonFavouriteRestaurants();
        // testGetNotCommonFavouriteRestaurants();
        // testGetMissingFavouriteRestaurants();
        // testGetTopRestaurantsByFavouriteCount();
        // testGetTopCustomersByFavouriteCount();
    }

    private void testAddFavourite() {
        try {
            // Initialise new store
            FavouriteStore favouriteStore = new FavouriteStore();

            // Create a favourite object
            // Favourite(Long favouriteID,
            //           Long customerID,
            //           Long restaurantID,
            //           Date dateFavourited)
            Favourite favourite = new Favourite(
                    1112223334445556L,
                    1112223334445557L,
                    1112223334445558L,
                    parseDate("2020-04-30")
            );

            // Add to store
            boolean result = favouriteStore.addFavourite(favourite);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testAddFavourite()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testAddFavourite()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testAddFavourite()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testAddFavourites() {
        try {
            // Initialise new store
            FavouriteStore favouriteStore = new FavouriteStore();

            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed, should return true as all the data is valid
            boolean result = favouriteStore.addFavourite(favourites);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testAddFavourites()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testAddFavourites()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testAddFavourites()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetFavourite() {
        try {
            // Initialise new store
            FavouriteStore favouriteStore = new FavouriteStore();

            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Should return true as the favourite with ID 9845217889252669 exists
            boolean result = favouriteStore.getFavourite(9845217889252669L) != null
                    && favouriteStore.getFavourite(9845217889252669L).getID().equals(9845217889252669L);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testGetFavourite()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testGetFavourite()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetFavourite()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetFavourites() {
        try {
            // Initialise new store
            FavouriteStore favouriteStore = new FavouriteStore();

            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Favourite[] gotFavourites = favouriteStore.getFavourites();

            // Load manually sorted data from /data folder to verify with
            Favourite[] expectedfavourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10-sorted-by-id.csv"));

            // Now we compare
            boolean result = compareTables(expectedfavourites, gotFavourites);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testGetFavourites()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testGetFavourites()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetFavourites()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetFavouritesByCustomerID() {
        try {
            // Initialise new store
            FavouriteStore favouriteStore = new FavouriteStore();

            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Favourite[] gotFavourites = favouriteStore.getFavouritesByCustomerID(1797633434427591L);

            // Load manually sorted data from /data folder to verify with
            Favourite[] expectedfavourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10-sorted-by-customerid.csv"));

            boolean result = compareTables(expectedfavourites, gotFavourites);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testGetFavouritesByCustomerID()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testGetFavouritesByCustomerID()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetFavouritesByCustomerID()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetFavouritesByRestaurantID() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Favourite[] gotFavourites = favouriteStore.getFavouritesByRestaurantID(9517651597424422L);

            // Load manually sorted data from /data folder to verify with
            Favourite[] expectedfavourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10-sorted-by-restaurant-id.csv"));

            boolean result = compareTables(expectedfavourites, gotFavourites);

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testGetFavouritesByRestaurantID()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testGetFavouritesByRestaurantID()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetFavouritesByRestaurantID()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetCommonFavouriteRestaurants() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Long[] gotFavourites = favouriteStore.getCommonFavouriteRestaurants(1797633434427591L, 2649541714385159L);

            // Load manually sorted data from /data folder to verify with
            Long[] expectedfavourites = {9517651597424422L, 9986542917841834L};

            boolean result = false;
            if (gotFavourites.length == expectedfavourites.length) {
                for (int i = 0; i < expectedfavourites.length; i++) {
                    result = gotFavourites[i].equals(expectedfavourites[i]);
                    if (!result) {
                        break;
                    }
                }
            } else {
                result = false;
            }

            // Print if wrong
            if (!result) {
                System.out.println("\n[Expected]");
                for (Long f : expectedfavourites) {
                    System.out.println(f);
                }

                System.out.println("\n[Got]");
                if (gotFavourites.length == 0) {
                    System.out.println("You got nothing!");
                }

                for (Long f : gotFavourites) {
                    System.out.println(f);
                }

                System.out.println();
            }

            if (result) {
                System.out.println("[SUCCESS]    FavouriteStore: testGetCommonFavouriteRestaurants()");
            } else {
                System.out.println(" [FAILED]    FavouriteStore: testGetCommonFavouriteRestaurants()");
            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetCommonFavouriteRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetMissingFavouriteRestaurants() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Long[] missing = favouriteStore.getMissingFavouriteRestaurants(1797633434427591L, 2649541714385159L);

            System.out.println("FavouriteStore: testGetMissingFavouriteRestaurants()");
            for (int i = 0; i < missing.length; i++) {
                System.out.println(missing[i]);
            }

//            boolean result = false;
//
//            if (result) {
//                System.out.println("[SUCCESS]    FavouriteStore: testGetMissingFavouriteRestaurants()");
//            } else {
//                System.out.println(" [FAILED]    FavouriteStore: testGetMissingFavouriteRestaurants()");
//            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetMissingFavouriteRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetNotCommonFavouriteRestaurants() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Long[] unCommon = favouriteStore.getNotCommonFavouriteRestaurants(1797633434427591L, 2649541714385159L);

            System.out.println("FavouriteStore: getNotCommonFavouriteRestaurants()");
            for (int i = 0; i < unCommon.length; i++) {
                System.out.println(unCommon[i]);
            }

//            boolean result = false;
//
//            if (result) {
//                System.out.println("[SUCCESS]    FavouriteStore: testGetNotCommonFavouriteRestaurants()");
//            } else {
//                System.out.println(" [FAILED]    FavouriteStore: testGetNotCommonFavouriteRestaurants()");
//            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetNotCommonFavouriteRestaurants()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopCustomersByFavouriteCount() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Long[] res = favouriteStore.getTopCustomersByFavouriteCount();

            System.out.println("FavouriteStore: testGetTopCustomersByFavouriteCount()");
            for (int i = 0; i < res.length; i++) {
                System.out.println(res[i]);
            }

//            boolean result = false;
//
//            if (result) {
//                System.out.println("[SUCCESS]    FavouriteStore: testGetTopCustomersByFavouriteCount()");
//            } else {
//                System.out.println(" [FAILED]    FavouriteStore: testGetTopCustomersByFavouriteCount()");
//            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetTopCustomersByFavouriteCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private void testGetTopRestaurantsByFavouriteCount() {
        try {
            FavouriteStore favouriteStore = new FavouriteStore();
            // Load test data from /data folder
            Favourite[] favourites = favouriteStore.loadFavouriteDataToArray(
                    loadData("/test-favourite/favourite-10.csv"));

            // Add to store to be processed
            favouriteStore.addFavourite(favourites);

            // Get favourites sorted by ID from store
            Long[] res = favouriteStore.getTopRestaurantsByFavouriteCount();

            System.out.println("FavouriteStore: testGetTopRestaurantsByFavouriteCount()");
            for (int i = 0; i < res.length; i++) {
                System.out.println(res[i]);
            }

//            boolean result = false;
//
//            if (result) {
//                System.out.println("[SUCCESS]    FavouriteStore: testGetTopRestaurantsByFavouriteCount()");
//            } else {
//                System.out.println(" [FAILED]    FavouriteStore: testGetTopRestaurantsByFavouriteCount()");
//            }
        } catch (Exception e) {
            System.out.println(" [FAILED]    FavouriteStore: testGetTopRestaurantsByFavouriteCount()");
            e.printStackTrace();
            System.out.println();
        }
    }

    private boolean compareTables(Favourite[] expectedfavourites, Favourite[] gotFavourites) {
        boolean result = false;
        if (gotFavourites.length == expectedfavourites.length) {
            for (int i = 0; i < expectedfavourites.length; i++) {
                result = gotFavourites[i].getID().equals(expectedfavourites[i].getID());
                if (!result) {
                    break;
                }
            }
        } else {
            result = false;
        }

        // Print if wrong
        if (!result) {
            System.out.println("\n[Expected]");
            for (Favourite f : expectedfavourites) {
                System.out.println(f);
            }

            System.out.println("\n[Got]");
            if (gotFavourites.length == 0) {
                System.out.println("You got nothing!");
            }

            for (Favourite f : gotFavourites) {
                System.out.println(f);
            }

            System.out.println();
        }

        return result;
    }

}
