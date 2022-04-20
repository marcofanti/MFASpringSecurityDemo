import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
 
public class App
{
	private static class Caller
	{
		private static final String behavioSenseUrl = "https://partner.behaviosec.com/BehavioSenseAPI/GetReport"; 
 
		// Parameters
		private String userId;
		private String timing;
		private String userAgent;
		private String ip;
		private String timestamp;
		private String sessionId;
		private String notes;
		private int reportFlags;
		private int operatorFlags;
 
		public Caller()
		{
			userId= "banco1";
			timing="[[\"w\",[{\"text#username\":8},{\"password#password\":8}],\"/BehavioSenseDemo/Login/\"],[\"f\",\"text#username\",[[0,84,1690],[1,84,1754],[0,69,1882],[1,69,1962],[0,83,2042],[1,83,2122],[0,84,2154],[1,84,2217],[0,85,2635],[1,85,2698],[0,83,2795],[1,83,2858],[0,69,2969],[1,69,3049],[0,82,3130],[1,82,3194]]],[\"fa\",\"password#password\",[[0,0,4427],[1,0,4474],[0,1,4522],[1,1,4587],[0,2,4714],[1,2,4778],[0,3,4874],[1,3,5003],[0,4,5034],[1,4,5113],[0,5,5243],[1,5,5306],[0,6,5387],[1,6,5450],[0,7,5546],[1,7,5627]]]]";
			userAgent="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
			ip = "127.0.0.1";
			timestamp = "1570200447200"; //Long.toString(Calendar.getInstance().getTimeInMillis());
			sessionId = "";
			notes = "GetReport API Test";
			reportFlags = 0;
			operatorFlags = 0;
		}
 
		public void postRequest() throws ClientProtocolException, IOException, AuthenticationException
		{
			// Create a new HttpClient and Post Header
			HttpClient httpclient = HttpClientBuilder.create().build();
			HttpPost httppost = new HttpPost(behavioSenseUrl);
 
			try
			{
				// Add your data
				System.out.println("timing" + timing);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("userid", userId));
				nameValuePairs.add(new BasicNameValuePair("timing", timing));
				nameValuePairs.add(new BasicNameValuePair("useragent", userAgent));
				nameValuePairs.add(new BasicNameValuePair("ip", ip));
				nameValuePairs.add(new BasicNameValuePair("timestamp", timestamp));
				nameValuePairs.add(new BasicNameValuePair("sessionid", sessionId));
				nameValuePairs.add(new BasicNameValuePair("notes", notes));
				nameValuePairs.add(new BasicNameValuePair("reportflags", Integer.toString(reportFlags)));
				nameValuePairs.add(new BasicNameValuePair("operatorflags", Integer.toString(operatorFlags)));
				nameValuePairs.add(new BasicNameValuePair("tenantId", "BancoDayCoval"));
				System.out.println("******** " + new UrlEncodedFormEntity( 
				nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
 
				System.out.println("HTTP POST Response:");
				System.out.println(EntityUtils.toString(entity));
			}
			catch (ClientProtocolException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 
	public static void main(String[] args) throws IOException, AuthenticationException
	{
		// Create caller object
		Caller caller = new Caller();
 
		// Perform POST request
		caller.postRequest();
		
	}
 
}