package de.scandio.e4.worker.util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomData {

	private static final Random RAND = new Random();

	public static final String STRING_LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Integer eget aliquet nibh praesent. Platea dictumst quisque sagittis purus sit amet volutpat consequat mauris. Montes nascetur ridiculus mus mauris vitae ultricies leo integer. In fermentum posuere urna nec. Viverra vitae congue eu consequat ac felis. Sed egestas egestas fringilla phasellus faucibus scelerisque eleifend donec pretium. Non diam phasellus vestibulum lorem sed risus ultricies. Amet tellus cras adipiscing enim eu turpis egestas pretium. A pellentesque sit amet porttitor eget dolor morbi. Integer quis auctor elit sed vulputate mi sit amet. Leo in vitae turpis massa sed elementum tempus egestas. Non odio euismod lacinia at quis risus sed vulputate odio. Nunc scelerisque viverra mauris in. Tortor at risus viverra adipiscing at. Bibendum at varius vel pharetra vel turpis.";

	// TODO: change to this!
//	public static final String STRING_LOREM_IPSUM_2 = "Et eius non dicta Qui iusto quia Ipsa quo hic delectus fuga aliquid et Odio ipsa aut Exercitationem necessitatibus vitae ut sapiente autem Eaque voluptatem eum placeat necessitatibus Id facilis voluptas Voluptas vel libero porro est molestias sed deleniti Ratione est blanditiis omnis sapiente Rem voluptatem sequi qui accusantium est Et porro aspernatur ab aut ratione Pariatur porro eum non ut qui Sint expedita corrupti Quibusdam deserunt magni minus quibusdam odio et Debitis corrupti esse Hic sunt dolores Voluptate earum atque qui quae vel odit minima Dolorum quo doloremque est molestiae nostrum qui Sed modi qui et sed";

	public static final List<String> LIST_LOREM_IPSUM = Arrays.asList(STRING_LOREM_IPSUM.split(" "));
	public static final int LIST_LOREM_IPSUM_SIZE = LIST_LOREM_IPSUM.size();

	public static String getRandomString(int wordCount) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < wordCount; i++) {
			builder.append(" ");
			builder.append(LIST_LOREM_IPSUM.get(RAND.nextInt(LIST_LOREM_IPSUM_SIZE - 1)));
		}
		return builder.toString();
	}

}
