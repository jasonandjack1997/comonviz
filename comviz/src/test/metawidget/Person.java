package metawidget;

public class Person {
	private String	mName;
	private int		mAge;
	private boolean	mRetired;

	public String getName() { return mName; }
	public void setName( String name ) { mName = name; }

	public int getAge() { return mAge; }
	public void setAge( int age ) { mAge = age; }

	public boolean isRetired() { return mRetired; }
	public void setRetired( boolean retired ) { mRetired = retired; }
	
	public String getType(){
		return "hehe";
	}
	
	public void setType(String type){
		return;
	}
}