package comonviz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.protege.ontograf.treeUtils.TreeInfoManager;
import org.semanticweb.owlapi.model.OWLEntity;

import ca.uvic.cs.chisel.cajun.graph.node.GraphNode;
import edu.umd.cs.piccolox.util.PFixedWidthStroke;

/**
 * @author uqwwan10 manage the size, font, color, pound of border, background,
 *         line, label,etc
 */
public class StyleManager {

	private static StyleManager instance;

	//colors
	private static List<Color> nodeBackgroundColors;
	private static List<Color> arcColors;

	private static Map<Object, Color> nodeBackgroundColorMap;
	private static Map<Object, Color> arcColorMap;

	final private static float NODE_BACKGROUND_SATUATION = 0.2F;
	final private static float NODE_BACKGROUND_BRIGHTNESS = 1F;
	final private static float NODE_HUE_START = 0.0F;
	final private static float NODE_HUE_END = 1.0F;
	
	final private static float NODE_BORDER_BRIGHTNESS_GAIN = 0.2F;
	final private static float ARC_SATUATION = 179/256F;
	final private static float ARC_BRIGHTNESS = 0.7F;
	final private static float ARC_HUE_START = 0.58F;
	final private static float ARC_HUE_END = 0.1F;

	private static int MAX_ALPHA = 255;
	private static int MIN_ALPHA = 50;
	
	private final static int defultArcColorCount = 5;
	private static Color[] defaultArcColors;

	public static final Color DEFAULT_TOOLTIP_BACKGROUND_COLOR = new Color(255, 253, 157);
	
	//strokes
	final private static float MAX_BORDER_STROKE_WIDTH = 2F;
	final private static float BORDER_STROKE_WIDTH_SELECTION_INCREEMENT = 1F;
	final public static float DEFAULT_ARC_WIDTH = 1F;

	

	//fonts
	public static final float DEFAULT_NODE_TEXT_FONT_SIZE = 12f;
	public static final float DEFAULT_ARC_LABEL_TEXT_FONT_SIZE = 10f;
	final public static float DEFAULT_HIDDEN_CHILDREN_COUNT_TEXT_FONT_SIZE = 8F;
	final public static float DEFAULT_TOOLTIP_TEXT_FONT_SIZE = 10F;

	public static Font BASE_FONT = new Font("SansSerif", Font.PLAIN, 12);
	public static Font TOOLTIP_FONT = BASE_FONT.deriveFont(DEFAULT_TOOLTIP_TEXT_FONT_SIZE);
	
	//misc
	public final static float DEFAULT_ARROW_HEAD_SIZE = 4F;
	


	private StyleManager() {
		defaultArcColors = new Color[defultArcColorCount];

		defaultArcColors[0] = new Color(253, 9, 2);
		defaultArcColors[1] = new Color(255, 50, 50);//red
		defaultArcColors[1] = new Color(11, 166, 150);//green blue
		defaultArcColors[2] = new Color(37, 234, 15);
		defaultArcColors[3] = new Color(217, 12, 222);//pink
		defaultArcColors[4] = new Color(4, 105, 230);//blue
		
	}
	
	public Stroke getArcStroke(Object arc){
		return new PFixedWidthStroke(DEFAULT_ARC_WIDTH);
	}

	public static void initStyleManager(Collection branchesTreeNode, Collection arcTypes) {
		generateNodeColors(branchesTreeNode.size());
		generateArcColors(arcTypes);

	}

	public static StyleManager getStyleManager() {
		if (instance == null) {
			instance = new StyleManager();
		}
		return instance;
	}

	/**
	 * generate colors apply for each branch
	 * 
	 * @param numBranches
	 *            , the number of branches
	 */
	private static void generateNodeColors(int numBranches) {

		nodeBackgroundColorMap = new HashMap();
		int numColors = numBranches + 1;
		nodeBackgroundColors = new ArrayList<Color>(numColors);
		float hueDistance = (NODE_HUE_END - NODE_HUE_START) / numColors;

		for (int i = 0; i < numColors; i++) {
			Color color = Color.getHSBColor(NODE_HUE_START + 100 + hueDistance
					* i, NODE_BACKGROUND_SATUATION, NODE_BACKGROUND_BRIGHTNESS);
			nodeBackgroundColors.add(color);
		}

		return;
	}

	private static void generateArcColors(Collection arcTypes) {
		arcColorMap = new HashMap();
		arcColors = new ArrayList<Color>(arcTypes.size());
		float hueDistance = (ARC_HUE_END - ARC_HUE_START) / arcTypes.size();

		for (int i = 0; i < defultArcColorCount; i++){
			arcColors.add(defaultArcColors[i]);
		}
		for (int i = defultArcColorCount; i < arcTypes.size(); i++) {
			Color color = Color.getHSBColor(ARC_HUE_START + hueDistance * i,
					ARC_SATUATION, ARC_BRIGHTNESS);
			arcColors.add(color);
		}
		
		int i = 0;
		for(Object o : arcTypes){
			arcColorMap.put(o, arcColors.get(i++));
		}
		
		
	}
	
	public Stroke getNodeBorderStroke(GraphNode graphNode){
		int level = TreeInfoManager.getTreeManager().getLevel(graphNode.getUserObject());
		float normalBorderStrokeWidth = this.MAX_BORDER_STROKE_WIDTH/level;
		float borderStrokeWidth = graphNode.isSelected()? normalBorderStrokeWidth: normalBorderStrokeWidth + this.BORDER_STROKE_WIDTH_SELECTION_INCREEMENT;
		return new PFixedWidthStroke(borderStrokeWidth);
		
	}

	public Color getNodeBackgroundColor(OWLEntity node) {

		TreeInfoManager treeInfoManager = TreeInfoManager.getTreeManager();

		Object branchEntity = treeInfoManager.getBranchEntity(node);

		if (nodeBackgroundColorMap.get(branchEntity) == null) {
			try {
				nodeBackgroundColorMap.put(branchEntity,
						nodeBackgroundColors.get(0));
				nodeBackgroundColors.remove(0);
			} catch (IndexOutOfBoundsException e) {
				int a = 1;
			}
		}

		Color branchColor = nodeBackgroundColorMap.get(branchEntity);
		int level = treeInfoManager.getLevel(node);
		int maxLevel = treeInfoManager.getTreeRoot().getMaxDepth();

		Color nodeColor = null;
		try {
			 nodeColor = new Color(branchColor.getRed(),
			 branchColor.getGreen(), branchColor.getBlue(), MAX_ALPHA
			 - (MAX_ALPHA - MIN_ALPHA) * (level - 1)
			 / (maxLevel - 1));

//			float[] hsbValue;
//			hsbValue = Color.RGBtoHSB(branchColor.getRed(),
//					branchColor.getGreen(), branchColor.getBlue(), null);
//			
//			nodeColor = Color.getHSBColor(hsbValue[0], hsbValue[1]  - (NODE_BACKGROUND_SATUATION - 0.1f) * (level - 1f)/(maxLevel -1f), hsbValue[2]);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return nodeColor;
	}

	public Color getNodeBorderColor(OWLEntity branchEntity) {
		Color backgroundColor = getNodeBackgroundColor(branchEntity);

		// ColorSpace colorSpace = new ColorSpace(ColorSpace.TYPE_HSV, 3);
		return backgroundColor.darker().darker();
	}

	public Color getArcColor(Object arc) {

		if (arcColorMap.get(arc) == null) {
			arcColorMap.put(arc, arcColors.get(0));
			arcColors.remove(0);
		}

		return arcColorMap.get(arc);
	}

}