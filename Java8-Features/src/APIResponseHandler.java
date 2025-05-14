import java.util.Arrays;
import java.util.List;
import java.util.Optional;

interface ResponseHandler<T> {
    void handleSuccess(T data);
    void handleError(Exception e);

    default void handleResponse(Optional<T> response) {
        // Implement default behavior to process an Optional response
//        response.ifPresentOrElse(
//                data ->{
//                    if(data!=null){
//                        handleSuccess(data);
//                    }else{
//                        handleError(new IllegalArgumentException("Null value received in successful Optional"));
//                    }
//                },
//        ()-> {
//            // Optional.empty() and Optional.ofNullable(null) are
//            // equivalent — they both are empty Optionals,
//            // and will trigger the () -> {} branch — not the ifPresent
//            // one. so "Empty Optional found" will show 2 times
//            System.out.println("Empty Optional found");
//        }
//        );
        // Only handle present Optionals (non-null wrapped)
        response.ifPresentOrElse(
                data -> handleSuccess(data),
                () -> System.out.println("No response received (Optional.empty), skipping...")
        );
    }
}

class StringResponseHandler implements ResponseHandler<String>{
    @Override
    public void handleSuccess(String data){
        System.out.println(data);
    }
    @Override
    public void handleError(Exception e) {
        System.out.println("Error : "+e.getMessage());
    }
}

public class APIResponseHandler {
    public static void main(String[] args) {

        String successResponse = "response received, status code : 200";
        String errorResponse = "response not received, status code : 404";
        String noResponse = null;
//        List<Optional<String>> responseList = Arrays.asList(
//                Optional.of(successResponse),
//                Optional.of(errorResponse),
//                Optional.empty(),
//                Optional.ofNullable(noResponse)); // otherwise throw NullPointerException @ runtime

        StringResponseHandler handler = new StringResponseHandler();
//        for (Optional<String> response : responseList) {
//            handler.handleResponse(response);
//        }
        List<String> rawResponses = Arrays.asList(successResponse, errorResponse, noResponse);
        for (String response : rawResponses) {
            if (response == null) {
                handler.handleError(new IllegalArgumentException("Null response before Optional wrapping"));
            } else {
                Optional<String> wrapped = Optional.of(response);
                handler.handleResponse(wrapped);
            }
        }

        // Separately test Optional.empty()
        System.out.println("\n--- Testing Optional.empty() case ---");
        handler.handleResponse(Optional.empty());
    }
}


