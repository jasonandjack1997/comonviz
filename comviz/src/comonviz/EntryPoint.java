package comonviz;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.swing.JFrame;

import org.protege.ontograf.common.GraphController;
import org.protege.ontograf.common.ProtegeGraphModel;
import org.protege.ontograf.common.StyleManager;
import org.protege.ontograf.tree.TreeInfoManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import ca.uvic.cs.chisel.cajun.actions.LayoutAction;
import ca.uvic.cs.chisel.cajun.constants.LayoutConstants;
import ca.uvic.cs.chisel.cajun.graph.AbstractGraph;
import ca.uvic.cs.chisel.cajun.graph.FlatGraph;

public class EntryPoint {
	
	public static OWLOntology ontology = null;
	
	
	//private static String ontologyURI = "CoMOn-281111.owl";
	//private static String ontologyURI = "pizza.owl";
	private static String ontologyURI = "COMON_v2.owl";
	
	private static GraphController gc;
    /**
     * 
     */
    @SuppressWarnings("unused")
	private static void start() {
    	
    	ontologyURI = "file:///" + (new File(ontologyURI)).getAbsolutePath();
    	ontologyURI = ontologyURI.replace("\\","/");
    	

		OwlApi owlapi = new OwlApi();

    	try {
			ontology = owlapi.openOntology(ontologyURI);

		} catch (OWLOntologyCreationException | OWLOntologyStorageException
				| IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //Create and set up the window.
        JFrame frame = new JFrame("CoMOnViz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.pack();
        frame.setBounds(0, 0, 800, 600);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        try {
			
			gc = new GraphController(frame);
	        FlatGraph fg = (FlatGraph)gc.getGraph();
	        frame.setVisible(true);

	        gc.getModel().owlOntology = EntryPoint.ontology;
			//LayoutAction layoutAction = ((AbstractGraph)gc.getGraph()).getLayout(LayoutConstants.LAYOUT_SPRING);
			LayoutAction layoutAction = ((AbstractGraph)gc.getGraph()).getLayout(LayoutConstants.LAYOUT_RADIAL);
			
			
	        for (OWLClass cls : ontology.getClassesInSignature()) {
	        	Set<OWLClassExpression> subClasses;
	        	subClasses = cls.getSubClasses(ontology);
	        	if(subClasses == null || subClasses.size() == 0){
	        		//continue;
	        	}
	        	((ProtegeGraphModel)(gc.getModel())).addNode((OWLEntity)cls);
	        	//then the model have all nodes, so we can generate tree information
	        	
	        	gc.showOWLClass(cls);
	        }
 
	        Collection nodes = gc.getModel().getVisibleNodes();
        	TreeInfoManager.getTreeManager().generateTreeInfo(nodes);

			StyleManager.initStyleManager(TreeInfoManager.getTreeManager().getBranchNodes().size(), 3);
        	

			
			for (OWLClass cls : ontology.getClassesInSignature()) {
	        	//gc.getModel().removeNode(cls);
	        }
	        
	        for (OWLClass cls : ontology.getClassesInSignature()) {
	        	Set<OWLClassExpression> subClasses;
	        	subClasses = cls.getSubClasses(ontology);
	        	if(subClasses == null || subClasses.size() == 0){
	        		//continue;
	        	}
	        	((ProtegeGraphModel)(gc.getModel())).addNode((OWLEntity)cls);
	        	//then the model have all nodes, so we can generate tree information
	        	
	        	gc.showOWLClass(cls);
	        }
	        
		
			layoutAction.doAction();


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

    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                start();
            }
        });

	}

}
