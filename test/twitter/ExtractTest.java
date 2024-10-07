package twitter;

import static org.junit.Assert.*;
import java.util.HashSet;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-03-17T11:00:00Z");
    //custom dates-----------------------------------------------------------
    private static final Instant d3 = Instant.parse("2017-03-18T01:00:00Z");
    private static final Instant d4 = Instant.parse("2017-04-17T01:00:00Z");
    private static final Instant d5 = Instant.parse("2018-04-17T01:00:00Z");
    
   
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    //custom tweets-----------------------------------------------------------
    private static final Tweet tweet3 = new Tweet(3, "abdu	", "This book belongs to @ali", d3);
    private static final Tweet tweet4 = new Tweet(4, "abdulrehman", "I like to drive a car like @abdu, hehehhe", d4);
    private static final Tweet tweet5 = new Tweet(5, "lebron", "i think @kareem is the greatest", d5);
    private static final Tweet tweet6 = new Tweet(6, "lebron", "i think @kareem is the greatest", d5);
    private static final Tweet tweet7 = new Tweet(7, "test22", "@ali This is a test tweet. And @kareem too!", d3);
    private static final Tweet tweet8 = new Tweet(8, "test4", "using invalid name @ . enddd", d3);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; 
    }

    
    // time span func tests-----------------------------------------------------
    @Test
    //this test simple two tweets times
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test 
    //this test on multiple tweet's timeline
    public void testGetTimespan_N_tweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
    	
    	assertEquals(d1, timespan.getStart());
    	assertEquals(d4, timespan.getEnd());
    	
    }
    
    @Test 
    //this test start and end date on a single tweet
    public void testGetTimespanSingleTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
    	
    	assertEquals(d1, timespan.getStart());
    	assertEquals(d1, timespan.getEnd());
    	
    }
    
    @Test
    // this test on a an empty list
    public void testGetTimespanNoTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList());
    	
    	assertEquals(null, timespan);
    }
    
    
    @Test
    // test same dates on two tweets
    public void testGetTimespanSameTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet5, tweet6));
    	
    	assertEquals(d5, timespan.getStart());
    	assertEquals(d5, timespan.getEnd());
    }
    
    @Test
    // test same dates on two tweets
    public void testGetTimespanMultipleSameTweets() {
    	Timespan timespan = Extract.getTimespan(Arrays.asList(tweet5, tweet6, tweet7, tweet8));
    	
    	assertEquals(d3, timespan.getStart());
    	assertEquals(d5, timespan.getEnd());
    }
    
    
    // username func tests--------------------------------------------------------
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        Set<String> expectedUser = new HashSet<>(Arrays.asList("ali"));
        assertEquals(expectedUser, mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersTwoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        Set<String> expectedUser = new HashSet<>(Arrays.asList("ali", "abdu"));
        assertEquals(expectedUser, mentionedUsers);
    } 
    
    @Test
    public void testGetMentionedUsersmultipleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4, tweet5));
        Set<String> expectedUser = new HashSet<>(Arrays.asList("ali", "abdu", "kareem"));
        assertEquals(expectedUser, mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersDuplicateMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet5, tweet6));
        Set<String> expectedUser = new HashSet<>(Arrays.asList("kareem"));
        assertEquals(expectedUser, mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersStartandEndMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet7));
        Set<String> expectedUser = new HashSet<>(Arrays.asList("ali", "kareem"));
        assertEquals(expectedUser, mentionedUsers);
    }
    
    @Test
    public void testGetMentionedUsersInvalidMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet8));
        
        assertTrue(mentionedUsers.isEmpty());
    }
}
