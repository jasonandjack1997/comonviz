package au.uq.dke.comonviz;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import au.uq.dke.comonviz.actions.LayoutAction;
import au.uq.dke.comonviz.graph.FlatGraph;
import au.uq.dke.comonviz.model.AnnotationManager;
import au.uq.dke.comonviz.model.TreeInfoManager;
import au.uq.dke.comonviz.ui.StyleManager;
import au.uq.dke.comonviz.util.OWLUtil;
import uk.ac.manchester.cs.bhig.util.MutableTree;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;
import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;

public class EntryPoint {

	public static OWLOntology ontology = null;
	private final String internalOWLFilePath = "/COMON_v8_HenryNewRel.owl";

	/** the graph object, performs layouts and renders the model */
	private static FlatGraph flatGraph;

	/** the model representation of the graph, nodes and edges */
	private static ComonvizGraphModel graphModel;

	private static TopView topView;

	private static GraphController graphController;

	private static JFrame jFrame;

	public static JFrame getjFrame() {
		return jFrame;
	}

	private static MutableTree ontologyTree;

	public static Dimension frameSize;

	private static AnnotationManager annotationManager;

	public static AnnotationManager getAnnotationManager() {
		return annotationManager;
	}

	/**
     * 
     */
	@SuppressWarnings({ "unused", "static-access", "static-access" })
	private void start() {
		jFrame = new JFrame("CoMOnViz");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Display the window.
		jFrame.setMinimumSize(new Dimension(800, 600));
		jFrame.pack();
		jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		jFrame.setVisible(true);
		frameSize = jFrame.getSize();

		graphModel = new ComonvizGraphModel();
		flatGraph = new FlatGraph();
		topView = new TopView();
		graphController = new GraphController();

		URL ontologyURL = null;
		try {

			ontologyURL = this.getClass().getResource(internalOWLFilePath);
			//ontologyURL = this.getClass().getResource("/annotationTest.owl");
		} catch (NullPointerException e3) {
			e3.printStackTrace();
		}
		
		try {
			loadOntologyFile(ontologyURL.toURI());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static OWLOntology getOntology() {
		return ontology;
	}

	public static FlatGraph getFlatGraph() {
		return flatGraph;
	}

	public static ComonvizGraphModel getGraphModel() {
		return graphModel;
	}

	public static TopView getTopView() {
		return topView;
	}

	public static GraphController getGraphController() {
		return graphController;
	}

	public static JFrame getFrame() {
		return jFrame;
	}

	public static MutableTree getOntologyTree() {
		return ontologyTree;
	}

	public static void loadOntologyFile(URI uri) {

		OWLUtil owlapi = new OWLUtil();
		try {
			ontology = owlapi.openOntology(uri);

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "This is an invalid owl file!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Create and set up the window.
		annotationManager = new AnnotationManager(ontology);
		graphController.getModel().owlOntology = EntryPoint.ontology;
		// LayoutAction layoutAction =
		// ((AbstractGraph)gc.getGraph()).getLayout(LayoutConstants.LAYOUT_SPRING);
		LayoutAction layoutAction = ((AbstractGraph) graphController.getGraph())
				.getLayout(LayoutConstants.LAYOUT_RADIAL);

		for (OWLClass cls : ontology.getClassesInSignature()) {
			graphController.getModel().generateNodesAndArcs(
					cls,
					((AbstractGraph) graphController.getGraph())
							.getFilterManager());
		}

		Collection nodes = null;
		nodes = graphController.getModel().getAllNodes();
		TreeInfoManager treeInfoManager = TreeInfoManager.getTreeManager();
		treeInfoManager.generateTreeInfo(nodes);

		ontologyTree = treeInfoManager.getTreeRoot();
		DefaultMutableTreeNode root = null;
		root = TreeInfoManager.convertFromManchesterToUITreeNode(ontologyTree);
		TopView topView = graphController.getView();
		topView.getTreeModel().setRoot(root);

		StyleManager.initStyleManager(treeInfoManager.getBranchNodes(),
				graphController.getModel().getArcTypes());

		for (OWLClass cls : ontology.getClassesInSignature()) {

			if (treeInfoManager.getLevel(cls) >= 2) {
				graphController.getModel().removeNode(cls);
			} else {
				graphController.getModel().hideAllDesendants(
						graphController.getModel().getNode(cls));
				graphController.getModel().show(cls,
						EntryPoint.getFlatGraph().getFilterManager());
			}
		}

		topView.changeDividerLocation();
		layoutAction.doAction();

	}

	public static void main(String[] args) {
		new EntryPoint().start();
		// TODO Auto-generated method stub
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				new EntryPoint().start();
//			}
//		});

	}

}
