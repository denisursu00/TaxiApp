package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithLabel;

/**
 * Calculeaza lungimea necesara pentru etichetele campului unui formular in functie de lungimea cuvintelor etichetelor.
 */
public class WordLengthBasedFieldLabelWidthCalculationStrategy {
	
	private static final String REGEX_WHITESPACE = "\\s";
	private static final int PIXELS_PER_CHARACTER = 7;
	
	private final int minimumWidthInPixels;
	private final Collection<? extends ModelWithLabel> modelsWithLabel;
	
	public WordLengthBasedFieldLabelWidthCalculationStrategy(int minimumWidthInPixels,
			Collection<? extends ModelWithLabel> modelsWithLabel) {
		
		this.minimumWidthInPixels = minimumWidthInPixels;
		this.modelsWithLabel = modelsWithLabel;
	}
	
	public int calculateNeededLabelWidth() {
		
		if (modelsWithLabel.isEmpty()) {
			return minimumWidthInPixels;
		}
		
		String longestWordInAnyModelLabel = "";
		for (ModelWithLabel model : modelsWithLabel) {
			String[] wordsInModelLabel = model.getLabel().split(REGEX_WHITESPACE);
			for (String wordInModelLabel : wordsInModelLabel) {
				if (wordInModelLabel.length() > longestWordInAnyModelLabel.length()) {
					longestWordInAnyModelLabel = wordInModelLabel;
				}
			}
		}
		
		int neededLabelWidthInPixelsByLongestWord = (longestWordInAnyModelLabel.length() * PIXELS_PER_CHARACTER);
		
		return (neededLabelWidthInPixelsByLongestWord > minimumWidthInPixels) ? neededLabelWidthInPixelsByLongestWord : minimumWidthInPixels;
	}
}