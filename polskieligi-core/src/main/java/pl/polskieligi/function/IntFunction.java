package pl.polskieligi.function;

@FunctionalInterface
public interface IntFunction<T, U> {
	public void apply(T t, U u);
}
