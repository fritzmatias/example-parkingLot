package assessment.parkinglot.controller.errors;

import jakarta.annotation.Nullable;

import java.util.Collection;

public record ValidationErrorResponse (
    String type,
    int status,
    String error ,
    @Nullable
    Collection<Violation> violations
){
}
