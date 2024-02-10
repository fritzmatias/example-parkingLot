package assessment.parkinglot.errors;

public class CustomExceptions extends Exception {
    private final String nonPublicMessage;

    public CustomExceptions(String s, String nonPublicMessage){
        super(s);
        this.nonPublicMessage = nonPublicMessage;
    }

    public CustomExceptions(String s, String nonPublicMessage, Throwable e){
        super(s,e);
        this.nonPublicMessage = nonPublicMessage;
    }

    public String getNonPublicMessage() {
        return nonPublicMessage;
    }

    public String getAllMessages(){
        return this.getMessage()+": "+this.getNonPublicMessage();
    }
}
