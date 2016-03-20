package index;

import java.io.IOException;
import java.util.LinkedHashMap;

public class RankingMain {

	public static void main(String[] args) {

		try {

			LinkedHashMap<String, Integer> WordsAndDocCoutnt = new FindDF()
					.findDF("D:/Anisha/Indexed.json");

			new Ranking().rank(WordsAndDocCoutnt);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
