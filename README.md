# AtmoSphere Service Search and Filtering

To facilitate an effective and user-friendly search mechanism that allows users to 
quickly and accurately find products they are interested in, thus improving user 
satisfaction and engagement on the platform.

## API Documentation
The API documentation is provided by Swagger. 
To access the documentation, run the application and go to the following URL:
```
http://localhost:18080/api/swagger-ui/index.html
```

## Example Requests
```
curl --location 'http://localhost:18080/api/search?search=Pro'
```
```
curl --location 'http://localhost:18080/api/search?search=&sortBy=price&sortOrder=DESC'
```
```
curl --location 'http://localhost:18080/api/filter?category=Electronics'
```
```
curl --location 'http://localhost:18080/api/filter?condition=Used'
```
```
curl --location 'http://localhost:18080/api/filter?fromPrice=40&toPrice=80'
```
```
curl --location 'http://localhost:18080/api/search/3d884170-adb8-4823-9819-e2570269d10d'
```


## Business Requirements

### Search Functionality
- A prominent, easily accessible search bar on the products page.
- Support for keyword searches based on product titles or descriptions.
- [OPTIONAL] Modify the search query without starting over.

### Search Results Page
- Display of search results with product title, image as thumbnail, and price per row.
- List of found products with a maximum of 25 products.
- [OPTIONAL] Pagination of all found products with 25 products per page.
- [OPTIONAL] Auto-complete suggestions while typing keywords.

### Filter Options
- Filters based on categories and price range.
- [OPTIONAL] Filter by product condition (new/used) and location.
- [OPTIONAL] Ability to apply multiple filters simultaneously.
- [OPTIONAL] Modify the filter options without starting over.

### Sort Functionality
- Option to sort search results by price (ascending and descending)
- [OPTIONAL] Sort by listings creation date (ascending and descending).

### Search History and Saved Searches
- [OPTIONAL] Ability for users to view their recent search history.
- [OPTIONAL] Option to save frequent searches for easy future access.
