package netty.chat.user;

import java.util.List;


public interface UserService {
	
	User getUser(String name);

	void addUser(User user)  throws Exception;
	
	List<User> getAllUsers();
}
