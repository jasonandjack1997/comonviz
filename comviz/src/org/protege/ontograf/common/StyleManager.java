package org.protege.ontograf.common;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author uqwwan10
 * manage the size, font, color, pound of  border, background, line, label,etc
 */
public class StyleManager {
	
	private static StyleManager instance;

	private static List<Color> 	nodeBackgroundColors;
	private static List<Color>	arcColors;
	
	private static Map<Object, Color> 	nodeBackgroundColorMap;
	private static Map<Object, Color>	arcColorMap;
	
	final private static float NODE_BACKGROUND_SATUATION = 0.1F;
	final private static float NODE_BACKGROUND_BRIGHTNESS = 0F;
	final private static float NODE_HUE_START = 0F;
	final private static float NODE_HUE_END = 1F;
	
	/**
	 * border is simply darker than the background
	 */
	final private static float NODE_BORDER_BRIGHTNESS_GAIN = 0.2F;
	
	final private static float ARC_SATUATION = 0.1F;
	final private static float ARC_BRIGHTNESS = 0F;
	final private static float ARC_HUE_START = 0F;
	final private static float ARC_HUE_END = 1F;
	
	private StyleManager(){
		
	}
	
	public static StyleManager getStyleManager(int numBranches, int numArcTypes){
		if(instance == null){
			instance = new StyleManager();
			generateNodeColors(numBranches);
			generateArcColors(numArcTypes);
		}
		return instance;
	}
	/**
	 * generate colors apply for each branch
	 * @param numBranches, the number of branches
	 */
	private static void generateNodeColors(int numBranches){
		nodeBackgroundColors = new ArrayList<Color>(numBranches);
		float hueDistance = (NODE_HUE_END - NODE_HUE_START)/numBranches;
		
		for(int i = 0; i< numBranches; i++){
			Color color = Color.getHSBColor(NODE_HUE_START + hueDistance * i, NODE_BACKGROUND_SATUATION, NODE_BACKGROUND_BRIGHTNESS);
			nodeBackgroundColors.add(color);
		}
	}
	
	private static void generateArcColors(int numArcTypes){
		arcColors = new ArrayList<Color>(numArcTypes);
		float hueDistance = (ARC_HUE_END - ARC_HUE_START)/numArcTypes;
		
		for(int i = 0; i< numArcTypes; i++){
			Color color = Color.getHSBColor(ARC_HUE_START + hueDistance * i, ARC_SATUATION, ARC_BRIGHTNESS);
			arcColors.add(color);
		}
	}
	
	public  Color getNodeBackgroundColor(Object branchEntity){
		if(nodeBackgroundColorMap == null){
			nodeBackgroundColorMap = new HashMap();
		}
		
		if(nodeBackgroundColorMap.get(branchEntity) == null){
			nodeBackgroundColorMap.put(branchEntity, nodeBackgroundColors.get(0));
			nodeBackgroundColors.remove(0);
		}
		
		return nodeBackgroundColorMap.get(branchEntity);
	}

	public  Color getNodeBorderColor(Object branchEntity){
		Color backgroundColor = getNodeBackgroundColor(branchEntity);
		
		//ColorSpace colorSpace = new ColorSpace(ColorSpace.TYPE_HSV, 3);
		return backgroundColor.darker();
	}

	public  Color getArcColor(Object arc){
		if(arcColorMap == null){
			arcColorMap = new HashMap();
		}
		
		if(arcColorMap.get(arc) == null){
			arcColorMap.put(arc, arcColors.get(0));
			arcColors.remove(0);
		}
		
		return arcColorMap.get(arc);
	}
}
