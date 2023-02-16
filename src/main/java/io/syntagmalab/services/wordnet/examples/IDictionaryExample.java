package io.syntagmalab.services.wordnet.examples;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class IDictionaryExample {
    public void runExample() throws IOException {
        // construct the URL to the Wordnet dictionary directory
//        String wnhome = System.getenv("WNHOME");
//        String path = wnhome + File.separator + "dict";
        String path = "./src/main/resources/wndict";
        URL url = null;
        try {
            url = new URL("file", null, path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) return;

        // construct the dictionary object and open it
        IDictionary dict = new Dictionary(url);
        dict.open();

        // look up first sense of the word "baby"
        IIndexWord idxWord = dict.getIndexWord("baby", POS.NOUN);
        IWordID wordID = idxWord.getWordIDs().get(0);
        IWord word = dict.getWord(wordID);
        System.out.println("Id = " + wordID);
        System.out.println("Lemma = " + word.getLemma());
        System.out.println("Gloss = " + word.getSynset().getGloss());
        System.out.println("====================");

        // look up related words
        IIndexWord idxWord1 = dict.getIndexWord("king", POS.NOUN);

        IWordID wordID1 = idxWord1.getWordIDs().get(0);
        IWord word1 = dict.getWord(wordID1);
        System.out.println(word1.getLemma() + " = " + word1.getSynset().getGloss());

        List<IWordID> word1RelatedWords = word1.getRelatedWords();
        for (IWordID wordId : word1RelatedWords) {
            IWord iWord = dict.getWord(wordId);
            System.out.println(iWord.getLemma() + " = " + iWord.getSynset().getGloss());
        }
        System.out.println("====================");

        // look up all senses of a given word
        IIndexWord idxWord2 = dict.getIndexWord("nice", POS.ADJECTIVE);
        List<IWordID> wordIdList2 = idxWord2.getWordIDs();

        for (IWordID iWordId : wordIdList2) {
            System.out.println(dict.getWord(iWordId).getLemma() + " = " + dict.getWord(iWordId).getSynset().getGloss());
        }
    }
}
