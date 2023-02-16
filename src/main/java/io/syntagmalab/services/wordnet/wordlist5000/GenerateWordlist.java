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
        final String PATH = "./src/main/resources/wndict";
        final String OUTPUT_FILE = "./src/main/resources/static/5000_words_with_definitions.txt";
        final String INPUT_FILE = "./src/main/resources/static/5000_words_with_POS.txt";

        try {
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
                word = line.split("\\_")[0];
                pos = Integer.parseInt(line.split("\\_")[1]);
                URL url = new URL("file", null, PATH);
                IDictionary dict = new Dictionary(url);
                dict.open();

                // get all senses of a word
                IIndexWord iIndexWord = dict.getIndexWord(word, POS.getPartOfSpeech(pos));
                if (iIndexWord == null) {
                    throw new NullPointerException("The word " + word + " cannot be found.");
                }
                List<IWordID> iWordIdList = iIndexWord.getWordIDs();

                // write keyword-definition in output file
                for (IWordID iWordId : iWordIdList) {
                    String keyword = dict.getWord(iWordId).getLemma();
                    String definition = dict.getWord(iWordId).getSynset().getGloss();
                    bufferedWriter.write(keyword + "\t" + definition);
                    bufferedWriter.newLine();
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
