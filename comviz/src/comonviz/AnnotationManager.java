package comonviz;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

public class AnnotationManager {

	private static String[] boldKeywords = new String[] { "Overview",
			"Definition", "Synonym", "Relationships", "Attributes" };

	private static Collection<String> owlClasses;
	private static Collection<String> owlAxioms;

	private Pattern boldKeywordPattern;
	private Pattern owlClassPattern;
	private Pattern owlAxiomPattern = Pattern
			.compile("(part of|associate with|type of)");

	// owlAxiomPattern =

	public AnnotationManager(OWLOntology ontology) {
		owlClasses = new ArrayList<String>();

		for (OWLClass cls : ontology.getClassesInSignature()) {
			owlClasses.add(getNameFromeStringID(cls.toStringID()));
		}

		owlAxioms = new ArrayList<String>();
		owlAxioms.add("part of");
		owlAxioms.add("associate with");
		owlAxioms.add("type of");

		StringBuffer sb = new StringBuffer("((");
		int i = 0;
		for (i = 0; i < boldKeywords.length - 1; i++) {
			sb.append(boldKeywords[i]);
			sb.append("|");
		}
		sb.append(boldKeywords[i]);
		sb.append(")(:)(.*))");
		boldKeywordPattern = Pattern.compile(sb.toString());

		sb = new StringBuffer("((");

		for (i = 0; i < owlClasses.size() - 1; i++) {
			sb.append(((ArrayList) owlClasses).get(i));
			sb.append("|");
		}
		sb.append(((ArrayList) owlClasses).get(i));
		sb.append(")([a-z]){0,2})");
		owlClassPattern = Pattern.compile(sb.toString());
	}

	public String getStylizedAnnotation(String orignalAnnotation) {
		
		orignalAnnotation = "Definition:" + orignalAnnotation;
		InputStream annotationInputStream = new ByteArrayInputStream(
				orignalAnnotation.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(
				annotationInputStream));
		StringBuffer stylelizedAnnotation = new StringBuffer("<html>");
		
		String line;
		try {
			while ((line = br.readLine()) != null) {
				Matcher m1 = boldKeywordPattern.matcher(line);
				if(m1.find()){
					m1.replaceAll("<b>$1</b><br/>");
				}
				
				line = boldKeywordPattern.matcher(line).replaceAll("<b>$1</b><br/>");
				line = owlAxiomPattern.matcher(line).replaceAll("<i>$1</i>");
				line = owlClassPattern.matcher(line).replaceAll(
						"<font color = "
								+ StyleManager.ANNOTATION_CLASS_COLOR_HEX
								+ "><b>$1</b></font>");
				stylelizedAnnotation.append("<p>" + line + "</p>");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		stylelizedAnnotation.append("</html>");
		return stylelizedAnnotation.toString();
	}

	private String getNameFromeStringID(String stringID) {
		stringID = stringID.substring(stringID.lastIndexOf("#") + 1)
				.replaceAll("_", " ");

		return stringID;

	}
}
