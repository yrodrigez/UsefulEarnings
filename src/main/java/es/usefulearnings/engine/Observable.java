package es.usefulearnings.engine;

public interface Observable<E> {
	void onChange();
	void addListeners(final Observer<E> ... observer);
}
