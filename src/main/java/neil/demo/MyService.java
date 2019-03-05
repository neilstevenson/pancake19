package neil.demo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MyService {

	@Cacheable("square")
	public int square(int i) {
		System.out.printf("square(%d)%n", i);
		return i*i;
	}
	
}
