package superapp.logic;

import superapp.boundaries.user.UserBoundary;

import java.util.List;

public interface AdvancedUsersService extends UsersService {
    public List<UserBoundary> getAllUsers(String userSuperapp,String email,int size,int page);
    public void deleteAllUsers(String userSuperapp, String email);
    }