package exceptions;

public class UnknownProperty extends RuntimeException {
    public UnknownProperty(){
        super();
    }

    public UnknownProperty(String message){
        super(message);
    }
}
