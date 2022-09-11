package springbook.user.sqlservice;

import springbook.user.SqlServiceContext;

@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
    
}
