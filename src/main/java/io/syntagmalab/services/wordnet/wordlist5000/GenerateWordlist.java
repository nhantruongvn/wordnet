package io.syntagmalab.services.wordnet.wordlist5000;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;

import java.io.*;
import java.net.URL;
import java.util.List;

public class GenerateWordlist {
    public static void main(String[] args) throws IOException {
        final String DICTIONARY_PATH = "./src/main/resources/wndict";
        final String INPUT_FILE = "./src/main/resources/static/5000_words_with_POS.txt";
        final String OUTPUT_FILE = "./src/main/resources/static/5000_words_with_definitions.txt";

        // construct the dictionary object and open it
        URL url = new URL("file", null, DICTIONARY_PATH);
        IDictionary dict = new Dictionary(url);
        dict.open();

        // create output file
        File outputFile = new File(OUTPUT_FILE);
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));

        // read input file
        BufferedReader bufferedReader = new BufferedReader(new FileReader(INPUT_FILE));
        String line = bufferedReader.readLine();
        String word;
        Integer pos;

        while (null != line) {
            // split each line into word and POS-number
            word = line.split("\\_")[0];
            pos = Integer.parseInt(line.split("\\_")[1]);

            // get all senses of a word
            IIndexWord iIndexWord = dict.getIndexWord(word, POS.getPartOfSpeech(pos));
            if (iIndexWord == null) {
                bufferedWriter.write(word);
                bufferedWriter.newLine();
                line = bufferedReader.readLine();
                continue;
            }
            List<IWordID> iWordIdList = iIndexWord.getWordIDs();

            // write keyword-definition pairs in output file
            for (IWordID iWordId : iWordIdList) {
                String keyword = dict.getWord(iWordId).getLemma();
                String definition = dict.getWord(iWordId).getSynset().getGloss();
                bufferedWriter.write(keyword + "\t" + definition);
                bufferedWriter.newLine();
            }
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
    }
}
