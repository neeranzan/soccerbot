import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

//extract noun phrases from a single sentence using OpenNLP
public class OpenNLP {
	private Set<String> nounPhrases;
	private Parser parser;

	public OpenNLP() {
		nounPhrases = new HashSet<String>();
		InputStream modelInParse = null;
		try {
			// load chunking model
			modelInParse = new FileInputStream(
					"war/WEB-INF/lib/en-parser-chunking.bin"); // from
																// http://opennlp.sourceforge.net/models-1.5/
			ParserModel model = new ParserModel(modelInParse);
			// create parse tree
			 parser = ParserFactory.create(model);

		}

		catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelInParse != null) {
				try {
					modelInParse.close();
				} catch (IOException e) {
				}
			}
		}
	}

	// static String sentence = "Who is the author of The Call of the Wild?";

	public Set<String> getPhraseChunks(String str) {

		try{
		Parse topParses[] = ParserTool.parseLine(str, parser, 1);
		if(topParses!=null){
		// call subroutine to extract noun phrases
		for (Parse p : topParses)

			getNounPhrases(p);

		// print noun phrases
		// for (String s : nounPhrases)
		// System.out.println(s);
		return nounPhrases;
		// The Call
		// the Wild?
		// The Call of the Wild? //punctuation remains on the end of sentence
		// the author of The Call of the Wild?
		// the author
		}
		}
		catch(Exception ex){
			throw ex;
		}
		nounPhrases.add(str);
		return nounPhrases;

	}

	// recursively loop through tree, extracting noun phrases
	public void getNounPhrases(Parse p) {

		if (p.getType().equals("NP")) { // NP=noun phrase
			nounPhrases.add(p.getCoveredText());
		}
		for (Parse child : p.getChildren())
			getNounPhrases(child);
	}
}