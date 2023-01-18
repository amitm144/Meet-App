package superapp.converters;

import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.user.UserIdBoundary;
import superapp.data.UserEntity;
import superapp.data.UserPK;
import superapp.data.UserRole;
import org.springframework.stereotype.Component;
import superapp.util.exceptions.InvalidInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    public UserConverter() { }

    public UserEntity toEntity(UserBoundary user) {
        UserEntity result = new UserEntity();
        result.setSuperapp(user.getUserId().getSuperapp());
        result.setEmail(user.getUserId().getEmail());
        result.setUsername(user.getUsername());
        result.setRole(UserRole.valueOf(user.getRole()));
        result.setAvatar(user.getAvatar());
        return result;
    }

    public UserBoundary toBoundary(UserEntity user) {
        UserBoundary result = new UserBoundary();
        result.setSuperApp(user.getSuperapp());
        result.setEmail(user.getEmail());
        result.setRole(user.getRole().name());
        result.setUsername(user.getUsername());
        result.setAvatar(user.getAvatar());
        return result;
    }

    public UserPK idBoundaryToPK(UserIdBoundary id) {
        return new UserPK(id.getSuperapp(), id.getEmail());
    }

    public List<UserIdBoundary> mapListToBoundaryList(List<Map<String, String>> list) {
        if (list == null)
            return new ArrayList<>();
        return  list
                .stream()
                .map(this::mapToBoundary)
                .collect(Collectors.toList());
    }

    public UserIdBoundary mapToBoundary(Map<String, String> map) {
        if (map == null || !(map.containsKey("superapp") && map.containsKey("email")))
            throw new InvalidInputException("Missing or invalid user data");

        return new UserIdBoundary(map.get("superapp"), map.get("email"));
    }
}
