package project.domain;

import org.junit.Test;
import project.domain.enums.Priority;
import project.domain.model.Article;
import project.domain.model.Event;
import project.domain.model.WorkStation;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Event} class, which represent events associated with items and workstations.
 * Includes tests to verify the behavior of the {@code getItem}, {@code getWorkStation},
 * {@code getStartTime}, {@code getEndTime}, {@code equals}, {@code notEquals}, and {@code compareTo} methods.
 */
public class EventTest {

    /**
     * Tests the {@link Event#getArticle()} method, verifying that it correctly returns the item associated with the event.
     */
    @Test
    public void testGetArticle() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event = new Event(article, workStation, 10);

        assertEquals("Item1", event.getArticle().getIdArticle());
    }

    /**
     * Tests the {@link Event#getWorkStation()} method, verifying that it correctly returns the workstation associated with the event.
     */
    @Test
    public void testGetWorkStation() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event = new Event(article, workStation, 10);

        assertEquals("WS1", event.getWorkStation().getWorkStationID());
    }

    /**
     * Tests the {@link Event#getStartTime()} method, verifying that it correctly returns the event's start time.
     */
    @Test
    public void testGetStartTime() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event = new Event(article, workStation, 10);

        assertEquals(10, event.getStartTime());
    }

    /**
     * Tests the {@link Event#getEndTime()} method, verifying that it correctly returns the event's end time.
     */
    @Test
    public void testGetEndTime() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event = new Event(article, workStation, 10);

        assertEquals(40, event.getEndTime());
    }

    /**
     * Tests the {@link Event#equals(Object)} method, verifying that two events with identical properties are considered equal.
     */
    @Test
    public void testEquals() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event1 = new Event(article, workStation, 10);
        Event event2 = new Event(article, workStation, 10);

        assertEquals(event1, event2);
    }

    /**
     * Tests the {@link Event#equals(Object)} method, verifying that two events with different properties are not considered equal.
     */
    @Test
    public void testNotEquals() {
        Article article1 = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        Article article2 = new Article("Item2", Priority.LOW, Arrays.asList("Polishing", "Painting"), 2);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event1 = new Event(article1, workStation, 10);
        Event event2 = new Event(article2, workStation, 10);

        assertNotEquals(event1, event2);
    }

    /**
     * Tests the {@link Event#compareTo(Event)} method, verifying that events are ordered correctly based on start time.
     */
    @Test
    public void testCompareTo() {
        Article article = new Article("Item1", Priority.HIGH, Arrays.asList("Cutting", "Drilling"), 1);
        WorkStation workStation = new WorkStation("WS1", "Cutting", 30);
        Event event1 = new Event(article, workStation, 10);
        Event event2 = new Event(article, workStation, 15);
        Event event3 = new Event(article, workStation, 5);

        assertTrue(event1.compareTo(event2) < 0);
        assertTrue(event2.compareTo(event1) > 0);
        assertTrue(event1.compareTo(event3) > 0);
    }
}
