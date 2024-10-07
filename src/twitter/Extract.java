package twitter;

import java.util.List;
import java.util.Set;
import java.time.Instant;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        
    	if (tweets.isEmpty()) {
    		return null;
    	}
    	
    	Instant first_dt = tweets.get(0).getTimestamp();
    	Instant last_dt = tweets.get(0).getTimestamp();    	
    	
    	for (Tweet ts: tweets) {
    		Instant time_stamp = ts.getTimestamp();
    		if(time_stamp.isBefore(first_dt)) {
    			first_dt = time_stamp;
    		}
    		
    		if (time_stamp.isAfter(last_dt)) {
    			last_dt = time_stamp;
    		}
    	}
    	return new Timespan(first_dt, last_dt);
    }

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
    	 Set<String> mentionedUsers = new HashSet<>();

         Pattern pattern = Pattern.compile("(?<![\\w])@(\\w{1,15})(?![\\w])", Pattern.CASE_INSENSITIVE);

         for (Tweet tweet : tweets) {
             String text = tweet.getText();
             Matcher matcher = pattern.matcher(text);
             while (matcher.find()) {
                 String username = matcher.group(1).toLowerCase();
                 mentionedUsers.add(username);
             }
         }

         return mentionedUsers;
     }  

}
