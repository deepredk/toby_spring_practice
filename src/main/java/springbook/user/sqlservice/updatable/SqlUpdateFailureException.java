package springbook.user.sqlservice.updatable;

import springbook.user.sqlservice.SqlNotFoundException;

public class SqlUpdateFailureException extends RuntimeException {
    
    public SqlUpdateFailureException(String message) {
        super(message);
    }

    public SqlUpdateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlUpdateFailureException(SqlNotFoundException e) {
        super(e);
    }

}
