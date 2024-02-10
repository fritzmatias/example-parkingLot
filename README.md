# example-parkingLot
SpringBoot microservice example.

## Definition
 - The parking lot has 25 total spaces
 - The parking lot can hold motorcycles, cars and vans
 - The parking lot has motorcycle spots, compact car spots and regular spots
 - A car can park in a single compact spot, or a regular spot
 - A van can park, but it will take up 3 regular spots (for simplicity we don't need to make sure these spots are beside each other)
## Acceptance Criteria
   Service endpoints

 - Park vehicle
 - Vehicle leaves parking lot
 - Find how many spots are remaining
 - Check if all parking spots are taken for a given vehicle type

### Design Considerations
 - VehicleTypes & SpotTypes & slots amount required for considered one spot (mapping), can be defined by configuration 
    file [application.properties](src/test/resources/application-test.properties) 
    || [application.yaml](src/main/resources/application.yaml).
 - Lot spots are initialized at start up, and set as free (without any vehicleId).
 - When remaining spots are queried, it could look like an error is happening showing more spots than configured.
  It is not an error, that is because each car type is considered to use all the free spots 
  given they required slots config and spot type. 