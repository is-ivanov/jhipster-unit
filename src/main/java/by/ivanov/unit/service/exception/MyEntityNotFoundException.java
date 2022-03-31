package by.ivanov.unit.service.exception;

public class MyEntityNotFoundException extends RuntimeException {

	public static final String TEMPLATE_MESSAGE_NOT_FOUND = "%s with %s(%s) not found";

	public MyEntityNotFoundException(String entityName, String fieldName, Object fieldValue) {
		super(String.format(TEMPLATE_MESSAGE_NOT_FOUND, entityName, fieldName, fieldValue));
	}

	public MyEntityNotFoundException(String entityName, String fieldName, Object fieldValue, Exception ex) {
		super(String.format(TEMPLATE_MESSAGE_NOT_FOUND, entityName, fieldName, fieldValue), ex);
	}
}
