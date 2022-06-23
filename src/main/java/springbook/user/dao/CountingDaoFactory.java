package springbook.user.dao;


public class CountingDaoFactory {
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }
    
    public ConnectionMaker realConnectionMaker() {
        return new SimpleConnectionMaker();
    }
}
