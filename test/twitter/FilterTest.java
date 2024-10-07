package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2024-01-01T10:00:00Z");
    private static final Instant d2 = Instant.parse("2024-01-01T11:00:00Z");
    private static final Instant d3 = Instant.parse("2024-01-01T12:00:00Z");
    private static final Instant d4 = Instant.parse("2024-01-01T13:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "Ali", "This is a tweet by Ali", Instant.parse("2024-01-01T11:00:00Z"));
    private static final Tweet tweet2 = new Tweet(2, "Bita", "This is a tweet by Bita", Instant.parse("2024-01-01T09:00:00Z"));
    private static final Tweet tweet3 = new Tweet(3, "Ali", "Another tweet by Ali", Instant.parse("2024-01-01T10:30:00Z"));
    private static final Tweet tweet4 = new Tweet(4, "Sara", "Good luck to everyone participating!", d4);
    
    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // ensure assertions are enabled
    }

    // Tests for writtenBy----------------------------------------------------------------------

    @Test
    public void testWrittenByNoResult() {
        List<Tweet> writtenByUnknown = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "Unknown");
        
        assertTrue("expected empty list", writtenByUnknown.isEmpty());
    }

    @Test
    public void testWrittenByMultipleResults() {
        Tweet tweet5 = new Tweet(5, "Ali", "Looking forward to the festival!", d1);
        List<Tweet> writtenByAli = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet5), "Ali");
        
        assertEquals("expected two tweets from Ali", 2, writtenByAli.size());
        assertTrue("expected list to contain tweet1", writtenByAli.contains(tweet1));
        assertTrue("expected list to contain tweet5", writtenByAli.contains(tweet5));
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenByAli = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALI");
        
        assertEquals("expected singleton list", 1, writtenByAli.size());
        assertTrue("expected list to contain tweet", writtenByAli.contains(tweet1));
    }
    
    @Test
    public void testWrittenBySingleResult() {
        List<Tweet> writtenByAli = Filter.writtenBy(Arrays.asList(tweet1), "Ali");
        
        assertEquals("expected singleton list", 1, writtenByAli.size());
        assertTrue("expected list to contain tweet", writtenByAli.contains(tweet1));
    }

    // Tests for inTimespan-----------------------------------------------------------------------
    @Test
    public void testInTimespanMultipleResults() {
        Instant testStart = Instant.parse("2024-01-01T09:00:00Z");
        Instant testEnd = Instant.parse("2024-01-01T14:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2, tweet3, tweet4)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }

    @Test
    public void testInTimespanNoResults() {
        Instant testStart = Instant.parse("2024-01-01T14:00:00Z");
        Instant testEnd = Instant.parse("2024-01-01T15:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(testStart, testEnd));
        
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanSingleResult() {
        Instant testStart = Instant.parse("2024-01-01T10:00:00Z");
        Instant testEnd = Instant.parse("2024-01-01T11:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertEquals("expected single result", 1, inTimespan.size());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
    }

    @Test
    public void testInTimespanBoundaryConditions() {
        Instant testStart = Instant.parse("2024-01-01T10:00:00Z");
        Instant testEnd = Instant.parse("2024-01-01T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
        assertFalse("expected list not to contain tweet2", inTimespan.contains(tweet2));
    }

    // Tests for containing
    @Test
    public void testContainingSingleWordMatch() {
        List<Tweet> containingCricket = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("cricket"));
        
        assertTrue("expected empty list", containingCricket.isEmpty());
    }


    @Test
    public void testContainingNoMatch() {
        List<Tweet> containingNoMatch = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("football"));
        
        assertTrue("expected empty list", containingNoMatch.isEmpty());
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containingGoodLuck = Filter.containing(Arrays.asList(tweet4), Arrays.asList("GOOD LUCK"));
        
        assertFalse("expected non-empty list", containingGoodLuck.isEmpty());
        assertTrue("expected list to contain tweet4", containingGoodLuck.contains(tweet4));
    }

}
