package superapp.converters;

//import com.fasterxml.jackson.databind.ObjectMapper;
import superapp.boundaries.user.UserBoundary;
import superapp.data.UserEntity;
import superapp.data.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

//    private ObjectMapper mapper;

    public UserConverter() {
//        this.mapper = new ObjectMapper();
    }

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
}
