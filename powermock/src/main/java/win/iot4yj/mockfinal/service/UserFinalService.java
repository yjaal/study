package win.iot4yj.mockstatic.service;

import win.iot4yj.common.User;
import win.iot4yj.mockstatic.dao.UserStaticDao;

public class UserStaticService {

    public int queryUserCount() {
        return UserStaticDao.getCount();
    }

    public void saveUser(User user) {
        UserStaticDao.insertUser(user);
    }
}
