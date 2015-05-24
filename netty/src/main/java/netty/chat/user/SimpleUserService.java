package netty.chat.user;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleUserService implements UserService{

	private List<User> users = new CopyOnWriteArrayList<User>();
	
	public SimpleUserService() {
		users.add(new User("user1", "pass1"));
		users.add(new User("user2", "pass2"));
		users.add(new User("user3", "pass3"));
		users.add(new User("user4", "pass4"));
		users.add(new User("user5", "pass5"));
		users.add(new User("user6", "pass6"));
		users.add(new User("user7", "pass7"));
		users.add(new User("user8", "pass8"));
		users.add(new User("user9", "pass9"));
		users.add(new User("user10", "pass10"));
	}
	
	public User getUser(String name) {
		for (User user : users) {
			if(user.getName().equals(name)){
				return user;
			}
		}
		return null;
	}

	public void addUser(User user) throws Exception{
		if(user == null){
			throw new IllegalArgumentException("user不能为空");
		}
		User existUser = getUser(user.getName());
		if(existUser != null){
			throw new IllegalArgumentException("该用户名已存在");
		}
		users.add(user);
	}
    
	public List<User> getAllUsers() {
		return users;
	}

	
	
}
