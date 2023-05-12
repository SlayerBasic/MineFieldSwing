package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver {
	
	private final int lines;
	private final int colunms;
	private final int mines;
	
	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<EventResult>> observers =
			new ArrayList<>();
	

	public Board(int lines, int colunms, int mines) {
		this.lines = lines;
		this.colunms = colunms;
		this.mines = mines;

		generateFields();
		associateNeighbors();
		raffleMines();
	}
	
	public void forEach(Consumer<Field> function) {
		fields.forEach(function);
	}
	
	public void registerObserver(Consumer<EventResult> observer) {
		observers.add(observer);
	}
	
	private void notifyObserver(Boolean result) {
		observers.stream()
		.forEach(o -> o.accept(new EventResult(result)));
	}
	
	public void open(int lines, int colunms) {
			fields.parallelStream()
					.filter(f -> f.getLine() == lines && f.getColunm() == colunms)
					.findFirst()
					.ifPresent(f -> f.unclose());;
	}
	
	public void alternateMarking(int line, int colunm) {
		fields.parallelStream()
		.filter(f -> f.getLine() == line && f.getColunm() == colunm)
		.findFirst()
		.ifPresent(f -> f.togglMarkup());;
	}
	
	private void generateFields() {
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < colunms; column++) {
				Field field = new Field(line, column);
				field.registerObserver(this);
				fields.add(field);
			}
		}
	}
	
	private void associateNeighbors() {
		for(Field f1: fields) {
			for(Field f2: fields) {
				f1.addNeighbor(f2);
			}
		}
	}
	
	private void raffleMines() {
		long armedMines = 0;
		Predicate<Field> undermined = f -> f.isUdermined();
		do {
				int random = (int) (Math.random() * fields.size());
				fields.get(random).udermine();
				armedMines = fields.stream().filter(undermined).count();
		}while(armedMines < mines);
	}
	
	public boolean achievedobject() {
		return fields.stream()
				.allMatch(f -> f.achievedobject());
	}
	
	public void restart() {
		fields.stream().forEach(f  -> f.restart());
		raffleMines();
	}
	
	public int getLines() {
		return lines;
	}
	
	public int getColunms() {
		return colunms;
	}
	
	public int getMines() {
		return mines;
	}
	@Override
	public void eventOccurred(Field field, EventField event) {
		if(event == EventField.EXPLODE) {
			showMines();
			notifyObserver(false);
		}else if(achievedobject()){
			notifyObserver(true);
		}
	}
	
	private void showMines() {
		fields.stream()
		.filter(f -> f.isUdermined())
		.filter(f -> !f.isMarked())
		.forEach(f -> f.setOpen(true));
	}
		
}

