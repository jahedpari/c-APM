package Utill;


public class MyException extends Exception {
	

	private static final long serialVersionUID = 1L;
	String msg="Unknown";
	
    public MyException(String message) {
        super(message);
        
       msg=message;
    }
    
    String getMesssage()
    {
    	return msg;
    }
}