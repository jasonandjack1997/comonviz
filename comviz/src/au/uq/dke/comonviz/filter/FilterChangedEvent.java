package au.uq.dke.comonviz.filter;


public class FilterChangedEvent {

	private FilterManager filterManager;

	public FilterChangedEvent(FilterManager filterManager) {
		this.filterManager = filterManager;
	}
	
	public FilterManager getFilterManager() {
		return filterManager;
	}
	
}
