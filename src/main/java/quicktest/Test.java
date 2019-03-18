package quicktest;

import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections4.list.CursorableLinkedList;
import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.collections4.list.TreeList;

public class Test {

	public static void main(String args[]) {
		Test listExample = new Test();
	    listExample.createLists();

	    uniqueList.add("Value1");
	    uniqueList.add("Value1"); 
	    System.err.println(uniqueList); // should contain only one element

	    cursorList.add("Element1"); 
	    cursorList.add("Element2"); 
	    cursorList.add("Element3"); 

	    ListIterator iterator = cursorList.listIterator();
	    iterator.next(); // cursor now between 0th and 1st element
	    iterator.add("Element2.5"); // adds this between 0th and 1st element

	    System.err.println(cursorList); // modification done to the iterator are visible in the list
	  }

	  private void createLists() {
	   // uniqueList = SetUniqueList.decorate(new TreeList());
	    uniqueList = SetUniqueList.setUniqueList(new TreeList<String>());
	   
	    cursorList = new CursorableLinkedList();
	  }

	  private static SetUniqueList<String> uniqueList;
	  private static List cursorList;
}
