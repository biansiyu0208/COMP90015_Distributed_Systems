

public class InvalidInputException extends Exception{
	public InvalidInputException() {
		super("invalid command");
	}
	
	public InvalidInputException(String message) {
		super(message);
	}
}
