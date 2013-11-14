package au.uq.dke.comonviz.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import database.model.ontology.OntologyAxiom;
import database.model.ontology.OntologyClass;
import au.uq.dke.comonviz.EntryPoint;
import au.uq.dke.comonviz.ui.StyleManager;

public class AnnotationManager {

	private static String[] boldKeywords = new String[] { "Overview",
			"Definition", "Synonym", "Relationships", "Attributes" };

	private static List<String> ontologyClasses;
	private static List<String> owlAxioms;

	private Pattern boldKeywordPattern;
	private Pattern owlClassPattern;
	private Pattern owlAxiomPattern = Pattern
			.compile("(part of|associated with|type of)");

	private Pattern overViewPattern = Pattern.compile("Overview");
	private Pattern definitionPattern = Pattern.compile("Definition");
	private Pattern attributesPattern = Pattern.compile("Attributes");
	private Pattern synonymPattern = Pattern.compile("Synonym");
	private Pattern relationshipsPattern = Pattern.compile("Relationships");

	private Pattern wordPattern = Pattern.compile("(\\w+)");

	private String AnnotationClassStyle = "<font color = "
			+ StyleManager.ANNOTATION_CLASS_COLOR_HEX + "><b>$1</b></font>";

	private String AnnotationAttributesStyle = "<font color = "
			+ StyleManager.ANNOTATION_ATTRIBUTES_COLOR_HEX + "><b>$1</b></font>";

	private String AnnotationRelationshipStyle = "<font color = "
			+ StyleManager.ANNOTATION_RELATIONSHIP_COLOR_HEX + "><b><i>$1</i></b></font>";

	// owlAxiomPattern =

	public AnnotationManager() {
		ontologyClasses = new ArrayList<String>();

		for (OntologyClass cls : EntryPoint.getOntologyClassService().findAll()) {
			ontologyClasses.add(cls.getName());
		}

		//sort, longer term first to fully match classes
		Collections.sort(ontologyClasses, Collections.reverseOrder());
		
		
		owlAxioms = new ArrayList<String>();
		for (OntologyAxiom axiom : EntryPoint.getOntologyAxiomService().findAll()) {
			owlAxioms.add(axiom.getName());
		}
		Collections.sort(owlAxioms, Collections.reverseOrder());
		
//		owlAxioms.add("part of");
//		owlAxioms.add("associated with");
//		owlAxioms.add("type of");

		StringBuffer sb = new StringBuffer("((");
		int i = 0;
		for (i = 0; i < boldKeywords.length - 1; i++) {
			sb.append(boldKeywords[i]);
			sb.append("|");
		}
		sb.append(boldKeywords[i]);
		sb.append(")(:?))");
		boldKeywordPattern = Pattern.compile(sb.toString());

		sb = new StringBuffer("((");

		for (i = 0; i < ontologyClasses.size() - 1; i++) {
			sb.append(((ArrayList) ontologyClasses).get(i));
			sb.append("|");
		}
		sb.append(((ArrayList) ontologyClasses).get(i));
		sb.append(")([a-z]){0,2})");
		owlClassPattern = Pattern.compile(sb.toString());
	}

	public String getStylizedAnnotation(String orignalAnnotation) {


		if(!orignalAnnotation.startsWith("Overview")){
			orignalAnnotation = "Definition:" + orignalAnnotation;
		}
		InputStream annotationInputStream = new ByteArrayInputStream(
				orignalAnnotation.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(
				annotationInputStream));
		StringBuffer stylelizedAnnotation = new StringBuffer("<html>");

		String line;

		try {
			while ((line = br.readLine()) != null) {
				String lineRemainder = "";
				String lineHead = "";

				Matcher boldKeywordMatcher = boldKeywordPattern.matcher(line);
				if (boldKeywordMatcher.find()) {
					lineHead = line.substring(0, boldKeywordMatcher.end());
					lineRemainder = line.substring(boldKeywordMatcher.end(),
							line.length());
				}
				
				
				if (attributesPattern.matcher(line).find()) {
					lineRemainder = wordPattern.matcher(lineRemainder)
							.replaceAll(AnnotationAttributesStyle);
				} else if (synonymPattern.matcher(line).find()) {
					lineRemainder = wordPattern.matcher(lineRemainder)
							.replaceAll(AnnotationClassStyle);

				} else if (relationshipsPattern.matcher(line).find()) {
					Matcher owlAxiomPatternMatcher = owlAxiomPattern
							.matcher(lineRemainder);
					if (owlAxiomPatternMatcher.find()) {
						String axiom = lineRemainder.substring(0,
								owlAxiomPatternMatcher.end());
						String wordList = lineRemainder.substring(
								owlAxiomPatternMatcher.end(),
								lineRemainder.length());
						axiom = owlAxiomPattern.matcher(axiom)
								.replaceAll(AnnotationRelationshipStyle);
						wordList = wordPattern.matcher(wordList)
								.replaceAll(AnnotationClassStyle);
						lineRemainder = axiom + wordList;
					}
				} else if (definitionPattern.matcher(line).find()) {
					lineRemainder = owlClassPattern.matcher(lineRemainder).replaceAll(
							AnnotationClassStyle);
				} else if (overViewPattern.matcher(line).find()) {
					lineRemainder = owlClassPattern.matcher(lineRemainder).replaceAll(
						AnnotationClassStyle);
				} 
				//
				if(lineHead.equals("")){
					lineRemainder = line;
					lineRemainder = owlClassPattern.matcher(lineRemainder).replaceAll(
							AnnotationClassStyle);
				}

				if(!lineHead.equals("")){
					lineHead = boldKeywordPattern.matcher(lineHead).replaceAll(
							"<b>$1</b><br/>");
				}

				stylelizedAnnotation.append("<p>" + lineHead + lineRemainder
						+ "</p>");
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
