/**
 * classDiagram
    class EventManager {
        -listeners: List~EventListener~
        +subscribe(listener: EventListener): void
        +unsubscribe(listener: EventListener): void
        +notify(event: Event): void
    }

    class EventListener {
        <<interface>>
        +onEvent(event: Event): void
    }

    class Event {
        -eventType: String
        -data: Object
        +Event(eventType: String, data: Object)
        +getEventType(): String
        +getData(): Object
    }

    class ConcreteListenerA {
        +onEvent(event: Event): void
    }

    class ConcreteListenerB {
        +onEvent(event: Event): void
    }

    EventManager "1" -- "*" EventListener : listeners
    EventListener <|.. ConcreteListenerA
    EventListener <|.. ConcreteListenerB
    EventManager "1" -- "*" Event : events
 * **/

import java.util.ArrayList;
import java.util.List;


class EventManager{
    private List<EventListener> listeners = new ArrayList<>();;

    protected void subscribe(EventListener listener){
        listeners.add(listener);
    }
    protected void unsubscribe(EventListener listener){
        listeners.remove(listener);
    }
    protected void notify(Event event){
        System.out.println("Event notification "+event.getEventType());
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
class Event{
    private String eventType;
    private Object data;
    protected Event(){};
    protected Event(String eventType,Object data){
        this.eventType = eventType;
        this.data = data;
    }
    protected String getEventType(){
        return eventType;
    }
    protected Object getData(){
        return data;
    }

}
abstract class EventListener{
    // protected interface ListenerEvent{
    //     void onEvent(Event event);
    // }
    protected abstract void onEvent(Event event);
}

class ConcreteListenerA extends EventListener{
    @Override
    protected void onEvent(Event event){
        System.out.println("ConcreteListenerA() --> EventType "+event.getEventType());
        System.out.println("ConcreteListenerA() --> data "+event.getData());
    }

}
class ConcreteListenerB extends EventListener{
    @Override
    protected void onEvent(Event event){
        System.out.println("ConcreteListenerB() --> EventType "+event.getEventType());
        System.out.println("ConcreteListenerB() --> data "+event.getData());
    }
}


class EventManagerHandler{
    public static void main(String[] args) {
        EventManager eventManager = new EventManager();
        ConcreteListenerA listenerA = new ConcreteListenerA();
        ConcreteListenerB listenerB = new ConcreteListenerB();

        eventManager.subscribe(listenerA);
        eventManager.subscribe(listenerB);

        Event event1 = new Event("Type1", "Data1");
        eventManager.notify(event1);

        eventManager.unsubscribe(listenerA);

        Event event2 = new Event("Type2", "Data2");
        eventManager.notify(event2);

    }
 }
