package model;

import java.util.ArrayList;
import java.util.List;

public class Field {

	private final int line;
	private final int colunm;
	
	private boolean open = false;
	private boolean undermined = false;
	private boolean marked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	private List<FieldObserver> observers = new ArrayList<>();
	
	public void registerObserver(FieldObserver observer) {
		observers.add(observer);
	}
	
	private void notifyObserver(EventField event) {
		observers.stream()
		.forEach(o -> o.eventOccurred(this, event));
	}
	
	Field(int line, int colunm){
		this.line = line;
		this.colunm = colunm;
	}
	boolean addNeighbor(Field neighbor) {
	boolean differentLine = line != neighbor.line;
	boolean differentColunm = colunm != neighbor.colunm;
	boolean diagonal = differentLine && differentColunm;
	
	int deltaLine = Math.abs(line - neighbor.line);
	int deltaColunm = Math.abs(colunm - neighbor.colunm);
	int deltaGeneral = deltaLine + deltaColunm;
		
	if(deltaGeneral == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;
	} else if(deltaGeneral == 2 && diagonal) {
			neighbors.add(neighbor);
			return true;
	}else {
			return false;
		}
	}
	
	public void togglMarkup() {
		if(!open) {
			marked = !marked;
			
			if(marked) {
				notifyObserver(EventField.MARK);
			}else {
				notifyObserver(EventField.MARKOFF);
			}
		}
	}
	
	public boolean unclose() {

		if(!open && !marked) {
			if(undermined) {
				notifyObserver(EventField.EXPLODE);
				return true;
			}
			
			setOpen(true);
			
			if(neighborSecurity()) {
				neighbors.forEach(n -> n.unclose());
			 }
			
				return true;
		} else {
				return false;
		}
	}
	
	public boolean neighborSecurity() {
		return neighbors.stream().noneMatch(n -> n.undermined);
	}
	
	void udermine() {
			undermined = true;
	}
	
	public boolean isUdermined() {
			return undermined;
	}
	
	public boolean isMarked() {
		return marked; 
	}
	
	void setOpen(boolean open) {	
		this.open = open;
	
		if(open) {
		notifyObserver(EventField.OPEN);
	
		}
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public boolean isClose() {
		return !isOpen();
	}
	
	public int getLine() {
		return line;
	}
	
	public int getColunm() {
		return colunm;
	}
	
	boolean achievedobject() {
		boolean unraveled = !undermined && open;
		boolean secure = undermined && marked;
		return unraveled || secure;
	}
	
	public int minesInTheNeighbors() {
		return (int) 
		neighbors.stream()
		.filter(n -> n.undermined)
		.count();
	}
	
	void restart() {
		open = false;
		undermined = false;
		marked = false;
		notifyObserver(EventField.RESTART);
	}
	
}



