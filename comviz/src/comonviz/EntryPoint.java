package comonviz;

import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;
import ca.uvic.cs.chisel.cajun.graph.FlatGraph;
import ca.uvic.cs.chisel.cajun.graph.Graph;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.tree.DefaultMutableTreeNode;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.protege.ontograf.common.GraphController;
import org.protege.ontograf.common.ProtegeGraphModel;
import org.protege.ontograf.common.StyleManager;

import ca.uvic.cs.chisel.cajun.actions.ClearOrphansAction;
import ca.uvic.cs.chisel.cajun.actions.LayoutAction;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;

import org.protege.ontograf.treeUtils.TreeInfoManager;
import org.protege.ontograf.ui.TopView;

import uk.ac.manchester.cs.bhig.util.MutableTree;
import unused.OntologyTreeExplorer;

public class EntryPoint {

	public static OWLOntology ontology = null;

	// private static String ontologyURI = "CoMOn-281111.owl";

	// private static String ontologyURI = "pizza.owl";
	// private static String ontologyURI = "comonTest.owl";
	 private static String ontologyURI = "COMON_v4_full_rel.owl";
	// private static String ontologyURI = "COMON_v2.owl";
	// private static String ontologyURI = "CoMOnv0.4.owl";
	// private static String ontologyURI = "comonTest.owl";

	public static GraphController gc;
	
	public static Graph graph;
	

	public static GraphController getGc() {
		return gc;
	}

	public static MutableTree ontologyTree;

	//public static OntologyTreeExplorer ontologyTreeExplorer = new OntologyTreeExplorer();

	/**
     * 
     */
	@SuppressWarnings("unused")
	private static void start() {

		ontologyURI = "file:///" + (new File(ontologyURI)).getAbsolutePath();
		ontologyURI = ontologyURI.replace("\\", "/");

		OwlApi owlapi = new OwlApi();

		try {
			ontology = owlapi.openOntology(ontologyURI);

		} catch (OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Create and set up the window.
		JFrame frame = new JFrame("CoMOnViz");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.pack();
		// frame.setBounds(0, 0, 800, 600);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		try {
			gc = new GraphController(frame);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		graph = gc.getGraph();
		FlatGraph fg = (FlatGraph) gc.getGraph();

		gc.getModel().owlOntology = EntryPoint.ontology;
		// LayoutAction layoutAction =
		// ((AbstractGraph)gc.getGraph()).getLayout(LayoutConstants.LAYOUT_SPRING);
		LayoutAction layoutAction = ((AbstractGraph) gc.getGraph())
				.getLayout(LayoutConstants.LAYOUT_RADIAL);

		for (OWLClass cls : ontology.getClassesInSignature()) {
			//Set<OWLClassExpression> subClasses;
			//subClasses = cls.getSubClasses(ontology);
			gc.getModel().generateNodesAndArcs(cls,
					((AbstractGraph) gc.getGraph()).getFilterManager());
		}

		Collection nodes = null;
		nodes = gc.getModel().getAllNodes();
		TreeInfoManager treeInfoManager = TreeInfoManager.getTreeManager();
		treeInfoManager.generateTreeInfo(nodes);

		ontologyTree = treeInfoManager.getTreeRoot();
		DefaultMutableTreeNode root = null;
		root = TreeInfoManager.convertFromManchesterToUITreeNode(ontologyTree);
		TopView topView = gc.getView();
		topView.getTreeModel().setRoot(root);
		



		StyleManager.initStyleManager(treeInfoManager.getBranchNodes()
				.size(), 3);

		for (OWLClass cls : ontology.getClassesInSignature()) {
			//gc.getModel().removeNode(cls);
		}
		
		//gc.getModel().removeSpecificArcType("has subclass");
		for (OWLClass cls : ontology.getClassesInSignature()) {
			
			if (treeInfoManager.getLevel(cls) >= 2) {
				gc.getModel().removeNode(cls);
				//gc.getModel().show(cls, ((AbstractGraph) gc.getGraph()).getFilterManager());
			}else{
				gc.getModel().hideAllDesendants(gc.getModel().getNode(cls));
				gc.getModel().show(cls, ((AbstractGraph) graph).getFilterManager());
			}
		}

		layoutAction.doAction();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				start();
			}
		});

		// start();

	}

}
