## Test Scenarios

<table>
<tr><th>UseCase</th><th>TestCase Description</th><th>TestCase Steps</th><th>Result</th></tr>

<tr>
<td><!-- UseCase Name-->Search For Flight</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->View FlightOptions</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Start Sales Process</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Stop Sales Process</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Stop Sales Process</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->book tickets</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->book tickets</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Edit PriceReduction</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>

<tr>
<td><!-- UseCase Name-->Create Price Reduction</td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
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
<td><!-- TestCaseDescription-->SalesManager sees the selected KPIs</td>
<td>
<ol>
<!--Steps-->
<li>Select KPI section</li>
<li>Select Filters, that should be applied to the KPIs</li>
</ol>
</td>
<td><!--Expected Result-->System shows filtered KPIs</td>
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
<td><!-- UseCase Name--></td>
<td><!-- TestCaseDescription--></td>
<td>
<ol>
<!--Steps-->
<li></li>
</ol>
</td>
<td><!--Expected Result--></td>
</tr>


</table>