package patterns;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private final List<IObserver> observers = new ArrayList<>();

    public void attach(IObserver observer) {
        observers.add(observer);
    }
    public void detach(IObserver observer) {
        observers.remove(observer);
    }
    public void notifyObservers() {
        for (IObserver o : observers) o.update();
    }
}