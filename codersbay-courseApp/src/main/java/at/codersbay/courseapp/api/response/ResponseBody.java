package at.codersbay.courseapp.api.response;

import java.util.ArrayList;

public class ResponseBody {

    private static final int ARRAY_INITIAL_CAPACITY = 3;

    private ArrayList<String> message = new ArrayList<>(ARRAY_INITIAL_CAPACITY);
    private ArrayList<String> errorMessage = new ArrayList<>(ARRAY_INITIAL_CAPACITY);

    public boolean addMessage(String message) {
        return this.message.add(message);
    }

    public boolean addErrorMessage(String message) {
        return this.errorMessage.add(message);
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    public ArrayList<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ArrayList<String> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
