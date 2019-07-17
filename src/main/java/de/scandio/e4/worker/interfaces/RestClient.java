package de.scandio.e4.worker.interfaces;

import java.util.List;
import java.util.Set;

public interface RestClient {

	String getUser();
	List<String> getUsernames();
	List<Long> getRandomEntityIds(int limit);

}
