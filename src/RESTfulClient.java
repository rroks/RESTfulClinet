

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.http.entity.mime.content.FileBody;


import javax.jws.WebService;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * The create part now can add an image when creating a new user
 * but it works well when create a new user with out an image
 *
 *
 */
@WebService()
public class RESTfulClient {
    private static String theUrl = "http://localhost:3000/api/users/";
    private static Logger logger = LoggerFactory.getLogger(RESTfulClient.class);
    public static void main(String[] argv) throws IOException {
        boolean flag = true;
        while(flag){
            displayMenu();
            Scanner scanner = new Scanner(System.in);
            String  option = scanner.nextLine();
            switch (option){
                case "1":
                    System.out.println("Displaying users: ");
                    displayUsers();
                    break;
                case "2":
                    System.out.println("Displaying user: ");
                    displayUser();
                    break;
                case "3":
                    System.out.println("Creating user: ");
                    creatUser();
                    break;
                case "4":
                    System.out.println("Updating user: ");
                    updateUser();
                    break;
                case "5":
                    System.out.println("Deleting user: ");
                    deleteUser();
                    break;
                case "Q":case "q":
                    flag = false;
                    break;
                default:
                    break;
            }
        }
    }

    public static void displayMenu(){
        System.out.println("Enter option:");
        System.out.println("1. Display users");
        System.out.println("2. Display user by ID");
        System.out.println("3. Create new user");
        System.out.println("4. Update user by ID");
        System.out.println("5. Delete user by ID");
        System.out.println("Q. Quite");
    }

    private static void displayUsers() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(theUrl);
        HttpResponse response = client.execute(request);
        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);
        List list = null;
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            list = com.alibaba.fastjson.JSON.parseArray(result);
            System.out.println(response.getStatusLine().getStatusCode());
            Iterator iter = list.iterator();
            while(iter.hasNext()) {
                String js = iter.next().toString();
                JSONObject jsonobject = (JSONObject) JSONObject.parse(js);
                for (String key: jsonobject.keySet()){
                    System.out.println(key + ": " + jsonobject.get(key));
                }
            }
        } else {
            logger.error("Failed.");
        }
    }

    private static void displayUser() throws IOException {
        System.out.print("Enter the user ID: ");
        int id = Integer.parseInt(new Scanner(System.in).nextLine());

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(theUrl + id);
        HttpResponse response = client.execute(request);
        HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){//check connected
            System.out.println(result);
            JSONObject jsonobject = (JSONObject) com.alibaba.fastjson.JSON.parse(result);
            for (String key: jsonobject.keySet()){
                System.out.println(key + ": " + jsonobject.get(key));
            }
        } else {
            logger.error("Failed to find user.");
        }
    }

    private static void creatUser() throws IOException {
        System.out.print("Surname: ");
        String surname = new Scanner(System.in).nextLine();
        System.out.print("Firstname: ");
        String firstname = new Scanner(System.in).nextLine();
        System.out.print("Phone: ");
        String phone = new Scanner(System.in).nextLine();
        System.out.print("Graduating year: ");
//    int year = Integer.parseInt(new Scanner(System.in).nextLine());
        String year = new Scanner(System.in).nextLine();
        System.out.print("Require job information? (y/n): ");
        String answer = new Scanner(System.in).nextLine();
        boolean jobs = answer.equalsIgnoreCase("Y");
        System.out.print("Email: ");
        String email = new Scanner(System.in).nextLine();

//        System.out.print("Filename path: ");
//        String filename = new Scanner(System.in).nextLine();
        /**
         * get filename
         */
//    System.out.print("Filename path: ");
//    String filename = new Scanner(System.in).nextLine();

        /**
         * client request
         */
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(theUrl);

        /**
         * get user text
         */
        JSONObject jso = new JSONObject();
        jso.put("surname", surname);
        jso.put("firstname", firstname);
        jso.put("phone", phone);
        jso.put("grad_year", year);
        jso.put("jobs", jobs);
        jso.put("email", email);
//        jso.put("image_file", filename);

        /**
         * this part is used to put user text and image path together to be an entity
         */
//    "/users/Rroks/JaProj/csaclient/" +
//    FileBody imageFile = new FileBody(new File(filename));
//    StringBody sb = new StringBody(jso.toJSONString(), ContentType.APPLICATION_JSON);
//    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//    builder.addPart("user", sb);
//    builder.addPart("image_file", imageFile);

        /**
         *  only makes user an entity
         */
        StringEntity sentity = new StringEntity(jso.toJSONString());
        sentity.setContentType("application/json");
        sentity.setContentEncoding("UTF-8");

        /**
         * for user & image
         */
//    HttpEntity httpentity = builder.build();
//    httpPost.setEntity(httpentity);
        /**
         * for user only
         */
        httpPost.setEntity(sentity);


        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == 201){
            System.out.println("Created successfully");
        } else {
            logger.error("Failed to create user.");
        }
        String resData = EntityUtils.toString(response.getEntity());
        System.out.print(resData);
    }

    private static void updateUser() throws IOException {
        System.out.print("Enter the user ID: ");
        int id = Integer.parseInt(new Scanner(System.in).nextLine());

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(theUrl + id);
        CloseableHttpResponse response = (CloseableHttpResponse) client.execute(request);
        HttpEntity httpEntity = response.getEntity();

        String result = EntityUtils.toString(httpEntity);
        System.out.println(result);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            JSONObject jsonObject = (JSONObject) com.alibaba.fastjson.JSON.parse(result);
            for (String key: jsonObject.keySet()){
                System.out.println(key + ": " + jsonObject.get(key));
            }

            /**
             * check not to change id, updated time and created time
             */
            jsonObject.keySet().stream().filter(key ->
                    !(key.equals("id") || key.equals("updated_at") || key.equals("created_at")))
                    .forEach(key -> {
                        System.out.print(key + ": " + jsonObject.get(key) + '\n' + "new " + key + "?: ");
                        String value = new Scanner(System.in).nextLine();
                        /**
                         * change the old thing if the input is not blank
                         */
                        if (!value.equals("")) {
                            jsonObject.put(key, value);
                        }
                    });
            StringEntity sentity = new StringEntity(jsonObject.toJSONString());
            /**
             *  set the output type json
             */
            sentity.setContentType("application/json");
            sentity.setContentEncoding("UTF-8");
            System.out.println(sentity);
            HttpPut httpPut = new HttpPut(theUrl + id);
            httpPut.setEntity(sentity);
            HttpResponse updateResponse = client.execute(httpPut);

            if (updateResponse.getStatusLine().getStatusCode() == 201){
                System.out.println("Updated successfully");
            } else {
                logger.error("Failed to update.");
            }
            response.close();
        } else {
            logger.error("Failed to find user.");
        }
    }

    private static void deleteUser() throws IOException {
        System.out.print("Enter the user ID: ");
        int id = Integer.parseInt(new Scanner(System.in).nextLine());
        HttpClient client = HttpClientBuilder.create().build();
        HttpDelete delete = new HttpDelete(theUrl + id);
        HttpResponse response = client.execute(delete);
        if (response.getStatusLine().getStatusCode() == 204){
            System.out.println("Deleted successfully.");
        } else {
            logger.error("Failed to delete.");
        }
    }
}