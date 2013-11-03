package comonviz;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import org.protege.ontograf.common.GraphController;
import org.protege.ontograf.treeUtils.TreeInfoManager;
import org.protege.ontograf.ui.TopView;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.manchester.cs.bhig.util.MutableTree;
import ca.uvic.cs.chisel.cajun.actions.LayoutAction;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;
import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;
import ca.uvic.cs.chisel.cajun.graph.Graph;

public class EntryPoint {

	public static OWLOntology ontology = null;


	public static GraphController gc;

	public static Graph graph;
	
	public static JFrame frame;

	public static GraphController getGc() {
		return gc;
	}

	public static MutableTree ontologyTree;
	
	private final String internalOWLFilePath = "/COMON_v5_annotation.owl";
	
	public static Dimension frameSize;


	/**
     * 
     */
	@SuppressWarnings({ "unused", "static-access", "static-access" })
	private  void start() {
		frame = new JFrame("CoMOnViz");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		// Display the window.
		frame.setMinimumSize(new Dimension(800, 600));
		frame.pack();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frameSize = frame.getSize();
		
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

	public static void loadOntologyFile(URI uri) {

		OwlApi owlapi = new OwlApi();
		try {
			ontology = owlapi.openOntology(uri);

		} catch(IllegalArgumentException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "This is an invalid owl file!", "Error", JOptionPane.ERROR_MESSAGE);		
			return;
		}
		catch (OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Create and set up the window.
		gc.getModel().owlOntology = EntryPoint.ontology;
		// LayoutAction layoutAction =
		// ((AbstractGraph)gc.getGraph()).getLayout(LayoutConstants.LAYOUT_SPRING);
		LayoutAction layoutAction = ((AbstractGraph) gc.getGraph())
				.getLayout(LayoutConstants.LAYOUT_RADIAL);

		for (OWLClass cls : ontology.getClassesInSignature()) {
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

		StyleManager.initStyleManager(treeInfoManager.getBranchNodes(), gc
				.getModel().getArcTypes());


		for (OWLClass cls : ontology.getClassesInSignature()) {

			if (treeInfoManager.getLevel(cls) >= 2) {
				gc.getModel().removeNode(cls);
			} else {
				gc.getModel().hideAllDesendants(gc.getModel().getNode(cls));
				gc.getModel().show(cls,
						((AbstractGraph) graph).getFilterManager());
			}
		}

		topView.changeDividerLocation();
		layoutAction.doAction();

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new EntryPoint().start();
			}
		});
		


	}

}
