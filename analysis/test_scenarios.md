## Test Scenarios

<table>
<tr><th>UseCase</th><th>TestCase Description</th><th>TestCase Steps</th><th>Result</th></tr>

<tr>
<td><!-- UseCase Name-->Search For Flight</td>
<td><!-- TestCaseDescription-->Actor searches for a flight to Barcelona</td>
<td>

1. Actor chooses the search flight option

2. Systems offers a way to enter the search term

3. Actor enters "Barcelona" as Destination

4. System lists upcoming flights with Barcelona as arrival airport


</td>
<td><!--Expected Result-->System shows all upcoming flights to Barcelona</td>
</tr>

<tr>
<td><!-- UseCase Name-->View FlightOptions</td>
<td><!-- TestCaseDescription-->Actor wants to see which FlightOptions are available  for a specific flight from "Frankfurt" to "New York"</td>
<td>

1. Actor clicks on a flight from "Frankfurt" to "New York"

2. System shows the overview for this flight

3. Actor chooses to view aditional FlightOptions

4. System shows amount of business class seats, amount of first class seats, amount of extra meals left (for this specific flight)


</td>
<td><!--Expected Result-->Actor sees how many FlightOtions still are available</td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process (Normal case)</td>
<td><!-- TestCaseDescription-->Sales Officer wants to start the sales process of flight "XAX-01B"</td>
<td>

1. Actor <u>searches for flight</u> "XAX-01B"

2. System lists this flight

3. Actor selects the flight

4. System asks for start and end date for the sales prcoess

5. Actor enters "20.04.2021" as start date and "20.06.2021" as end date

6. System asks to confirm these dates

7. Actor confirms

8. System saves the change


</td>
<td><!--Expected Result-->Flight is now available for booking</td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process (Extension 7.i)</td>
<td><!-- TestCaseDescription--></td>
<td>

1. Actor chooses to correct the dates

2. Actor chooses "20.07.2021" as end date instead

3. Actor confirms

4. System saves the change

</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process (Extension 7.ii)</td>
<td><!-- TestCaseDescription--></td>
<td>

1. Actor chooses to cancel the whole process.

2. System confirms.

</td>
<td><!--Expected Result-->The sales process hasn't been started</td>
</tr>

<tr>
<td><!-- UseCase Name-->Stop Sales Process (Normal case)</td>
<td><!-- TestCaseDescription-->Actor wants to stop the sales process of flight "XAX-01B"</td>
<td>

1. Actor <u>searches for flight</u> "XAX-01B"

2. System lists this flight

3. Actor chooses to stop the flight process for this flight.

4. System asks for confirmation

5. Actor confirms.

6. System stops the sales process



</td>
<td><!--Expected Result-->Flight cannot be booked anymore</td>
</tr>

<tr>
<td><!-- UseCase Name-->Stop Sales Process (Extension 5.)</td>
<td><!-- TestCaseDescription--></td>
<td>

1. Actor denies

2. System does not stop the sales process.

</td>
<td><!--Expected Result-->Flight can still be booked.</td>
</tr>

<tr>
<td><!-- UseCase Name-->Book tickets (Normal case)</td>
<td><!-- TestCaseDescription-->Sales employee wants to book a ticket for flight "XAX-01B" with the seat Option "business class" and flight option "Tomatensaft"</td>
<td>

1. Actor searches for a flight

2. System shows available flight for booking

3. Actor chooses flight for the booking

4. System shows all unbooked seats

5. Actor selects the filters for the desired seatoption

6. System shows unbooked seats and those matching the filter

7. Actor selects random selectable seat and confirms.

8. System asks for passenger names and contacting email

9. Actor enters names and the email and confirms

10. System shows available flight options

11. Actor selects flight option "Tomatensaft" with Quantity 1

12. System shows overview of the booking

13. Actor confirms booking
</td>
<td><!--Expected Result-->Customer has his ticket for flight "XAX-01B" with a business class seat and 1x Tomatensaft</td>
</tr>

<tr>
<td><!-- UseCase Name-->Book tickets with no options</td>
<td><!-- TestCaseDescription-->Employee wants to add another flight with id "X1B-406"</td>
<td>

1. Actor searches for a flight

2. System shows available flight for booking

3. Actor chooses flight for the booking

4. System shows all unbooked seats

5. Actor selects no filters for seatoptions

6. System shows all unbooked seats and 

7. Actor selects random selectable seat and confirms.

8. System asks for passenger names and contacting email

9. Actor enters names and the email and confirms

10. System shows available flight options

11. Actor selects no flight option

12. System shows overview of the booking

13. Actor confirms booking

</td>
<td><!--Expected Result-->Customer now has a booking with no options at all</td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit PriceReduction</td>
<td><!-- TestCaseDescription-->SalesOfficer edits static price reduction</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight with static price reduction</li>
<li>Select to edit the price reduction</li>
<li>give the new Percentage</li>
<li>Submit change</li>
</ol>
</td>
<td><!--Expected Result-->Static price reduction has been changed</td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit PriceReduction</td>
<td><!-- TestCaseDescription-->SalesOfficer edits static price reduction</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight with static price reduction</li>
<li>??</li>
</ol>
</td>
<td><!--Expected Result-->Dynamic pricereduction has ben edited</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Price Reduction</td>
<td><!-- TestCaseDescription-->SalesOfficer adds a static pricereduction to an existing flight</td>
<td>
<ol>
<!--Steps-->
<li>Select flight, to which the pricereduction should be added</li>
<li>Select option to add a price reduction</li>
<li>Select reduction type: Static</li>
<li>Give percentage of reduction</li>
<li>Submit price reduction</li>
</ol>
</td>
<td><!--Expected Result-->Static price reduction has been added</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Price Reduction</td>
<td><!-- TestCaseDescription-->SalesOfficer adds a dynamic pricereduction to an existing flight</td>
<td>
<ol>
<!--Steps-->
<li>Select flight, to which the pricereduction should be added</li>
<li>??</li>
</ol>
</td>
<td><!--Expected Result-->Dynamic price reduction has been added</td>
</tr>







<tr>
<td><!-- UseCase Name-->Edit flight Options</td>
<td><!-- TestCaseDescription-->Sales Officer selects and modifies a Flight Option</td>
<td>
<ol>
<!--Steps-->
<li>Select existing Flight</li>
<li>Select existing Flight Option to be edited</li>
<li>modify certain attributes</li>
<li>Submit Changes</li>
</ol>
</td>
<td><!--Expected Result-->Selected Flight Option has been modified</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create FlightLeg Stops</td>
<td><!-- TestCaseDescription-->Sales Officer adds FlightLeg to existing Route</td>
<td>
<ol>
<!--Steps-->
<li>Select Route, to which a FlightLeg should be added</li>
<li>Enter destination of FlightLeg</li>
<li>Select Position of FlightLeg</li>
<li>Submit</li>
</ol>
</td>
<td><!--Expected Result-->FlightLeg has been added to Route</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Flight Option</td>
<td><!-- TestCaseDescription-->SalesOfficer adds Flight Option to existing Flight</td>
<td>
<ol>
<!--Steps-->
<li>Select FLight, to which to add a flightOption</li>
<li>Select to add FlightOption</li>
<li>Enter not existing name of Flight Option</li>
<li>Select Seats, that should have this option</li>
<li>Set price for new option</li>
<li>Check and Submit</li>
</ol>
</td>
<td><!--Expected Result-->The Flight Option has been added to that Flight</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Flight Option</td>
<td><!-- TestCaseDescription-->SalesOfficer adds Flight Option with an already existing name to existing Flight</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight, to which to add a flightOption</li>
<li>Select to add FlightOption</li>
<li>Enter already existing name of Flight Option</li>
<li>Select Seats, that should have this option</li>
<li>Set price for new option</li>
<li>Check and Submit</li>
</ol>
</td>
<td><!--Expected Result-->An Exception has been thrown, because the FlightOption name already exists for that flight</td>
</tr>

<tr>
<td><!-- UseCase Name-->Remove Flight Option</td>
<td><!-- TestCaseDescription-->SalesOfficer removes FlightOption of a flight</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight, from which a FlightOption should be deleted</li>
<li>Select Flight Option to be deleted</li>
<li>Confirm deletion</li>
</ol>
</td>
<td><!--Expected Result-->The selected FlightOption should be removed from the Flight</td>
</tr>

<tr>
<td><!-- UseCase Name-->View KPIs</td>
<td><!-- TestCaseDescription-->SalesManager wants to see KPIs for employee 'Snens'</td>
<td>

1. Actor selects option to view KPIs for employees
  
2. System offers a table with allemployees
  
3. Actor selects Employee 'Snens'.
  
4. System shows that the total Revenue by this employe, the total number of bookings he made and
  the avg amount of tickets sold in one booking. (e.g. 20€, 4 total bookings and an average ticket amount of 1.25).
  
</td>
<td><!--Expected Result-->Actor can see the statistics for 'Snens'</td>
</tr>

<tr>
<td><!-- UseCase Name-->Remove Price Reduction</td>
<td><!-- TestCaseDescription-->Sales Officer removes a pricereduction</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight with a price reduction</li>
<li>Select Delete PriceReduction of the wanted PriceReduction</li>
<li>Confirm Deletion</li>
</ol>
</td>
<td><!--Expected Result-->PriceReduction has been removed</td>
</tr>

<tr>
<td><!-- UseCase Name-->Cancel Tickets</td>
<td><!-- TestCaseDescription-->Sales Employee can cancel a ticket</td>
<td>
<ol>
<!--Steps-->
<li>Select Flight, of the ticket</li>
<li>Select the Seat of the ticket</li>
<li>Select to cancel this ticket</li>
</ol>
</td>
<td><!--Expected Result-->The ticket is not valid for the selected flight anymore</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create a flight</td>
<td><!-- TestCaseDescription-->SalesOfficer can create a flight </td>
<td>
<ol>
<!--Steps-->
<li>Select Option to create a flight</li>
<li>Search and Select the Route</li>
<li>Enter start and end time/date</li>
<li>Confirm flight</li>
</ol>
</td>
<td><!--Expected Result-->Flight has been added with the correct data</td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit a flight</td>
<td><!-- TestCaseDescription-->Sales officer wants to edit an existing Flight</td>
<td>

1. Actor selects option to edit a flight

2. System offers search function

3. Actor searches for a flight to Düsseldorf

4. System asks actor to edit the data

5. Actor increases start and arrival date by "1 day"

6. System asks if all values are correct

6. Actor confirms the values

8. System edits the flight in the system

</td>
<td><!--Expected Result-->Flight has been edited with the correct data</td>
</tr>


<tr>
<td><!-- UseCase Name-->Remove a flight</td>
<td><!-- TestCaseDescription-->Sales officer wants to remove an existing Flight</td>
<td>

1. Actor selects option to remove a flight

2. System offers search function

3. Actor searches for a flight to Düsseldorf

4. System asks actor if he wants to remove the flight

5. Actor confirms

6. System removes the flight from the system

</td>
<td><!--Expected Result-->Flight has been removed from the system</td>
</tr>


<tr>
<td><!-- UseCase Name-->Login</td>
<td><!-- TestCaseDescription-->Employee can log in with his account</td>
<td>
<ol>
<!--Steps-->
<li>Enter correct E-Mail and Password and confirm</li>
</ol>
</td>
<td><!--Expected Result-->Employee has been logged on</td>
</tr>

<tr>
<td><!-- UseCase Name-->Login</td>
<td><!-- TestCaseDescription-->Employee can not log in with his wrong account data</td>
<td>
<ol>
<!--Steps-->
<li>Enter wrong E-Mail andOr Password and confirm</li>
</ol>
</td>
<td><!--Expected Result-->Employee has not been logged on, and was notified, that Password and Email do not correspond</td>
</tr>

<tr>
<td><!-- UseCase Name-->Logout</td>
<td><!-- TestCaseDescription-->Employee can log out</td>
<td>
<ol>
<!--Steps-->
<li>Select Log-Out</li>
</ol>
</td>
<td><!--Expected Result-->Employee has been logged out and sees the login Screen</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Route</td>
<td><!-- TestCaseDescription-->Sales Officer can create a new Route</td>
<td>
<ol>
<!--Steps-->
<li>Select function to add a new Route</li>
<li>Enter existing Airports as departure and arrival airport</li>
<li>Confirm information</li>
</ol>
</td>
<td><!--Expected Result-->Route has been created</td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Route</td>
<td><!-- TestCaseDescription-->Sales Officer can not create a new Route if Airport does not exist</td>
<td>
<ol>
<!--Steps-->
<li>Select function to add a new Route</li>
<li>Enter at least one <b>not-existing</b> Airport as departure and arrival airport</li>
<li>Confirm information</li>
</ol>
</td>
<td><!--Expected Result-->Route has not been created</td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit Route</td>
<td><!-- TestCaseDescription-->Sales Office can edit an existing Route</td>
<td>
<ol>
<!--Steps-->
<li>Select the function to edit routes</li>
<li>Select desired Route to be edited</li>
<li>Change departure and/or arrival airport to an existing airport</li>
<li>Confirm Changes</li>
</ol>
</td>
<td><!--Expected Result-->Route has been edited</td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit Route</td>
<td><!-- TestCaseDescription-->Sales Officer can not edit an existing Route if new Airport doesnt exist</td>
<td>
<ol>
<!--Steps-->
<li>Select the function to edit routes</li>
<li>Select desired Route to be edited</li>
<li>Change departure and/or arrival airport to an <b>not-existing</b> airport</li>
<li>Confirm Changes</li>
</ol>
</td>
<td><!--Expected Result-->Route has not been edited</td>
</tr>

<tr>
<td><!-- UseCase Name-->Remove Route</td>
<td><!-- TestCaseDescription-->Sales Officer can remove an existing Route</td>
<td>
<ol>
<!--Steps-->
<li>Select function to delete Routes</li>
<li>Select desired Route</li>
<li>Confirm deletion</li>
</ol>
</td>
<td><!--Expected Result--> Desired Route was removed</td>
</tr>


<tr>
<td>Create Plane</td>
<td>Sales Officer creates a new Plane</td>
<td>
<ol>
<!--Steps-->
<li>Chooses to add a new plane</li>
<li>Enter the name "D-ABCD"</li>
<li>Enter the type "A380"</li>
<li>Enter the manufacturer "Airbus"</li>
<li>Add four seats in two rows</li>
<li>Sales Officers adds a seat option called "First Class"</li>
<li>Newly created seat option is added to the two seats in the first row</li>
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name--></td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li>Selecting to save the plane</li>
</ol>
</td>
<td><!--Expected Result-->Plane is saved</td>
</tr>

<tr>
<td>Edit Plane</td>
<td>Sales Officer successfully edits a previously created plane</td>
<td>
<ol>
<!--Steps-->
<li>Chooses an plane to edit</li>
<li>Change the name to "D-BCDE"</li>
<li>Change the type to "747"</li>
<li>Change manufacturer to "Boeing</li>
<li>Choosing to save the changes</li>
</ol>
</td>
<td>Changes are saved and no error appears</td>
</tr>

<tr>
<td>Edit Plane</td>
<td>Sales Officer successfully edits a previously created plane and adds new seats with a new seat option</td>
<td>
<ol>
<!--Steps-->
<li>Chooses an plane to edit</li>
<li>A new row with three seats is added</li>
<li>Add a new seat option called "Extra Leg Room"</li>
<li>Add the new seat option to the newly added three seats</li>
<li>Choosing to save the changes</li>
</ol>
</td>
<td>Changes are saved and no error appears</td>
</tr>

<tr>
<td>Delete Plane</td>
<td>Sales Officer successfully deletes a plane that is not used in any flights</td>
<td>
<ol>
<!--Steps-->
<li>Chooses an plane to delete</li>
<li>After the plane edit window was opened delete is chosen</li>
</ol>
</td>
<td>The plane is deleted without an error</td>
</tr>

<tr>
<td>Delete Plane (Error)</td>
<td>Sales Officer tries to delete a plane that is used in a flight</td>
<td>
<ol>
<!--Steps-->
<li>Chooses an plane to delete</li>
<li>After the plane edit window was opened delete is chosen</li>
</ol>
</td>
<td>The plane is not deleted but instead an error dialog is shown</td>
</tr>


</table>
