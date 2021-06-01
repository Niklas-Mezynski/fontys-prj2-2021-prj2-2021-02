# Use Cases

- Search for Flight: Nils
- View FlightOptions: Nils
- Start sales Process Luca
- Stop sales process Luca
- book tickets Luca
- Edit PriceReduction : Philip
- Create PriceReduction : Philip
- edit flight options: Philip
- Create FlightLeg Stops: Anatol (for removal)
- Create Flight Option: Anatol
- remove flight options: Anatol
- View KPIs: Niklas
- Remove Price Reductions: Niklas
- Cancel tickets: Eric
- Create a flight
- Login
- Logout

***TODO***:

- Register?


## Create Route

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Create a Route</td>
</tr>
<tr>
<td>
<b>Pre-condition</b>
</td>
<td>
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor chooses to add a new Route
</li>
<li>
System asks for arrival and departure airport
</li>
<li>
Actor gives arrival and departure airport
</li>
<li>
System asks for confirmation
</li>
<li>
Actor confirms given information
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor has added a new route
</td>
</tr>
<tr>
<td>
<b>Exceptions</b>
</td>
<td>
5. One or more given airports are incorrect
</td>
</tr>
</table>

## Edit Route

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Edit a Route</td>
</tr>
<tr>
<td>
<b>Pre-condition</b>
</td>
<td>
The Route to be edited was created
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor chooses to edit a new Route
</li>
<li>
System gives opportunity to edit arrival and departure airport
</li>
<li>
Actor changes arrival and departure airport to desire
</li>
<li>
System asks for confirmation
</li>
<li>
Actor confirms given information
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor has edited a route
</td>
</tr>
<tr>
<td>
<b>Exceptions</b>
</td>
<td>
5. One or more given airports are incorrect
</td>
</tr>
</table>

## Remove Route

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Remove a Route</td>
</tr>
<tr>
<td>
<b>Pre-condition</b>
</td>
<td>
Route to be deleted was already created
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor chooses to delete a Route
</li>
<li>
System asks for confirmation
</li>
<li>
Actor confirms his intention
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor has deleted a route
</td>
</tr>
</table>

## Search for Flight

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Search for a flight</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor chooses to search for a flight
</li>
<li>
System offers a way to enter the search term
</li>
<li>
Actor enters the search term and submits
</li>
<li>
System shows the result of the search
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor is shown the search result and can do stuff with the
information displayed
</td>
</tr>
</table>

## Book Tickets
<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to sell Tickets to a Customer</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor <ins>searches for flight</ins>
</li>
<li>
System shows available flights
</li>
<li>
Actor selects a flight
</li>
<li>
Systems shows available Seats and seat options
</li>
<li>
Actor selects required seat options
</li>
<li>
System updates seats, to match selected seat options
</li>
<li>
Actor choses seat
</li>
<li>
System requests names of the passengers and Email for contacting
</li>
<li>
Actor provides names of passengers and gives email
</li>
<li>
System shows <ins>available Flight options</ins>
</li>
<li>
Actor selects requsted flight Options
</li>
<li>
System gives overview of booking
</li>
<li>
Actor confirms booking
</li>
<li>
System prints the tickets and returns to main page
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Exception</b></td>
<td>


</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Customer receives the Tickets from the Actor and the booking is created
</td>
</tr>
</table>

## Start sales process

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to start the sales process</td>
</tr>
<tr>
<td>
Pre-condition
</td>
<td>
<ins>Flight created</ins> and open after <ins>the 
flight was searched for</ins>
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
<ins>Actor searches for flight</ins>
</li>
<li>
System shows available flight
</li>
<li>
Actor selects to start the sales process
</li>
<li>
System asks for confirmation to start the sale
</li>
<li>
Actor confirms
</li>
<li>
System saves change and sends back to start page
</li>
</ol>
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Sales process for the Flight has started
</td>
</tr>
<tr>
<td>
<b>
Extensions
</b>
</td>
<td>

4. Actor denies

	1. cancel process and return to step 2
	 
</td>
</tr>
</table>

## Stop sales process

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to stop the sales process</td>
</tr>
<tr>
<td>
Pre-condition
</td>
<td>
<ins>Flight created</ins> and open after <ins>the 
flight was searched for</ins>
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. <ins>Actor searches for flight</ins>

2. System shows available flight
   
3. Actor selects to stop the sales process
   
6. System asks for confirmation to stop the sales process
   
7. Actor confirms

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Sales process for the Flight has started
</td>
</tr>
<tr>
<td>
<b>
Extensions
</b>
</td>
<td>

7. Actor denies 
   
	7.1 System sends the Actor back to start page
</td>
</tr>
</table>

## View additional FlightOptions

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to view FlightOptions for a Flight</td>
</tr>
<tr>
<td>
Pre-condition
</td>
<td>
<ins>Flight created</ins> and open after <ins>the 
flight was searched for</ins>
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor opens the overview page for the flight
   
2. System show the flight overview
   
3. Actor chooses to view additional FLightOptions
   
4. System shows the FlightOptions


</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor sees the FlightOptions
</td>
</tr>
</table>

## Edit PriceReductions

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Edit existing PriceReduction</td>
</tr>
<tr>
<td>
Pre-condition
</td>
<td>
<ins>Flight created</ins> and open after <ins>the 
flight was searched for</ins>
</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor chooses a <ins>created PriceReduction<ins>
   
2. System displays selected PriceReduction
   
3. Actor edits the PriceReduction
   
4. Actor submits changes
   
5. System applies the changes and display modified
   PriceReduction

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
PriceReduction is edited and saved.
</td>
</tr>
</table>

## Cancel Tickets

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Cancel a Booked Ticket</td>
</tr>
<tr>
<td>
Pre-condition
</td>
<td>

</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor searches by FlightNo and/or by email and selects the booking to be canceled
   
2. System provides overview of the booking

3. Actor chooses to cancel the selected booking

4. System asks for confirmation

5. Actor confirms cancelation

6. System cancels the selected booking

</td>

</tr>
<tr>
<td>
<b>Exceptions</b>
</td>
<td>
3. No booking is selected

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
The entire Booking is cancelled incl its tickets
</td>
</tr>
</table>

## View Key Performance Indicators

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Manager</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Sales Manager wants to see some Key Performance Indicatorss in order to see which Routes/Employees perform best.</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor enters the Management Dashboard
   
2. System shows options to see Route or Employee KPIs
   
3. Actor selects an option
   
4. System offers a search for a specific Route/Employee
   
5. Actor selects a Route/Employee
   
6. System shows a few KPIs for this Route/Employee
	
7. Actor selects option to show a Diagram which shows the Revenue over time
	
8. System shows the diagram

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Manager gets an overview about the performance of different
routes or flight options
</td>
</tr>
</table>


## Create PriceReductions

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor creates a new PriceReduction for a specific flight</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor chooses a <ins>created Flight<ins>
   
2. System displays selected Flight and offers to add a new PriceReduction
   
3. Actor chooses the type of the new PriceReduction and its content for the flight
   
4. Actor submits the PriceReduction
   
5. System applies the PriceReduction and displays the updated flight

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
A new PriceReduction is created and applied to a Flight
</td>
</tr>
</table>

## Remove PriceReductions

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor removes an existing PriceReduction for a specific flight.</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor chooses a Flight with an existing PriceReduction<ins>
   
2. System displays selected Flight and offers the option to remove the PriceReduction
   
3. Actor chooses to remove the flight
   
4. System informs the Actor that the PriceReduction has been removed.

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
The PriceReduction has been removed, and the price of the
flight is at its initial price again
</td>
</tr>
</table>

## Edit FlightOption

<table>
<tr>
<td><b>Actor</b></td>
<td>Sales Officer</td>
</tr>
<tr>
<td><b>Description</b></td>
<td>Actor accesses an existing FlightOption to modify it</td>
</tr>
<tr>
<td><b>Pre-Condition</b></td>
<td><ins>create FlightOption</ins></td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1.  Actor selects a <ins>created FlightOption<ins>
    
2.  System displays selected FlightOption and its content
    
3.  Actor modifies specific attributes
    
4.  Actor submits changes
    
5.  System applies changes and displays the updated 
    FlightOption
</td>
</tr>
<tr>
<td><b>Results</b></td>
<td>The selected FlightOption is modified</td>
</tr>
</table>


## Create FlightLeg Stops

<table>
<tr>
<td><b>Actor</b></td>
<td>Sales Officer</td>
</tr>
<tr>
<td><b>Description</b></td>
<td>Actor adds a FlightLeg to an Existing Route</td>
</tr>
<tr>
<td><b>Pre-Condition</b></td>
<td>Actor is logged in as SalesOfficer and accesses an already existing route</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. System gives opportunity to enter the destination of the FlightLeg
   
2. Actor gives the new destination
   
3. System gives opportunity to put the new destination in the wanted order
   
4. User indicates, where to put the new destination
</td>
</tr>

<tr>
<td><b>Results</b></td>
<td>The Actor has added a new FlightLeg to an existing Route</td>
</tr>
</table>

## Create FlightOption


<table>
<tr>
<td><b>Actor</b></td>
<td>Sales Officer</td>
</tr>
<tr>
<td><b>Description</b></td>
<td>Actor adds a FlightOption to an existing Flight</td>
</tr>
<tr>
<td><b>Pre-Condition</b></td>
<td>Actor is logged in as SalesOfficer and accesses an already existing Flight</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. System asks for name of the new FlightOption
   
2. Actor gives name for the FlightOption
   
3. System gives the option to select for which seats this option is available
   
4. Actor selects the wanted seats
   
5. System gives opportunity to set the price for this option
   
6. Actor gives price for the option
   
7. System gives overview of the option, and asks to submit
   
8. Actor submits

</td>
</tr>
<tr>
<td><b>Exceptions</b></td>
<td>

2. Name for the FlightOption already exists

</td>
</tr>
<tr>
<td><b>Results</b></td>
<td>A Flight option has been created and added to an existing flight</td>
</tr>
</table>

## Remove FlightOption


<table>
<tr>
<td><b>Actor</b></td>
<td>Sales Officer</td>
</tr>
<tr>
<td><b>Description</b></td>
<td>Actor can remove a FlightOption from a Flight</td>
</tr>
<tr>
<td><b>Pre-Condition</b></td>
<td>Actor is logged in as Sales Officer and has selected an existing Flight</td>
</tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. System gives Overview of all registered Flight Options for selected Flight
   
2. Actor selects the Option, which should be deleted
   
3. System asks Actor to confirm the deletion
   
4. Actor Confirms the deletion
</td>
</tr>
<tr>
<td><b>Results</b></td>
<td>FlightOption has been deleted from an existing flight</td>
</tr>
</table>



## Create a flight

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to add a new Flight to the System</td>
</tr>
<tr>
        <td><b>Pre-condition</b></td>
        <td>Route of the flight already exists</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor selects option to create a new flight.

2. Systems offers a search for the different flight routes.

3. Actor chooses a flight route.

4. System asks the actor to enter start date and time.

5. Actor enters start date and time.

6. System asks if all values are correct.

7. Actor confirms the values

8. System adds the flight to the system.
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
A new flight has been registered in the system.
</td>
</tr>
<tr>
<td><b>Extension</b></td>
<td>

3. Actor wants to create a flight route.    
    1.  Use case <ins>create route</ins>
    2. Continue at step 4.

7. Actor does not confirm the values
    1. Return to step 4.
</td>
</tr>
</table>



## Edit flight

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to edit an existing Flight</td>
</tr>
<tr>
        <td><b>Pre-condition</b></td>
        <td>Flight already exists</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor selects option to edit a flight.

2. Systems offers a search for the different flights.

3. Actor chooses a flight.

4. System asks the actor to edit the flight data.

5. Actor changes the data.

6. System asks if all values are correct.

7. Actor confirms the values.

8. System edits the flight in the system.
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
The flight has been edited.
</td>
</tr>
<tr>
<td><b>Extension</b></td>
<td>

7. Actor does not confirm the values
    1. Return to step 4.
</td>
</tr>
</table>



## Remove flight

<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>Actor wants to remove an existing Flight</td>
</tr>
<tr>
        <td><b>Pre-condition</b></td>
        <td>Flight process was not started yet.</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>

1. Actor selects option to remove a flight.

2. Systems offers a search for the different flights.

3. Actor chooses a flight.

4. System asks the actor if he wants to remove the flight.

5. Actor confirms.

6. System removes the flight from the system.
</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
The flight has been removed.
</td>
</tr>
<tr>
<td><b>Exception</b></td>
<td>

5. Sales process already started

    1. System shows error message

    2. Return to step 1.
</td>
</tr>
</table>

## Logout
<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee, Sales Officer, Sales Manager</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>The Actor is about to logout and leave the application</td>
    </tr>
    <tr>
        <td><b>Pre-condition</b></td>
        <td>Actor is already logged in</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor enters to log him-/herself out
</li>
<li>
System notifies actor about logout
</li>
<li>
 Actor is located on the login-screen
</li>
</ol>
</td>

</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor is logged out and not able to access the application without logging in
</td>
</tr>
</table>


## Log-in
<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Employee, Sales Officer, Sales Manager</td>
   </tr>
    <tr>
      <td><b>Description</b></td><td>The Actor logs into his/her personal account</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor enters e-mail address
</li>
<li>
Actor enters password that matches email
</li>
<li>
System checks logon data
</li>
</ol>
</td>
<tr>
<td><b>Extension</b></td>
<td>

3. No account with the logon data

    3.1 System notifies User about incorrect input and is 
   ready for new input

</td>
</tr>
<tr>
<td><b>Result</b></td>
<td>
Actor is logged in and granted access onto several functions
</td>
</tr>
</table>



## Create Plane
<table>
   <tr>
      <td><b>Actor</b></td><td>Sales Officer</td>
   </tr>
	<tr>
      <td><b>Description</b></td><td>The Actor is about to create a new plane that can be used for flights</td>
    </tr>
    <tr>
        <td><b>Pre-condition</b></td>
        <td>Actor is already logged in</td>
    </tr>
<tr>
<td><b>Scenario</b></td>
<td>
<ol>
<li>
Actor chooses to create a new plane
</li>
<li>
System offers to add seatrows/-columns
</li>
<li>
Actor determines the amount of rows and seats in total
</li>
<li>
System requires other plane information
</li>
<li>
Actor enters specific information and confirms
</li>
<li>
System saves the new plane and its characteristics
</li>
</ol>
</td>

</tr>
<tr>
<td><b>Result</b></td>
<td>
A new plane is created and can be edited, removed or used for a specific flight
</td>
</tr>
</table>
