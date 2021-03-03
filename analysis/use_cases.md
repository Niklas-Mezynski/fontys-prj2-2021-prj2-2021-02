# Use Cases

- Search for Flight: Nils
- View FlightOptions: Nils
- Start sales Process Luca
- Stop sales process Luca
- book tickets Luca
- Edit PriceReduction : Philip
- Create PriceReduction : Philip
- edit flight options: Philip
- Create FlightLeg Stops: Anatol
- Create Flight Option: Anatol 
- remove flight options: Anatol  
- View KPIs: Niklas
- Remove Price Reductions: Niklas
- Cancel tickets: Eric 

- Create a flight ??


todo:



## Search for Flight

### Actor:
Sales Employee

### Description:
Search for a flight

### Scenario
1. Actor chooses to search for a flight
2. System offers a way to enter the search term
3. Actor enters the search term and submits
4. System shows the result of the search

### Result:
Actor is shows the search result and can do stuff 
with the information displayed



## Book Tickets

### Actor:
Sales Employee

### Description:
Actor wants to sell Tickets to a Customer

### Pre-condition:
none

### Scenario:
1. Actor <ins>searches for flight</ins>
2. System shows available flights
3. Actor selects a flight
4. System shows <ins>available flight options</ins>
5. Actor selects flight options and books
6. System books the tickets and prints the tickets

### Result:
Customer receive the Tickets from the Actor

### Exceptions:



## Start sales process

### Actor:
Sales Officer

### Description:
Actor wants to start the sales process

### Pre-condition:
<ins>Create a flight</ins>

### Scenario:
1. <ins>Actor searches for flight</ins>
2. System shows available flight
3. Actor selects to start the sales process
4. System asks for a start and stop date
5. Actor enters dates
6. System asks for confirmation to start the sale
7. Actor confirms
8. System saves change and sends back to start page

### Result:
Sales process for the Flight has started

### Extensions:
7. Actor denies

8. System asks if he wants to change or to delete
   changes

   1. Actor wants to change the dates

      1. System asks for other dates to change to

      2. Actor sets new dates

      3. Back to 7

   1. Actor wants to delete changes

      1. System sends the Actor back to start page



## Stop sales process

### Actor:
Sales Officer

### Description:
Actor wants to stop the sales process

### Pre-condition:
<ins>Create a flight</ins>

### Scenario:
1. <ins>Actor searches for flight</ins>
2. System shows available flight
3. Actor selects to stop the sales process
6. System asks for confirmation to stop the sales process
7. Actor confirms

### Result:
Sales process for the Flight has started

### Exceptions:
7. Actor denies
7.1 System sends the Actor back to start page



## View additional FlightOptions

### Actor:
Sales Employee

### Description:
Actor wants to view FlightOptions for a Flight

### Pre-condition:
Actor has <ins>searched for a flight</ins>

### Scenario:
1. Actor opens the overview page for the flight
2. System show the flight overview
3. Actor chooses to view additional FLightOptions
4. System shows the FlightOptions

### Result:
Actor sees the FlightOptions

### Exceptions:



## Edit PriceReductions

### Actor:
Sales Officer

### Description:
Edit existing PriceReduction

### Scenario
1. Actor chooses a <ins>created PriceReduction<ins>
2. System displays selected PriceReduction
3. Actor edits the PriceReduction
4. Actor submits changes
5. System applies the changes and display modified PriceReduction

### Result:
PriceReduction is edited and saved.



## Cancel Tickets

### Actor:
Sales Employee

### Description:
Chancel a Booked Ticket

### Pre-Condition:
Actor has selected the Flight for which the Ticket 
should be canceled

### Scenario:
1. Actor selects the Seat for which the Ticket is valid
2. Actor removes the Ticket and the Booking which it is 
   a Part of
   
### Results:
Ticket and Booking is canceled

### Exceptions:
2. Actor is not the same Actor that booked the Ticket



## View Key Performance Indicators

### Actor:
Sales Manager

### Description:
Sales Manager wants to see different KPIs in order to see which flights perform best.

### Pre-condition:
none

### Scenario:
1. Actor selects section for KPIs
2. System shows a few general KPIs
3. Actor selects the filter section
4. System offers filter options
5. Actor selects a filter
6. System shows the KPIs for these filters

### Result:
Manager gets an overview about the performance of different routes or flight options



## Create PriceReductions

### Actor:
Sales Officer

### Description:
Actor creates a new PriceReduction for a specific flight

### Scenario
1. Actor chooses a <ins>created Flight<ins>
2. System displays selected Flight and offers to add a new PriceReduction
3. Actor chooses the type of the new PriceReduction and its content for the flight
4. Actor submits the PriceReduction
5. System applies the PriceReduction and displays the updated flight

### Result:
A new PriceReduction is created and applied to a Flight



## Remove PriceReductions

### Actor:
Sales Officer

### Description:
Actor removes an existing PriceReduction for a specific flight.

### Scenario
1. Actor chooses a Flight with an existing 
   PriceReduction<ins>
2. System displays selected Flight and offers the option to remove the PriceReduction
3. Actor chooses to remove the flight
4. System informs the Actor that the PriceReduction has been removed.

### Result:
The PriceReduction has been removed, and the price of 
the flight is at its initial price again



## Edit FlightOption

### Actor:
Sales Officer

### Description:
Actor accesses an existing FlightOption to modify it

### Pre-condition:
<ins>create FlightOption<ins>

### Scenario
1. Actor selects a <ins>created FlightOption<ins>
2. System displays selected FlightOption and its content
3. Actor modifies specific attributes
4. Actor submits changes
5. System applies changes and displays the updated FlightOption

### Result:
The selected FlightOption is modified