package assessment.parkinglot.errors;

import java.util.function.Supplier;

public class ThrowerFactory {
    public static void throwDataNotFound(String publicMessage, String nonPublicMessage)
            throws DataNotFound {
        throw new DataNotFound(publicMessage, nonPublicMessage);
    }

    public static Supplier<DataNotFound> DataNotFoundSupplier(String publicMessage, String nonPublicMessage) {
        return ()->new DataNotFound(publicMessage, nonPublicMessage);
    }

    public static void throwDataDuplication(String publicMessage, String nonPublicMessage)
            throws DataDuplication {
        throw new DataDuplication(publicMessage, nonPublicMessage);
    }

}
