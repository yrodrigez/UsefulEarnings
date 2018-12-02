package es.usefulearnings.engine;


public interface Observer<E> {
	void onChange(final E entity);
}
