package assessment.parkinglot.controller.errors;

public record ErrorResponse(
    String type,
    int status,
    String error
){
}
