package mvvm.retrofit;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import mvvm.model.Appointment;
import mvvm.model.Notification;
import mvvm.model.Pet;
import mvvm.model.User;
import mvvm.model.Vet;


public class Parser {



    public static User parseUser(JSONObject jsonObject) throws JSONException
    {
        User u = new User(jsonObject.getInt("user_id"),
                jsonObject.getString("username"),
                jsonObject.getString("user_firstName"),
                jsonObject.getString("user_lastName"),
                jsonObject.getString("user_email"),
                jsonObject.getString("user_password"),
                jsonObject.getInt("user_number"),
                jsonObject.getString("profileImgUrl"),
                jsonObject.getString("role"));
        return u;
    }

    public static Pet parsePet(JSONObject jsonObject) throws JSONException
    {
        Pet pet = new Pet(jsonObject.getInt("pet_id"),
                jsonObject.getString("name"),
                jsonObject.getString("species"),
                jsonObject.getString("gender"),
                jsonObject.getString("breed"),
                jsonObject.getInt("age"),
                (float) jsonObject.getDouble("weight"),
                jsonObject.getBoolean("neutered"),
                jsonObject.getBoolean("vaccinated"),
                jsonObject.getString("petImgUrl"));
        return pet;
    }

    public static Vet parseVet(JSONObject jsonObject) throws JSONException
    {
        Vet v = new Vet(jsonObject.getInt("vet_id"),
                jsonObject.getString("vet_firstName"),
                jsonObject.getString("vet_lastName"),
                jsonObject.getString("vetImgUrl"));
        return v;
    }

    public static Appointment parseAppointment(JSONObject jsonObject) throws JSONException
    {
        Appointment appointment = new Appointment(jsonObject.getInt("appointment_id"),
                parsePet(jsonObject.getJSONObject("pet")),
                parseVet(jsonObject.getJSONObject("vet")),
                jsonObject.getString("appointment_date"),
                jsonObject.getString("appointment_reason"),
                jsonObject.getBoolean("confirmed"),
                parseUser(jsonObject.getJSONObject("user"))
               );
        return appointment;
    }

    public static Notification parseNotification(JSONObject jsonObject) throws JSONException, ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = sdf.parse(jsonObject.getString("date"));

        Notification notification = new Notification(
                    jsonObject.getInt("notification_id"),
                    parseUser(jsonObject.getJSONObject("user")),
                    jsonObject.getString("content"),
                    date,
                    jsonObject.getBoolean("seen"),
                    jsonObject.getBoolean("forAdmin"));

        /*if(jsonObject.getJSONObject("pet") != null)
            notification.setPet(parsePet(jsonObject.getJSONObject("pet")));*/
        return notification;
    }

    public static List<Notification> parseNotificationList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Notification> foundNotifications = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Notification notification = parseNotification(jsonObject);

            foundNotifications.add(notification);
        }

        return foundNotifications;
    }

    public static List<Appointment> parseAppointmentList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Appointment> foundAppointments = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Appointment appointment = parseAppointment(jsonObject);

            foundAppointments.add(appointment);
        }

        return foundAppointments;
    }

    public static List<Pet> parsePetList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Pet> foundPets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Pet pet = parsePet(jsonObject);

            foundPets.add(pet);
        }

        return foundPets;
    }

    public static List<Vet> parseVetList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Vet> foundVets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Vet vet = parseVet(jsonObject);

            foundVets.add(vet);
        }

        return foundVets;
    }

    /*public static User parseFullUser(JSONObject jsonObject) throws JSONException
    {
        User u = new User(jsonObject.getInt("user_id"),
                jsonObject.getString("username"),
                jsonObject.getString("user_firstName"),
                jsonObject.getString("user_lastName"),
                jsonObject.getString("user_email"),
                jsonObject.getString("user_password"),
                jsonObject.getString("role"),
                jsonObject.getString("user_bio"),
                jsonObject.getInt("followersCount"),
                jsonObject.getInt("followingCount"),
                jsonObject.getString("profileImgUrl"),
                jsonObject.getBoolean("isPrivate"),
                jsonObject.getBoolean("isOnline"),
                jsonObject.getInt("postsCount"),
                parseUserList(jsonObject.getJSONArray("followers")),
                parseUserList(jsonObject.getJSONArray("following")));
        return u;
    }

    public static List<User> parseUserList(JSONArray jsonArray) throws JSONException {
        List<User> foundUsers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            foundUsers.add(parseUser(jsonObject));
        }
        return foundUsers;
    }

    public static List<Comment> parseCommentList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Comment> foundComments = new ArrayList<>();


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(jsonObject.getString("date"));

            Comment comment = new Comment(jsonObject.getInt("id"),
                                        jsonObject.getString("content"),
                                        date,
                                        parseUser(jsonObject.getJSONObject("user")),
                                        parseUserList(jsonObject.getJSONArray("likes")),
                                        jsonObject.getInt("likesCount"));

            foundComments.add(comment);
        }

        return foundComments;
    }

    public static Message parseMessage(JSONObject jsonObject) throws JSONException, ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
        Date date = sdf.parse(jsonObject.getString("date"));

        Message m = new Message(jsonObject.getInt("id"),
                jsonObject.getString("content"),
                jsonObject.getString("status"),
                date,
                parseUser(jsonObject.getJSONObject("sender")),
                parseUser(jsonObject.getJSONObject("reciever")));
        return m;
    }

    public static List<Message> parseMessageList(JSONArray jsonArray) throws JSONException, ParseException
    {
        List<Message> foundMessages = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            foundMessages.add(parseMessage(jsonObject));
        }

        return foundMessages;
    }

    public static Message parsePartialMessage(JSONObject jsonObject) throws JSONException, ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-1"));
        Date date = sdf.parse(jsonObject.getString("date"));

        Message m = new Message(jsonObject.getInt("id"),
                jsonObject.getString("content"),
                jsonObject.getString("status"),
                date);
        return m;
    }

    public static List<Message> parsePartialMessageList(JSONArray jsonArray) throws JSONException, ParseException
    {
        List<Message> foundMessages = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            foundMessages.add(parsePartialMessage(jsonObject));
        }

        return foundMessages;
    }



    public static List<Conversation> parseConversationList(JSONArray jsonArray) throws JSONException, ParseException {
        List<Conversation> foundConversations = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject jsonObject = jsonArray.getJSONObject(i);



            Conversation conversation = new Conversation(jsonObject.getInt("id"),
                    parseUser(jsonObject.getJSONObject("first_user")),
                    parseUser(jsonObject.getJSONObject("second_user")),
                    parsePartialMessageList(jsonObject.getJSONArray("messages")));

            foundConversations.add(conversation);
        }

        return foundConversations;
    }
*/

}
