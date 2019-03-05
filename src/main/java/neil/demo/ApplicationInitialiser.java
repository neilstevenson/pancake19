package neil.demo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Configuration
public class ApplicationInitialiser implements CommandLineRunner {
	
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private HazelcastInstance hazelcastInstance;
	@Autowired
	private MyService myService;
	
	@Override
	public void run(String... args) throws Exception {

		System.out.println("~~~~~~~~~~~~~~~~~~");

		for (int i=0 ; i<3; i++) {
			int input = 5;
			System.out.printf("BEFORE ITERTION %d%n", i);
			int output = this.myService.square(input);
			System.out.printf("AFTER ITERTION %d, %d -> %d%n", i, input, output);
		}

		System.out.println("~~~~~~~~~~~~~~~~~~");

		Collection<DistributedObject> dObjs = this.hazelcastInstance.getDistributedObjects();
				
		for (DistributedObject dObj : dObjs) {
			System.out.println("=======================");
			System.out.printf("%s : %s%n", dObj.getName(), dObj.getClass().getCanonicalName());
			if (dObj instanceof IMap) {
				IMap<?,?> iMap = (IMap<?,?>) dObj;
				for (Object key : iMap.keySet()) {
					System.out.printf(" - Key '%s', Value '%s'%n", key, iMap.get(key));
				}
			}
		}

		System.out.println("~~~~~~~~~~~~~~~~~~");
		System.out.printf("Distributed Objects %d%n", dObjs.size());
		System.out.printf("CacheManager %s%n", this.cacheManager);
		System.out.println("~~~~~~~~~~~~~~~~~~");
		
		
		this.hazelcastInstance.shutdown();
	}
	
}