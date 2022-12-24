# SneakerShip
Android sample code for an Sneaker app.

### Features:
- Home Page
  - Shows a list of sneakers with there image, name and price
  - You can add the sneaker to cart or view details
  
- Details Page
  - Shows the following data
    - Image
    - Name
    - Release Date
    - Retail Price
    - Description
    - color
  - Has a add to cart button, which adds the sneaker to your cart

- Cart
  - Shows you following info
    - List of sneakers added to cart
    - Subtotal
    - Tax and Charges
    - Total
  - Has a Check out button to check-out your cart.

### About the App:
```
This repository is all about sample app showcasing the sneakers. Following are the major libraries used
- Paging : To load the data seamlessly
- Navigation : To Navigate between different fragments
- Coil : For image download and caching
- Room : For all DB related functionality (Which is very minimum)

The app is rough around the edges and has still room for improvements.
Have used a shared ViewModel for most of the data modification.
```
### Architecture

- Used Plain MVVM architecture.

### Learning's : 

- Learnt a bit about flows.
- How the paging works.
- Navigation.
- Writing Room DB queries.

