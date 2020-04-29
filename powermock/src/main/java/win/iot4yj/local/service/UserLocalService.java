package win.iot4yj.helloworld.service;

import win.iot4yj.common.User;
import win.iot4yj.helloworld.dao.UserDao;

public class UserService {

    private UserDao userDao;

    public UserService() {
    }

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public int queryUserCount(){
        return userDao.getCount();
    }

    public void saveUser(User user) {
        userDao.insertUser(user);
    }
}
